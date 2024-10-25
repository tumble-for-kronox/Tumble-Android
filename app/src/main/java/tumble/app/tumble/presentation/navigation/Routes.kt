package tumble.app.tumble.presentation.navigation

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
    const val HomeUri = "https://tumble.hkr.se/$home"
    const val HomeNewsUri = "https://tumble.hkr.se/$homeNews"
    const val HomeNewsDetailsUri = "https://tumble.hkr.se/$homeNewsDetails"
    const val BookmarksUri = "https://tumble.hkr.se/$bookmarks"
    const val BookmarksDetailsUri = "https://tumble.hkr.se/$bookmarksDetails"
    const val SearchUri = "https://tumble.hkr.se/$search"
    const val SearchDetailsUri = "https://tumble.hkr.se/$searchDetails"
    const val AccountUri = "https://tumble.hkr.se/$account"
    const val AccountLoginUri = "https://tumble.hkr.se/$accountLogin"

    // Protected deep links
    const val AccountSettingsUri = "https://tumble.hkr.se/$accountSettings"
    const val AccountResourcesUri = "https://tumble.hkr.se/$accountResources"
    const val AccountResourceDetailsUri = "https://tumble.hkr.se/$accountResourceDetails"
    const val AccountEventsUri = "https://tumble.hkr.se/$accountEvents"
    const val AccountEventDetailsUri = "https://tumble.hkr.se/$accountEventDetails"
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
