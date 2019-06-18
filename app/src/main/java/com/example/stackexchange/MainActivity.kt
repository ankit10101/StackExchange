package com.example.stackexchange

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val gson = Gson()
    val questionsList = ArrayList<Question>()
    val questionAdapter = QuestionAdapter(questionsList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSearch.setOnClickListener {
            val tag = etTag.text.toString()
            if(tag != "")
                questionsList.clear()
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://api.stackexchange.com/2.2/questions?order=desc&sort=activity&tagged=$tag&site=stackoverflow")
                .build()
            val call = client.newCall(request)
            call.enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    client.newCall(request).enqueue(this)
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody: ResponseBody? = response.body()
                    val result = responseBody?.string()
                    Log.e("TAG", result)
                    val questionsResponse = gson.fromJson(result, QuestionResponse::class.java)
                    Log.e("Question",questionsResponse.items[0].toString())
                    questionsList.addAll(questionsResponse.items)
                    runOnUiThread {
                        questionAdapter.notifyDataSetChanged()
                    }
                }
            })
            rvQuestions.layoutManager = LinearLayoutManager(this)
            rvQuestions.adapter = questionAdapter
        }
    }
}
