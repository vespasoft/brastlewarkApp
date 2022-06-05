package com.vespadev.brastlewarkapp.di

import android.content.Context
import androidx.work.WorkManager
import com.google.gson.GsonBuilder
import com.vespadev.brastlewarkapp.App
import com.vespadev.brastlewarkapp.data.resources.api.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@Suppress("unused")
@InstallIn(SingletonComponent::class)
class NetworkModule {
    companion object {
        const val BASE_URL_API = "https://raw.githubusercontent.com/"
        const val READ_TIMEOUT = 30
        const val WRITE_TIMEOUT = 30
        const val CONNECTION_TIMEOUT = 10
        const val CACHE_SIZE_BYTES = 10 * 1024 * 1024L // 10 MB
    }

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): App {
        return app as App
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        headerInterceptor: Interceptor,
        cache: Cache
    ): OkHttpClient {

        val okHttpClientBuilder = OkHttpClient().newBuilder()
        okHttpClientBuilder.connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.cache(cache)
        okHttpClientBuilder.addInterceptor(headerInterceptor)

        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideHeaderInterceptor(): Interceptor {
        return Interceptor {
            val requestBuilder = it.request().newBuilder()
            //hear you can add all headers you want by calling 'requestBuilder.addHeader(name ,  value)'
            it.proceed(requestBuilder.build())
        }
    }

    @Provides
    @Singleton
    internal fun provideCache(context: Context): Cache {
        val httpCacheDirectory = File(context.cacheDir.absolutePath, "HttpCache")
        return Cache(httpCacheDirectory, CACHE_SIZE_BYTES)
    }

    @Provides
    @Singleton
    fun provideContext(application: App): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideWorkManager(application: App): WorkManager {
        return WorkManager.getInstance(application)
    }

    @Provides
    @Singleton
    fun provideApi(client: OkHttpClient): Api {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_API)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
        return retrofit.create(Api::class.java)
    }
}
