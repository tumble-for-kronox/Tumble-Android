package tumble.app.tumble.data.repository.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import tumble.app.tumble.domain.enums.Types.AppearanceType
import tumble.app.tumble.domain.enums.ViewType
import tumble.app.tumble.presentation.views.Settings.Preferences.Notifications.NotificationOffset
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext


data class CombinedData(val authSchoolId: Int? = null, val autoSignup: Boolean? = null, val viewType: ViewType? = null, val userOnBoarded: Boolean? = null, val appearance: AppearanceType? = null, val notificationOffset: NotificationOffset? = null)

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val coroutineContext: CoroutineContext = Dispatchers.IO
) {
    private val scope = CoroutineScope(coroutineContext)
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private object PreferencesKeys {
        val AUTH_SCHOOL_ID = intPreferencesKey("auth_school_id")
        val USER_ON_BOARDED = booleanPreferencesKey("user_on_boarded")
        val NOTIFICATION_OFFSET = intPreferencesKey("notification_offset")
        val AUTO_SIGNUP = booleanPreferencesKey("auto_signup")
        val APPEARANCE = stringPreferencesKey("appearance")
        val VIEW_TYPE = stringPreferencesKey("view_type")
    }

    private val _authSchoolId = MutableStateFlow(-1)
    val authSchoolId: StateFlow<Int> = _authSchoolId

    private val _userOnBoarded = MutableStateFlow(false)
    val userOnBoarded: StateFlow<Boolean> = _userOnBoarded

    private val _autoSignup = MutableStateFlow(false)
    val autoSignup: StateFlow<Boolean> = _autoSignup

    private val _viewType = MutableStateFlow(ViewType.WEEK)
    val viewType: StateFlow<ViewType> = _viewType

    private val _appearance = MutableStateFlow(AppearanceType.AUTOMATIC)
    val appearance: StateFlow<AppearanceType> = _appearance

    private val _notificationOffset = MutableStateFlow(NotificationOffset.Thirty)
    val notificationOffset: StateFlow<NotificationOffset> = _notificationOffset

    init {
        context.dataStore.data
            .map { preferences ->
                _authSchoolId.value = preferences[PreferencesKeys.AUTH_SCHOOL_ID] ?: -1
                _userOnBoarded.value = preferences[PreferencesKeys.USER_ON_BOARDED] ?: false
                _viewType.value = ViewType.valueOf(preferences[PreferencesKeys.VIEW_TYPE] ?: ViewType.WEEK.name)
                _autoSignup.value = preferences[PreferencesKeys.AUTO_SIGNUP] ?: false
                _appearance.value = AppearanceType.valueOf(preferences[PreferencesKeys.APPEARANCE] ?: AppearanceType.AUTOMATIC.name)
                _notificationOffset.value = NotificationOffset.allCases.first {it.value == (preferences[PreferencesKeys.NOTIFICATION_OFFSET]?: NotificationOffset.Thirty.value) }
            }
            .launchIn(scope)
    }

    suspend fun setAuthSchoolId(id: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.AUTH_SCHOOL_ID] = id
        }
        _authSchoolId.value = id
    }

    suspend fun setUserOnBoarded(userOnBoarded: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ON_BOARDED] = userOnBoarded
        }
        _userOnBoarded.value = userOnBoarded
    }

    suspend fun setNotificationOffset(offset: NotificationOffset) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATION_OFFSET] = offset.value
        }
    }

    suspend fun setAppearance(appearance: AppearanceType) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.APPEARANCE] = appearance.name
        }
    }

    suspend fun setAutoSignup(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.AUTO_SIGNUP] = value
        }
    }

    suspend fun setBookmarksViewType(type: ViewType) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.VIEW_TYPE] = type.name
        }
    }
}