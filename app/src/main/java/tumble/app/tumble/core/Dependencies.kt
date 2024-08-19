package tumble.app.tumble.core

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
import tumble.app.tumble.BuildConfig
import tumble.app.tumble.datasource.SchoolManager
import tumble.app.tumble.datasource.network.auth.AuthManager
import tumble.app.tumble.datasource.network.kronox.KronoxRepository
import tumble.app.tumble.datasource.preferences.DataStoreManager
import tumble.app.tumble.datasource.realm.RealmManager
import tumble.app.tumble.presentation.viewmodels.EventDetailsSheetViewModel
import tumble.app.tumble.presentation.viewmodels.SearchPreviewViewModel
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.inject.Provider
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object KronoxModule {
    @Provides
    @Singleton
    fun provideKronoxManager(retrofit: Retrofit): KronoxRepository {
        return KronoxRepository(retrofit)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthManager(retrofit: Retrofit): AuthManager {
        return AuthManager(retrofit)
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
    fun provideSchoolManager(@ApplicationContext context: Context): SchoolManager{
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
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val baseUrl = "${NetworkSettings.shared.scheme}://${NetworkSettings.shared.tumbleUrl}:${NetworkSettings.shared.port}"
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}
