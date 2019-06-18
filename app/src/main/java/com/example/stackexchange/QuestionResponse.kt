package com.example.stackexchange

import java.util.*

class QuestionResponse(var items: ArrayList<Question>)

class Question(
    var answer_count: Int,
    var title: String,
    var link: String,
    var creation_date: String,
    var is_answered: Boolean,
    var last_activity_date: String,
    var score: Int,
    var view_count: Int,
    var owner: Owner
)

class Owner(
    var profile_image: String,
    var display_name: String
)