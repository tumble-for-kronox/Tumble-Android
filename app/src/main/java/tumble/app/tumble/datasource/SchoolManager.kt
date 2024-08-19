package tumble.app.tumble.datasource

import android.content.Context
import androidx.collection.LruCache
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import tumble.app.tumble.domain.models.presentation.School
import java.io.InputStreamReader

class SchoolManager(private val context: Context) {
    private var schools: List<School> = listOf()
    private var ladokUrl: String = ""
    private val cache = LruCache<String, List<School>>(1)

    init{
         schools = loadJSON("Schools.json")
    }

    fun getLadokUrl():String{
        return ladokUrl
    }

    fun getSchools(): List<School>  {
        cache.get("schools")?.let {
            return it
        } ?: run { cache.put("schools", schools)
        return schools
        }
    }

    private inline fun <reified T> loadJSON(filename: String): T {
        val inputStream =  context.assets.open(filename)
        val reader = InputStreamReader(inputStream)

        val type = object : TypeToken<T>() {}.type

        return Gson().fromJson<T>(reader, type).also{
            reader.close()
        }
    }
}