package tumble.app.tumble.core

import android.content.Context
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import tumble.app.tumble.BuildConfig
import tumble.app.tumble.datasource.SchoolManager
import tumble.app.tumble.data.api.HeadersInterceptor
import tumble.app.tumble.data.api.auth.AuthApiService
import tumble.app.tumble.data.api.auth.AuthManager
import tumble.app.tumble.data.notifications.NotificationManager
import tumble.app.tumble.data.repository.preferences.DataStoreManager
import tumble.app.tumble.data.repository.realm.RealmManager
import tumble.app.tumble.data.repository.securestorage.SecureStorageManager
import tumble.app.tumble.datasource.network.kronox.KronoxApiService
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object KronoxModule {
    @Provides
    @Singleton
    fun provideKronoxApiService(retrofit: Retrofit): KronoxApiService {
        return retrofit.create(KronoxApiService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    @Provides
    @Singleton
    fun provideNotificationManager(): NotificationManager {
        return NotificationManager()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthManager(
        authApiService: AuthApiService,
        secureStorageManager: SecureStorageManager,
        dataStoreManager: DataStoreManager
    ): AuthManager {
        return AuthManager(authApiService, secureStorageManager, dataStoreManager)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object SecureStorageModel{
    @Provides
    @Singleton
    fun provideSecureStorageManager(@ApplicationContext context: Context): SecureStorageManager{
        return SecureStorageManager(context)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {
    @Provides
    @Singleton
    fun providePreferenceService(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object SchoolManager{
    @Provides
    @Singleton
    fun provideSchoolManager(@ApplicationContext context: Context): SchoolManager {
        return SchoolManager(context)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RealmModule {
    @Provides
    @Singleton
    fun provideRealmManager(): RealmManager {
        return RealmManager()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return if (BuildConfig.DEBUG) provideDevOkHttpClient() else OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val baseUrl = "${NetworkSettings.shared.scheme}://${NetworkSettings.shared.tumbleUrl}:${NetworkSettings.shared.port}"
        val okHttpClient = provideOkHttpClient()
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}
