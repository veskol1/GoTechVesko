package com.example.gotechtest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gotechtest.R
import com.example.gotechtest.databinding.ItemMultipleQuestionBinding
import com.example.gotechtest.databinding.ItemRadioQuestionBinding
import com.example.gotechtest.databinding.ItemTextQuestionBinding

class GoTechAdapter(private var questions: ArrayList<QuestionViewItem>,  val listener: QuestionAnswerListener):
    RecyclerView.Adapter<GoTechViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoTechViewHolder {
        return when (viewType) {
            R.layout.item_radio_question -> GoTechViewHolder.RadioQuestionViewHolder(
                binding = ItemRadioQuestionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.item_text_question -> GoTechViewHolder.TextQuestionViewHolder(
                binding = ItemTextQuestionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            R.layout.item_multiple_question -> GoTechViewHolder.MultipleQuestionViewHolder(
                binding = ItemMultipleQuestionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Invalid ViewType Provided")
        }
    }

    override fun onBindViewHolder(holder: GoTechViewHolder, position: Int) {

        when (holder) {
            is GoTechViewHolder.RadioQuestionViewHolder -> {
                holder.bind(question = questions[position] as QuestionViewItem.Radio, listener = listener)
            }
            is GoTechViewHolder.TextQuestionViewHolder -> {
                holder.bind(question = questions[position] as QuestionViewItem.Text, listener = listener)
            }
            is GoTechViewHolder.MultipleQuestionViewHolder -> {
                holder.bind(question = questions[position] as QuestionViewItem.Multiple, listener = listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (questions[position]) {
            is QuestionViewItem.Radio -> R.layout.item_radio_question
            is QuestionViewItem.Text -> R.layout.item_text_question
            is QuestionViewItem.Multiple -> R.layout.item_multiple_question
        }
    }

    override fun getItemCount(): Int = questions.size

    fun updateQuestions(questions: ArrayList<QuestionViewItem>, shouldNotify: Boolean = false) {
        this.questions = questions
        if (shouldNotify) {
            notifyDataSetChanged()
        }
    }

    interface QuestionAnswerListener {
        fun onQuestionAnswered(
            id: String,
            selectedRadioText: String,
            checkButtonId: Int,
            userTypedAnswer: String = ""
        )
    }
}