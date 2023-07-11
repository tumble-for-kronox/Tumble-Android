package tumble.app.tumble.core

import android.content.Context
import androidx.datastore.core.DataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import tumble.app.tumble.datasource.network.auth.AuthManager
import tumble.app.tumble.datasource.network.kronox.KronoxManager
import tumble.app.tumble.datasource.preferences.DataStoreManager
import tumble.app.tumble.datasource.realm.RealmManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KronoxModule {
    @Provides
    @Singleton
    fun provideKronoxManager(retrofit: Retrofit): KronoxManager {
        return KronoxManager(retrofit)
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
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val baseUrl = "${NetworkSettings.shared.scheme}://${NetworkSettings.shared.tumbleUrl}:${NetworkSettings.shared.port}"
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
}
