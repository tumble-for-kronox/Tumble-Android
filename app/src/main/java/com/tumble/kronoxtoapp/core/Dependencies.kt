package com.tumble.kronoxtoapp.core

import android.content.Context
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
import com.tumble.kronoxtoapp.data.repository.SchoolManager
import com.tumble.kronoxtoapp.data.api.auth.AuthApiService
import com.tumble.kronoxtoapp.data.api.auth.AuthManager
import com.tumble.kronoxtoapp.data.notifications.NotificationManager
import com.tumble.kronoxtoapp.data.repository.preferences.DataStoreManager
import com.tumble.kronoxtoapp.data.repository.realm.RealmManager
import com.tumble.kronoxtoapp.data.repository.securestorage.SecureStorageManager
import com.tumble.kronoxtoapp.data.api.kronox.KronoxApiService
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
    fun provideNotificationManager(@ApplicationContext context: Context, realmManager: RealmManager): NotificationManager {
        return NotificationManager(context, realmManager)
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
    fun provideSecureStorageManager(@ApplicationContext context: Context): SecureStorageManager {
        return SecureStorageManager(context)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object dataStoreManager {
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
        return OkHttpClient.Builder()
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
