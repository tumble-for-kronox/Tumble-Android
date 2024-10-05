package tumble.app.tumble.core

import tumble.app.tumble.BuildConfig

class NetworkSettings private constructor(val port: Int, val scheme: String, val tumbleUrl: String) {
    companion object {
        val shared: NetworkSettings = when (BuildConfig.BUILD_TYPE) {
            "debug" -> Environments.production
            else -> Environments.production
        }

        object Environments {
            // Production URL
            val production = NetworkSettings(
                port = 443, scheme = "https", tumbleUrl = "app.tumbleforkronox.com"
            )

            // Debug URL
            val development = NetworkSettings(
                port = 80, scheme = "https", tumbleUrl = "localhost"
            )
        }
    }
}
