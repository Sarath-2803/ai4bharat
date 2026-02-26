package com.example.ai4bharat

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schemes")
data class Scheme(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val description: String,
    val state: String
)