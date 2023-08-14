package com.example.gotechtest.adapter

import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.gotechtest.R
import com.example.gotechtest.databinding.ItemMultipleQuestionBinding
import com.example.gotechtest.databinding.ItemRadioQuestionBinding
import com.example.gotechtest.databinding.ItemTextQuestionBinding


sealed class GoTechViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class RadioQuestionViewHolder(val binding: ItemRadioQuestionBinding) : GoTechViewHolder(binding) {

        fun bind(
            question: QuestionViewItem.Radio,
            listener: GoTechAdapter.QuestionAnswerListener
        ) {
            if (question.answerSelectedOrEntered) {
                val radioButton = when (question.selectedRadioButtonId) {
                    R.id.radio_answer_1 -> binding.radioAnswer1
                    R.id.radio_answer_2 -> binding.radioAnswer2
                    R.id.radio_answer_3 -> binding.radioAnswer3
                    else -> {binding.radioAnswer1}
                }
                radioButton.isChecked = true
            } else {
                binding.radioGroup.clearCheck()
            }

            binding.questionText.text = question.question
            binding.radioAnswer1.text = question.answer1
            binding.radioAnswer2.text = question.answer2
            binding.radioAnswer3.text = question.answer3

            binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->

                when (checkedId) {
                    R.id.radio_answer_1 -> {
                        if (binding.radioAnswer1.isChecked) {
                            listener.onQuestionAnswered(
                                id = question.id,
                                selectedRadioText = binding.radioAnswer1.text.toString(),
                                checkButtonId = checkedId
                            )
                        }
                    }

                    R.id.radio_answer_2 -> {
                        if (binding.radioAnswer2.isChecked) {
                            listener.onQuestionAnswered(
                                id = question.id,
                                selectedRadioText = binding.radioAnswer2.text.toString(),
                                checkButtonId = checkedId
                            )
                        }
                    }

                    R.id.radio_answer_3 -> {
                        if (binding.radioAnswer3.isChecked) {
                            listener.onQuestionAnswered(
                                id = question.id,
                                selectedRadioText = binding.radioAnswer3.text.toString(),
                                checkButtonId = checkedId
                            )
                        }
                    }
                }
            }
        }
    }

    class TextQuestionViewHolder(val binding: ItemTextQuestionBinding) : GoTechViewHolder(binding = binding) {
        fun bind(
            question: QuestionViewItem.Text,
            listener: GoTechAdapter.QuestionAnswerListener,
        ) {
            binding.textAnswer.addTextChangedListener {
                listener.onQuestionAnswered(
                    id = question.id,
                    selectedRadioText = "",
                    checkButtonId = 0,
                    userTypedAnswer = it.toString()
                )
            }

            if (question.answerSelectedOrEntered.not()) {
                binding.textAnswer.setText("")
            }

            if (binding.textAnswer.text.isEmpty()) {
                if (question.userTypedAnswer.isNotEmpty()) {
                    binding.textAnswer.setText(question.userTypedAnswer)
                }
            }
            binding.questionText.text = question.question
        }
    }

    class MultipleQuestionViewHolder(val binding: ItemMultipleQuestionBinding) : GoTechViewHolder(binding = binding) {

        fun bind(
            question: QuestionViewItem.Multiple,
            listener: GoTechAdapter.QuestionAnswerListener
        ) {
            if (binding.textAnswer.text.isEmpty()) {
                if (question.userTypedAnswer.isNotEmpty()) {
                    binding.textAnswer.setText(question.userTypedAnswer)
                }
            }

            if (question.answerSelectedOrEntered) {
                val radioButton = when (question.selectedRadioButtonId) {
                    R.id.radio_answer_1 -> binding.radioAnswer1
                    R.id.radio_answer_2 -> binding.radioAnswer2
                    R.id.radio_answer_3 -> binding.radioAnswer3
                    R.id.radio_answer_text -> binding.radioAnswerText
                    else -> {binding.radioAnswer1}
                }
                radioButton.isChecked = true
            } else {
                binding.radioGroup.clearCheck()
                binding.textAnswer.setText("")
                binding.textAnswer.isEnabled = false
            }

            binding.textAnswer.addTextChangedListener {
                //Log.d("haha","in second222222 listener")
                if (it.toString().isNotEmpty()) {
                    listener.onQuestionAnswered(
                        id = question.id,
                        selectedRadioText = binding.radioAnswerText.text.toString(),
                        checkButtonId = R.id.radio_answer_text,
                        userTypedAnswer = it.toString()
                    )
                }
            }

            if (question.userTypedAnswer.isNotEmpty()) {
                binding.textAnswer.setText(question.userTypedAnswer)
            }

            binding.questionText.text = question.question
            binding.radioAnswer1.text = question.answer1
            binding.radioAnswer2.text = question.answer2
            binding.radioAnswer3.text = question.answer3

            binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.radio_answer_1 -> {
                        if (binding.radioAnswer1.isChecked) {
                            binding.textAnswer.isEnabled = false
                            listener.onQuestionAnswered(
                                id = question.id,
                                selectedRadioText = binding.radioAnswer1.text.toString(),
                                checkButtonId = checkedId
                            )
                        }
                    }

                    R.id.radio_answer_2 -> {
                        if (binding.radioAnswer2.isChecked) {
                            binding.textAnswer.isEnabled = false
                            listener.onQuestionAnswered(
                                id = question.id,
                                selectedRadioText = binding.radioAnswer2.text.toString(),
                                checkButtonId = checkedId
                            )
                        }
                    }

                    R.id.radio_answer_3 -> {
                        if (binding.radioAnswer3.isChecked) {
                            binding.textAnswer.isEnabled = false
                            listener.onQuestionAnswered(
                                id = question.id,
                                selectedRadioText = binding.radioAnswer3.text.toString(),
                                checkButtonId = checkedId
                            )
                        }
                    }

                    R.id.radio_answer_text -> {
                        if (binding.radioAnswerText.isChecked) {
                            binding.textAnswer.isEnabled = true
                            listener.onQuestionAnswered(
                                id = question.id,
                                selectedRadioText = binding.radioAnswerText.text.toString(),
                                checkButtonId = checkedId,
                                userTypedAnswer = binding.textAnswer.text.toString()
                            )
                        }
                    }
                }
            }
        }
    }
}