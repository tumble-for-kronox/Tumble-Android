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
import com.tumble.kronoxtoapp.services.SchoolService
import com.tumble.kronoxtoapp.services.authentication.AuthenticationServiceProtocol
import com.tumble.kronoxtoapp.services.authentication.AuthenticationService
import com.tumble.kronoxtoapp.services.notifications.NotificationService
import com.tumble.kronoxtoapp.services.DataStoreService
import com.tumble.kronoxtoapp.services.RealmService
import com.tumble.kronoxtoapp.services.SecureStorageService
import com.tumble.kronoxtoapp.services.kronox.KronoxServiceProtocol
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KronoxModule {
    @Provides
    @Singleton
    fun provideKronoxApiService(retrofit: Retrofit): KronoxServiceProtocol {
        return retrofit.create(KronoxServiceProtocol::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext context: Context, realmService: RealmService): NotificationService {
        return NotificationService(context, realmService)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthenticationServiceProtocol {
        return retrofit.create(AuthenticationServiceProtocol::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthManager(
        authenticationServiceProtocol: AuthenticationServiceProtocol,
        secureStorageService: SecureStorageService,
        dataStoreService: DataStoreService
    ): AuthenticationService {
        return AuthenticationService(authenticationServiceProtocol, secureStorageService, dataStoreService)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object SecureStorageModel{
    @Provides
    @Singleton
    fun provideSecureStorageManager(@ApplicationContext context: Context): SecureStorageService {
        return SecureStorageService(context)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun providePreferenceService(@ApplicationContext context: Context): DataStoreService {
        return DataStoreService(context)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object SchoolModule{
    @Provides
    @Singleton
    fun provideSchoolManager(@ApplicationContext context: Context): SchoolService {
        return SchoolService(context)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RealmModule {
    @Provides
    @Singleton
    fun provideRealmManager(): RealmService {
        return RealmService()
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
