package com.tumble.kronoxtoapp.data.repository.realm

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import com.tumble.kronoxtoapp.domain.models.realm.Course
import com.tumble.kronoxtoapp.domain.models.realm.Day
import com.tumble.kronoxtoapp.domain.models.realm.Event
import com.tumble.kronoxtoapp.domain.models.realm.Location
import com.tumble.kronoxtoapp.domain.models.realm.Schedule
import com.tumble.kronoxtoapp.domain.models.realm.Teacher
import javax.inject.Singleton

@Singleton
class RealmManager {

    private var config = RealmConfiguration.create(
        schema = setOf(
            Schedule::class,
            Day::class,
            Event::class,
            Course::class,
            Location::class,
            Teacher::class
        )
    )

    private val realm: Realm
        get() = try {
            Realm.open(config)
        } catch (e: Exception) {
            throw RuntimeException("Couldn't open Realm", e)
        }

    fun getAllSchedules(): List<Schedule> {
        return realm.query<Schedule>().find().toList()
    }

    fun getAllLiveSchedules(): RealmResults<Schedule> {
        return realm.query<Schedule>().find()
    }

    suspend fun deleteAllSchedules() {
        realm.write {
            val schedules: RealmResults<Schedule> = this.query<Schedule>().find()
            delete(schedules)
        }
    }

    suspend fun updateCourseColors(courseId: String, color: String) {
        realm.write {
            val coursesToUpdate = this.query<Course>("courseId == $0", courseId).find()

            coursesToUpdate.forEach { course ->
                if (course.color != color) {
                    course.color = color
                }
            }
        }
    }


    suspend fun updateSchedule(scheduleId: String, newSchedule: Schedule) {
        realm.write {
            val schedule: Schedule? = this.query<Schedule>("scheduleId == $0", scheduleId).first().find()
            schedule?.days = newSchedule.days
            schedule?.cachedAt = newSchedule.cachedAt
        }
    }

    suspend fun updateScheduleVisibility(scheduleId: String, visibility: Boolean){
        realm.write {
            val schedule: Schedule? = this.query<Schedule>("scheduleId == $0", scheduleId).first().find()
            schedule?.toggled = visibility
        }
    }

    fun getCourseColors(): MutableMap<String, String> {
        val courses: RealmResults<Course> = realm.query<Course>().find()
        val courseColors = mutableMapOf<String, String>()
        for (course in courses) {
            course.courseId?.let {
                    id -> course.color?.let { color -> courseColors.put(id, color) }
            }
        }
        return courseColors
    }

    suspend fun saveSchedule(schedule: Schedule) {
        realm.write {
            this.copyToRealm(Schedule().apply {
                scheduleId = schedule.scheduleId
                title = schedule.title
                cachedAt = schedule.cachedAt
                days = schedule.days
                toggled = schedule.toggled
                schoolId = schedule.schoolId
                requiresAuth = schedule.requiresAuth
            })
        }
    }

    suspend fun deleteSchedule(schedule: Schedule) {
        realm.write {
            findLatest(schedule)?.let { delete(it) }
        }
    }

    fun getScheduleByScheduleId(scheduleId: String): Schedule? {
        return realm.query<Schedule>("scheduleId == $0", scheduleId).first().find()
    }
}
