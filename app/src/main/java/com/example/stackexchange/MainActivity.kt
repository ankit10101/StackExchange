package com.example.stackexchange

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val gson = Gson()
    val questionsList = ArrayList<Question>()
    val questionAdapter = QuestionAdapter(questionsList)
    lateinit var spinnerItem: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner.onItemSelectedListener = this
        val categories = ArrayList<String>()
        categories.add("activity")
        categories.add("votes")
        categories.add("creation")
        val dataAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = dataAdapter

        btnSearch.setOnClickListener {
            fetchQuestions(spinnerItem)
        }
    }

    fun fetchQuestions(sortBy: String) {
        val tag = etTag.text.toString()
        if (tag != "")
            questionsList.clear()
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.stackexchange.com/2.2/questions?order=desc&sort=$sortBy&tagged=$tag&site=stackoverflow")
            .build()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                client.newCall(request).enqueue(this)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody: ResponseBody? = response.body()
                val result = responseBody?.string()
                Log.e("TAG", result)
                val questionsResponse = gson.fromJson(result, QuestionResponse::class.java)
                Log.e("Question", questionsResponse.items[0].toString())
                questionsList.addAll(questionsResponse.items)
                runOnUiThread {
                    questionAdapter.notifyDataSetChanged()
                }
            }
        })
        rvQuestions.layoutManager = LinearLayoutManager(this)
        rvQuestions.adapter = questionAdapter
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = parent?.getItemAtPosition(position).toString()
        spinnerItem = item
        questionsList.clear()
        fetchQuestions(item)
    }

}
