package com.tumble.kronoxtoapp.core

import com.tumble.kronoxtoapp.BuildConfig

class NetworkSettings private constructor(val port: Int, val scheme: String, val tumbleUrl: String) {
    companion object {
        val shared: NetworkSettings = when (BuildConfig.BUILD_TYPE) {
            "debug" -> Environments.development
            else -> Environments.production
        }

        object Environments {
            val production = NetworkSettings(
                port = 443, scheme = "https", tumbleUrl = "app.tumbleforkronox.com"
            )

            val development = NetworkSettings(
                port = 443, scheme = "https", tumbleUrl = "app.tumbleforkronox.com"
            )
        }
    }
}
