package com.example.stackexchange

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_question.view.*

class QuestionAdapter(private val questions: List<Question>) : RecyclerView.Adapter<QuestionAdapter.QuestionHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): QuestionHolder {
        val inflatedView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_question, viewGroup, false)
        return QuestionHolder(inflatedView)
    }

    override fun getItemCount() = questions.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(questionHolder: QuestionHolder, position: Int) {

        val currentQuestion = questions[position]
        with(questionHolder.itemView) {
            tvTitle.text = currentQuestion.title
            tvScore.text = "Score" + currentQuestion.score.toString()
            tvNumberOfAnswer.text = "Number of Answers:" + currentQuestion.answer_count.toString()
            tvAskedOn.text = currentQuestion.creation_date.toString()
            tvAskedBy.text = currentQuestion.owner.display_name
            com.squareup.picasso.Picasso.get()
                .load(currentQuestion.owner.profile_image)
//                .placeholder(R.drawable.loading)
//                .error(R.drawable.loading_failed)
                .into(ivUser)
        }
        questionHolder.itemView.setOnClickListener {
            val i = Intent()
            i.action = Intent.ACTION_VIEW
            i.data = Uri.parse(currentQuestion.link)
            questionHolder.itemView.context.startActivity(i)
        }

    }

    inner class QuestionHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}