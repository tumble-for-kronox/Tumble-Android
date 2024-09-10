package tumble.app.tumble.domain.enums.Types

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import tumble.app.tumble.R
import tumble.app.tumble.domain.enums.ViewType

enum class AppearanceType {
    AUTOMATIC, LIGHT, DARK
}

@Composable
fun appearenceTypeToStringResource(appearanceType: AppearanceType): String {
    return when(appearanceType){
        AppearanceType.AUTOMATIC -> {
            stringResource(id = R.string.list)
        }
        AppearanceType.LIGHT -> {
            stringResource(id = R.string.calendar)
        }
        AppearanceType.DARK -> {
            stringResource(id = R.string.week)
        }
    }
}
