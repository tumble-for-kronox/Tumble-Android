package tumble.app.tumble.domain.models.network

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

data class Token(
    val value: String,
    val createdDate: LocalDateTime
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun isExpired(): Boolean {
        val currentDate = LocalDateTime.now()
        return createdDate.plusSeconds(1314900000) <= currentDate
    }
}

enum class TokenType(val type: String) {
    REFRESH_TOKEN("refresh-token"),
    SESSION_TOKEN("session-token")
}
