package com.tumble.kronoxtoapp.services

import android.content.Context
import androidx.collection.LruCache
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tumble.kronoxtoapp.domain.models.presentation.School
import java.io.InputStreamReader

class SchoolService(private val context: Context) {
    private var schools: List<School> = listOf()
    private val cache = LruCache<String, List<School>>(1)

    init {
        schools = loadJSON("Schools.json")
    }

    fun getSchools(): List<School> {
        cache["schools"]?.let {
            return it
        } ?: run {
            cache.put("schools", schools)
            return schools
        }
    }

    fun getSchoolById(id: Int? = -1): School? {
        return schools.firstOrNull { it.id == id }
    }

    private inline fun <reified T> loadJSON(filename: String): T {
        val inputStream = context.assets.open(filename)
        val reader = InputStreamReader(inputStream)

        val type = object : TypeToken<T>() {}.type

        return Gson().fromJson<T>(reader, type).also {
            reader.close()
        }
    }
}