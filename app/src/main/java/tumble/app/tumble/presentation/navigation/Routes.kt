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
    const val accountResourceDetails = "account/resources?resourceId={id}"
    const val accountEvents = "account/events"
    const val accountEventDetails = "account/events?eventId={id}"

    // Deep links
    const val HomeUri = "https://app.tumbleforkronox.com/$home"
    const val HomeNewsUri = "https://app.tumbleforkronox.com/$homeNews"
    const val HomeNewsDetailsUri = "https://app.tumbleforkronox.com/$homeNewsDetails"
    const val BookmarksUri = "https://app.tumbleforkronox.com/$bookmarks"
    const val BookmarksDetailsUri = "https://app.tumbleforkronox.com/$bookmarksDetails"
    const val SearchUri = "https://app.tumbleforkronox.com/$search"
    const val SearchDetailsUri = "https://app.tumbleforkronox.com/$searchDetails"
    const val AccountUri = "https://app.tumbleforkronox.com/$account"
    const val AccountLoginUri = "https://app.tumbleforkronox.com/$accountLogin"

    // Protected deep links
    const val AccountSettingsUri = "https://app.tumbleforkronox.com/$accountSettings"
    const val AccountResourcesUri = "https://app.tumbleforkronox.com/$accountResources"
    const val AccountResourceDetailsUri = "https://app.tumbleforkronox.com/$accountResourceDetails"
    const val AccountEventsUri = "https://app.tumbleforkronox.com/$accountEvents"
    const val AccountEventDetailsUri = "https://app.tumbleforkronox.com/$accountEventDetails"
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
