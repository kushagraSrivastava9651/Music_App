package com.example.musicappui.ui.theme

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    @SerializedName("description") val description: String = "",
    @SerializedName("id") val id: Int =0,
    @SerializedName("image") val image: String = "",
    @SerializedName("title") val title: String= ""

   // Changed from imageUrl to image
) : Parcelable

data class CategoriesResponse(
    @SerializedName("categories") val categories: List<Item>
)
