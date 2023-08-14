package com.example.gotechtest.repository

import com.example.gotechtest.adapter.QuestionViewItem
import com.example.gotechtest.api.Api
import com.example.gotechtest.model.Answer
import com.example.gotechtest.model.Question
import com.example.gotechtest.viewmodel.Status
import javax.inject.Inject

class GoTechRepository @Inject constructor(private val api: Api) {

    suspend fun getQuestions(): ArrayList<QuestionViewItem>? {
        return try {
            val response = api.getQuestions()
            if (response.isSuccessful && response.body() != null) {
                getQuestionViewItemList(response.body()!!)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun setAnswers(answers: ArrayList<Answer>): Status {
        return try {
            val response = api.postAnswers(answers)
            if (response.isSuccessful && response.body() != null) {
                Status.DONE
            } else {
                Status.ERROR
            }

        } catch (e: Exception) {
            Status.ERROR
        }
    }

    private fun getQuestionViewItemList(questions: ArrayList<Question>): ArrayList<QuestionViewItem> {
        val questionViewItem = arrayListOf<QuestionViewItem>()
        questions.forEach { q ->
            when (q.type) {
                "1" -> {
                    questionViewItem.add(
                        QuestionViewItem.Radio(
                            id = q.id,
                            question = q.question,
                            answer1 = q.answers[0],
                            answer2 = q.answers[1],
                            answer3 = q.answers[2],
                        )
                    )
                }
                "2" -> {
                    questionViewItem.add(
                        QuestionViewItem.Text(
                            id = q.id,
                            question = q.question,
                        )
                    )
                }
                "3" -> {
                    questionViewItem.add(
                        QuestionViewItem.Multiple(
                            id = q.id,
                            question = q.question,
                            answer1 = q.answers[0],
                            answer2 = q.answers[1],
                            answer3 = q.answers[2]
                        )
                    )
                }
            }

        }

        return questionViewItem
    }

}