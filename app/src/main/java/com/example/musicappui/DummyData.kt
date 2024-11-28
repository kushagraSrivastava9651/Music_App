package com.example.musicappui

import androidx.annotation.DrawableRes

data class Lib(@DrawableRes val icon:Int, val name:String)

val libraries= listOf<Lib>(
    Lib(R.drawable.baseline_format_list_bulleted_24,"Favourite NewsList"),
    Lib(R.drawable.baseline_article_24,"Previous Polls & Survey"),
    Lib(R.drawable.baseline_poll_24,"Polls and Surveys"),
    Lib(R.drawable.baseline_contact_phone_24,"Contact Us"),
    Lib(R.drawable.baseline_sports_martial_arts_24,"About Us")

)
