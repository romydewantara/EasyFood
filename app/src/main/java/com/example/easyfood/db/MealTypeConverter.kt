package com.example.easyfood.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters
class MealTypeConverter {

    @TypeConverter
    fun fromAnyToString(attr: Any?): String {
        return if (attr == null) ""
        else attr as String
    }

    @TypeConverter
    fun fromStringToAny(attr: String?): Any {
        return attr ?: ""
    }
}