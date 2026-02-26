package com.example.ai4bharat

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DatabaseInitializer {

    suspend fun populateIfEmpty(dao: SchemeDao) {

        withContext(Dispatchers.IO) {

            val existing = dao.searchSchemes("")

            if (existing.isEmpty()) {

                val schemes = listOf(

                    Scheme(
                        name = "PM Kisan Yojana",
                        description = "₹6000 per year financial support for small and marginal farmers.",
                        state = "All"
                    ),

                    Scheme(
                        name = "Ayushman Bharat",
                        description = "Health insurance coverage up to ₹5 lakh per family per year.",
                        state = "All"
                    ),

                    Scheme(
                        name = "PM Awas Yojana",
                        description = "Affordable housing scheme for urban and rural poor.",
                        state = "All"
                    ),

                    Scheme(
                        name = "MGNREGA",
                        description = "100 days of guaranteed wage employment for rural households.",
                        state = "All"
                    ),

                    Scheme(
                        name = "PM Jan Dhan Yojana",
                        description = "Zero balance bank account for financial inclusion.",
                        state = "All"
                    ),

                    Scheme(
                        name = "Ujjwala Yojana",
                        description = "Free LPG connections for women from BPL households.",
                        state = "All"
                    ),

                    Scheme(
                        name = "Skill India Mission",
                        description = "Skill development training programs for youth.",
                        state = "All"
                    ),

                    Scheme(
                        name = "Stand Up India",
                        description = "Bank loans for SC/ST and women entrepreneurs.",
                        state = "All"
                    ),

                    Scheme(
                        name = "Digital India",
                        description = "Improving digital infrastructure and online government services.",
                        state = "All"
                    ),

                    Scheme(
                        name = "Startup India",
                        description = "Support and incentives for startups and entrepreneurs.",
                        state = "All"
                    ),

                    Scheme(
                        name = "Karunya Health Scheme",
                        description = "Kerala government medical financial assistance scheme.",
                        state = "Kerala"
                    ),

                    Scheme(
                        name = "Kudumbashree Mission",
                        description = "Kerala poverty eradication and women empowerment program.",
                        state = "Kerala"
                    ),

                    Scheme(
                        name = "Amma Canteen",
                        description = "Tamil Nadu subsidized food scheme.",
                        state = "Tamil Nadu"
                    )
                )

                schemes.forEach { dao.insertScheme(it) }
            }
        }
    }
}