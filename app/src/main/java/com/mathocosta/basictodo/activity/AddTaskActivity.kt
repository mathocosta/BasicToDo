package com.mathocosta.basictodo.activity

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.mathocosta.basictodo.databinding.ActivityAddTaskBinding
import com.mathocosta.basictodo.datasource.TaskDataSource
import com.mathocosta.basictodo.model.Task
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddTaskBinding.inflate(layoutInflater) }

    private lateinit var viewModel: AddTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(AddTaskViewModel::class.java)

        binding.toolbar.let {
            setSupportActionBar(it)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        setClickListeners()
        handleIntentExtra()
    }

    private fun setClickListeners() {
        with(binding) {
            tilDate.editText?.setOnClickListener { handleDateInputClick() }

            tilTime.editText?.setOnClickListener { handleTimeInputClick() }

            btnCancel.setOnClickListener { finish() }

            btnNewTask.setOnClickListener {
                viewModel.saveTask(
                    Task(
                        title = tilTitle.editText?.text.toString(),
                        date = tilDate.editText?.text.toString(),
                        time = tilTime.editText?.text.toString()
                    )
                )

                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    private fun handleTimeInputClick() {
        val tilTime = binding.tilTime
        val timePicker =
            MaterialTimePicker.Builder().apply {
                setTimeFormat(TimeFormat.CLOCK_24H)
                getDateFrom(tilTime.editText?.text.toString(), "HH:mm")?.let { actualDate ->
                    val calendar = Calendar.getInstance()
                    calendar.time = actualDate
                    setHour(calendar.get(Calendar.HOUR_OF_DAY))
                    setMinute(calendar.get(Calendar.MINUTE))
                }
            }.build()

        timePicker.addOnPositiveButtonClickListener {
            val timeText = "${timePicker.hour}:${timePicker.minute}"
            tilTime.editText?.setText(timeText)
        }

        timePicker.show(supportFragmentManager, "TIME_PICKER_TAG")
    }

    private fun handleDateInputClick() {
        val tilDate = binding.tilDate
        val dateFieldPattern = "dd/MM/yyyy"
        val datePicker = MaterialDatePicker.Builder.datePicker().apply {
            getDateFrom(
                tilDate.editText?.text.toString(),
                dateFieldPattern
            )?.let { actualDate ->
                setSelection(actualDate.time)
            }
        }.build()

        datePicker.addOnPositiveButtonClickListener {
            val offset = TimeZone.getDefault().getOffset(Date().time) * -1
            val date = Date(it + offset)
            val dateFormatter = SimpleDateFormat(dateFieldPattern, Locale("pt", "BR"))
            tilDate.editText?.setText(dateFormatter.format(date))
        }

        datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
    }

    private fun handleIntentExtra() {
        if (intent.hasExtra(ADD_TASK_ID)) {
            val taskId = intent.getIntExtra(ADD_TASK_ID, 0)
            viewModel.editingTaskId = taskId
            viewModel.getTaskById(taskId).let {
                binding.tilTitle.editText?.setText(it.title)
                binding.tilDate.editText?.setText(it.date)
                binding.tilTime.editText?.setText(it.time)
            }
        }
    }

    private fun getDateFrom(source: String, pattern: String): Date? {
        val dtFormatter = SimpleDateFormat(pattern, Locale("pt", "BR"))
        return try {
            dtFormatter.parse(source)
        } catch (ex: ParseException) {
            ex.printStackTrace()
            null
        }
    }

    companion object {
        const val ADD_TASK_ID = "TASK_ID"
    }
}