package com.example.paintingjournal.converters

import android.net.Uri
import androidx.room.TypeConverter

class UriConverters {
    @TypeConverter
    fun fromString(value: String?) : Uri? {
        return Uri.parse(value)
    }

    @TypeConverter
    fun uriToString(uri: Uri?) : String {
        return uri.toString()
    }
}