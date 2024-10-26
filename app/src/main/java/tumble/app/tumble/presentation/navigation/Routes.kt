package tumble.app.tumble.presentation.navigation

import tumble.app.tumble.core.NetworkSettings.Companion.shared

object Routes {
    const val home = "home"
    const val homeNews = "home/news"
    const val homeNewsDetails = "home/news?articleId={id}"
    const val bookmarks = "bookmarks"
    const val bookmarksDetails = "bookmarks?eventId={id}"
    const val search = "search"
    const val searchDetails = "search?scheduleId={id}"
    const val account = "account"
    const val accountLogin = "account/login"
    const val accountSettings = "account/settings"
    const val accountSettingsAppearance = "account/settings/appearance"
    const val accountSettingsLanguage = "account/settings/language"
    const val accountSettingsNotifications = "account/settings/notifications"
    const val accountSettingsBookmarks = "account/settings/bookmarks"

    // Protected routes
    const val accountResources = "account/resources"
    const val accountResourceDetails = "account/resources/{id}"
    const val accountEvents = "account/events"
    const val accountEventDetails = "account/events?eventId={id}"

    // Deep links
    var HomeUri = "https://${shared.tumbleUrl}/$home"
    var HomeNewsUri = "https://${shared.tumbleUrl}/$homeNews"
    var HomeNewsDetailsUri = "https://${shared.tumbleUrl}/$homeNewsDetails"
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
    fun buildHomeNewsDetailsUri(articleId: String): String {
        return Routes.HomeNewsDetailsUri.replace("{id}", articleId)
    }

    fun buildBookmarksDetailsUri(eventId: String): String {
        return Routes.BookmarksDetailsUri.replace("{id}", eventId)
    }

    fun buildSearchDetailsUri(scheduleId: String): String {
        return Routes.SearchDetailsUri.replace("{id}", scheduleId)
    }

    fun buildAccountResourceDetailsUri(resourceId: String): String {
        return Routes.AccountResourceDetailsUri.replace("{id}", resourceId)
    }

    fun buildAccountEventDetailsUri(eventId: String): String {
        return Routes.AccountEventDetailsUri.replace("{id}", eventId)
    }
}
