package tumble.app.tumble.core

import tumble.app.tumble.BuildConfig

class NetworkSettings private constructor(val port: Int, val scheme: String, val tumbleUrl: String) {
    companion object {
        val shared: NetworkSettings = when (BuildConfig.BUILD_TYPE) {
            "debug" -> Environments.development
            else -> Environments.production
        }

        object Environments {
            // Production URL
            val production = NetworkSettings(
                port = 443, scheme = "https", tumbleUrl = "tumble.hkr.se"
            )

            // Debug URL
            val development = NetworkSettings(
                port = 7036, scheme = "https", tumbleUrl = "10.0.2.2"
            )
        }
    }
}
