package tumble.app.tumble.core

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import tumble.app.tumble.datasource.auth.AuthManager
import tumble.app.tumble.datasource.kronox.KronoxManager
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
