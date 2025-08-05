package com.tumble.kronoxtoapp.presentation.navigation

import com.tumble.kronoxtoapp.core.NetworkSettings.Companion.shared

object Routes {
    const val home = "home"
    const val homeNews = "home/news"
    const val homeNewsDetails = "home/news?articleId={id}"
    const val homeEventDetails = "home/event?eventId={event_id}"
    const val bookmarks = "bookmarks"
    const val bookmarksDetails = "bookmarks_details?eventId={event_id}"
    const val search = "search"
    const val searchDetails = "search?scheduleId={schedule_id}&schoolId={school_id}&scheduleTitle={schedule_title}"
    const val account = "account"
    const val accountLogin = "account/login"
    const val accountSettings = "account/settings"
    const val accountSettingsPreferences = "account/settings/preferences"
    const val accountSettingsAppearance = "account/settings/preferences/appearance"
    const val accountSettingsLanguage = "account/settings/preferences/language"
    const val accountSettingsNotifications = "account/settings/preferences/notifications"
    const val accountSettingsBookmarks = "account/settings/bookmarks"

    // Protected routes
    const val accountResources = "account/resources"
    const val accountResourceDetails = "account/resources/{id}"
    const val accountEvents = "account/events"
    const val accountEventDetails = "account/events/{id}"

    // Deep links
    var HomeUri = "https://${shared.tumbleUrl}/$home"
    var HomeNewsUri = "https://${shared.tumbleUrl}/$homeNews"
    var HomeNewsDetailsUri = "https://${shared.tumbleUrl}/$homeNewsDetails"
    var HomeEventDetailsUri = "https://${shared.tumbleUrl}/$homeEventDetails"
    var BookmarksUri = "https://${shared.tumbleUrl}/$bookmarks"
    var BookmarksDetailsUri = "https://${shared.tumbleUrl}/$bookmarksDetails"
    var SearchUri = "https://${shared.tumbleUrl}/$search"
    var SearchDetailsUri = "https://${shared.tumbleUrl}/$searchDetails"
    var AccountUri = "https://${shared.tumbleUrl}/$account"
    var AccountLoginUri = "https://${shared.tumbleUrl}/$accountLogin"

    // Protected deep links
    var AccountSettingsUri = "https://${shared.tumbleUrl}/$accountSettings"
    var AccountResourcesUri = "https://${shared.tumbleUrl}/$accountResources"
    var AccountResourceDetailsUri = "https://${shared.tumbleUrl}/$accountResourceDetails"
    var AccountEventsUri = "https://${shared.tumbleUrl}/$accountEvents"
    var AccountEventDetailsUri = "https://${shared.tumbleUrl}/$accountEventDetails"
}

object UriBuilder {
    fun buildSearchDetailsUri(scheduleId: String, schoolId: String, scheduleTitle: String): String {
        return Routes.SearchDetailsUri
            .replace("{schedule_id}", scheduleId)
            .replace("{school_id}", schoolId)
            .replace("{schedule_title}", scheduleTitle.trim())
    }

    fun buildBookmarksDetailsUri(eventId: String): String {
        return Routes.BookmarksDetailsUri
            .replace("{event_id}", eventId)
    }

    fun buildHomeEventDetailsUri(eventId: String): String {
        return Routes.HomeEventDetailsUri
            .replace("{event_id}", eventId)
    }

    fun buildAccountResourceDetailsUri(resourceId: String): String {
        return Routes.AccountResourceDetailsUri.replace("{id}", resourceId)
    }
}
