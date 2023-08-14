package com.example.gotechtest.di

import android.os.Build
import com.example.gotechtest.api.Api
import com.example.gotechtest.repository.GoTechRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModules {

    private val isEmulator = Build.HARDWARE.equals("ranchu")

    private val BASE_URL = "http://10.0.2.2:3000/".takeIf { isEmulator } ?: "http://127.0.0.1:3000/"

    @Singleton
    @Provides
    fun provideApi(): Api {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

    @Singleton
    @Provides
    fun provideGoTechRepo(api: Api): GoTechRepository = GoTechRepository(api = api)


}