package com.example.gotechtest

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gotechtest.adapter.GoTechAdapter
import com.example.gotechtest.databinding.ActivityMainBinding
import com.example.gotechtest.viewmodel.GoTechViewModel
import com.example.gotechtest.viewmodel.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), GoTechAdapter.QuestionAnswerListener {

    private lateinit var binding: ActivityMainBinding
    private val goTechViewModel: GoTechViewModel by viewModels()
    private val adapter = GoTechAdapter(questions = arrayListOf(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                goTechViewModel.uiState.collectLatest { uiState ->
                    when (uiState.fetchStatus) {
                        Status.LOADING -> {
                            viewsVisibility(hide = true)
                            binding.progressBar.visibility = View.VISIBLE
                            binding.errorView.visibility = View.GONE
                        }

                        Status.DONE -> {
                            viewsVisibility(hide = false)
                            binding.progressBar.visibility = View.GONE
                            binding.errorView.visibility = View.GONE
                            binding.submitButton.isEnabled = uiState.allRequiredAnswered
                            adapter.updateQuestions(uiState.questions, shouldNotify = uiState.submitClicked)

                            if (uiState.submitClicked) {
                                if (uiState.postStatus == Status.DONE) {
                                    Toast.makeText(this@MainActivity,resources.getString(R.string.save_to_server_done), Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this@MainActivity,resources.getString(R.string.save_to_server_error), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        Status.ERROR -> {
                            viewsVisibility(hide = true)
                            binding.progressBar.visibility = View.GONE
                            binding.errorView.visibility = View.VISIBLE
                        }

                        else -> {}
                    }
                }
            }
        }

        binding.submitButton.setOnClickListener {
            goTechViewModel.checkQuestionnaire()
        }
    }

    private fun viewsVisibility(hide: Boolean) {
        binding.recyclerView.visibility = View.GONE. takeIf { hide } ?: View.VISIBLE
        binding.topCard.visibility = View.GONE. takeIf { hide } ?: View.VISIBLE
        binding.submitButton.visibility = View.GONE. takeIf { hide } ?: View.VISIBLE
    }

    override fun onQuestionAnswered(
        id: String,
        selectedRadioText: String,
        checkButtonId: Int,
        userTypedAnswer: String
    ) {
        goTechViewModel.onQuestionAnswered(
            id = id,
            answer = selectedRadioText,
            checkButtonId = checkButtonId,
            userTypedAnswer = userTypedAnswer
        )
    }
}