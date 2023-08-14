package com.example.gotechtest.api

import com.example.gotechtest.model.Answer
import com.example.gotechtest.model.Question
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {

    @GET("questions/")
    suspend fun getQuestions(): Response<ArrayList<Question>>

    @POST("answers/")
    suspend fun postAnswers(@Body answers: ArrayList<Answer>) : Response<ArrayList<Answer>>

}