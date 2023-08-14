package com.example.gotechtest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gotechtest.adapter.QuestionViewItem
import com.example.gotechtest.model.Answer
import com.example.gotechtest.repository.GoTechRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoTechViewModel @Inject constructor(private val goTechRepository: GoTechRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(
        UiState(
            fetchStatus = Status.LOADING,
            questions = arrayListOf(),
            allRequiredAnswered = false,
            postStatus = Status.UNKNOWN,
            submitClicked = false
        )
    )

    var uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val response = goTechRepository.getQuestions()
            response?.let {
                updateState(status = Status.DONE, questions = response, submitClicked = false)
            } ?: run {
                updateState(status = Status.ERROR, submitClicked = false)
            }

        }
    }

    private fun sendAnswers(answers: ArrayList<Answer>) {
        viewModelScope.launch(Dispatchers.IO) {
            val sendResponse = goTechRepository.setAnswers(answers)
            resetForm(postStatus = sendResponse)
        }
    }

    private fun resetForm(postStatus: Status) {
        val questions = uiState.value.questions.toMutableList()
        questions.forEach {
            it.answerSelectedOrEntered = false
            it.selectedRadioButtonText = ""
            it.selectedRadioButtonId = 0
            it.userTypedAnswer = ""
        }

        updateState(
            questions = questions as ArrayList<QuestionViewItem>,
            allRequiredAnswered = false,
            postStatus = postStatus,
            submitClicked = true
        )
    }

    private fun updateState(
        status: Status = Status.DONE,
        questions: ArrayList<QuestionViewItem> = uiState.value.questions,
        allRequiredAnswered: Boolean = uiState.value.allRequiredAnswered,
        postStatus: Status = Status.UNKNOWN,
        submitClicked: Boolean = false
    ) {
        _uiState.update { state ->
            state.copy(
                fetchStatus = status,
                questions = questions,
                allRequiredAnswered = allRequiredAnswered,
                postStatus = postStatus,
                submitClicked = submitClicked
            )
        }
    }

    fun checkQuestionnaire() {
        val currentQuestionsState = uiState.value.questions

        if (uiState.value.allRequiredAnswered) {
            val answers = arrayListOf<Answer>()

            currentQuestionsState.forEach { question ->
                when (question) {
                    is QuestionViewItem.Radio -> {
                        answers.add(Answer(id = question.id, answer = question.selectedRadioButtonText))
                    }
                    is QuestionViewItem.Text -> {
                        answers.add(Answer(id = question.id, answer = question.userTypedAnswer))
                    }
                    is QuestionViewItem.Multiple -> {
                        if (question.userTypedAnswer.isEmpty()) {
                            answers.add(Answer(id = question.id, answer = question.selectedRadioButtonText))
                        } else {
                            answers.add(Answer(id = question.id, answer = question.userTypedAnswer))
                        }
                    }
                }
            }
            sendAnswers(answers)
        }
    }

    fun onQuestionAnswered(id: String, answer: String, checkButtonId: Int, userTypedAnswer: String) {
        val currentQuestions = uiState.value.questions.toMutableList()
        val question = currentQuestions.find { it.id == id }
        question!!.answerSelectedOrEntered = true
        question.selectedRadioButtonText = answer
        question.selectedRadioButtonId = checkButtonId
        question.userTypedAnswer = userTypedAnswer

        val allRequiredAnswered: Boolean =
            currentQuestions.none { it.isRequiredQuestion && it.answerSelectedOrEntered.not() }

        updateState(
            questions = currentQuestions as ArrayList<QuestionViewItem>,
            allRequiredAnswered = allRequiredAnswered,
        )
    }
}

data class UiState(
    val fetchStatus: Status,
    val questions: ArrayList<QuestionViewItem>,
    val allRequiredAnswered: Boolean,
    val postStatus: Status,
    val submitClicked: Boolean
)

enum class Status {
    LOADING,
    DONE,
    ERROR,
    UNKNOWN
}