package com.zoewave.myapplication.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {


    @Provides
    @Singleton
    fun provideAuthInterceptorOkHttpClient(
        //authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            //.addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://example.com")
            .build()
            //.create()//ApiServices::class.java)
    }


}


