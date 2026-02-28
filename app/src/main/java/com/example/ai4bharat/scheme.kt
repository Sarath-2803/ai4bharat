package com.example.ai4bharat

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schemes")
data class Scheme(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val category: String,
    val state: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val descriptionMl: String,
    val descriptionTa: String,
    val eligibilityEn: String,
    val eligibilityHi: String,
    val eligibilityMl: String,
    val eligibilityTa: String,
    val benefitsEn: String,
    val benefitsHi: String,
    val benefitsMl: String,
    val benefitsTa: String,
    val howToApplyEn: String,
    val howToApplyHi: String,
    val howToApplyMl: String,
    val howToApplyTa: String,
    val tags: String = ""
)
