package com.example.gotechtest.adapter

sealed class QuestionViewItem(
    val id: String,
    val isRequiredQuestion: Boolean,
    var answerSelectedOrEntered: Boolean = false,
    var selectedRadioButtonText: String = NO_SELECTED_BUTTON_TEXT,
    var selectedRadioButtonId: Int = NO_SELECTED_BUTTON_ID,
    var userTypedAnswer: String = "",
    ) {

    class Radio(
        id: String,
        isRequiredQuestion: Boolean = true,
        val question: String,
        val answer1: String,
        val answer2: String,
        val answer3: String,
    ) : QuestionViewItem(
        id = id,
        isRequiredQuestion = isRequiredQuestion,
    )

    class Text(
        id: String,
        isRequiredQuestion: Boolean = false,
        val question: String,
    ) : QuestionViewItem(
        id = id,
        isRequiredQuestion = isRequiredQuestion,
    )

    class Multiple(
        id: String,
        isRequiredQuestion: Boolean = true,

        val question: String,
        val answer1: String,
        val answer2: String,
        val answer3: String,
    ) : QuestionViewItem(
        id = id,
        isRequiredQuestion = isRequiredQuestion,
    )

    override fun equals(other: Any?): Boolean {
        return this.answerSelectedOrEntered != answerSelectedOrEntered ||
                this.selectedRadioButtonText != selectedRadioButtonText ||
                this.userTypedAnswer != userTypedAnswer
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + isRequiredQuestion.hashCode()
        result = 31 * result + answerSelectedOrEntered.hashCode()
        result = 31 * result + selectedRadioButtonText.hashCode()
        result = 31 * result + selectedRadioButtonId
        result = 31 * result + userTypedAnswer.hashCode()
        return result
    }

    companion object {
        const val NO_SELECTED_BUTTON_TEXT = ""
        const val NO_SELECTED_BUTTON_ID = 0
    }

}