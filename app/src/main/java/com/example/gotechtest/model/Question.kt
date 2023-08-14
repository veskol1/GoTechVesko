package com.example.gotechtest.model

data class Question(
    val id: String,
    val question: String,
    val type: String,
    val answers: ArrayList<String>
)