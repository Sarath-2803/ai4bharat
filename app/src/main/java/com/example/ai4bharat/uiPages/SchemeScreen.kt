package com.example.ai4bharat.uiPages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ai4bharat.Language
import com.example.ai4bharat.Scheme
import com.example.ai4bharat.SchemeDao
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchemeScreen(dao: SchemeDao, language: Language) {

    var query by remember { mutableStateOf("") }
    var selectedState by remember { mutableStateOf("All") }
    var expanded by remember { mutableStateOf(false) }
    var schemes by remember { mutableStateOf<List<Scheme>>(emptyList()) }

    val scope = rememberCoroutineScope()
    val states = listOf("All", "Kerala", "TN")

    // Helper function to select field based on language
    fun description(scheme: Scheme): String = when(language) {
        Language.EN -> scheme.descriptionEn
        Language.HI -> scheme.descriptionHi
        Language.ML -> scheme.descriptionMl
        Language.TA -> scheme.descriptionTa
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {

        Text(
            text = when(language) {
                Language.EN -> "Find Government Schemes"
                Language.HI -> "सरकारी योजनाएँ खोजें"
                Language.ML -> "സർക്കാർ പദ്ധതികൾ കണ്ടെത്തുക"
                Language.TA -> "அரசு திட்டங்களை கண்டறியவும்"
            },
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 State Dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedState,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select State") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                states.forEach { state ->
                    DropdownMenuItem(
                        text = { Text(state) },
                        onClick = {
                            selectedState = state
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🔹 Search
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text(
                when(language) {
                    Language.EN -> "Search scheme name"
                    Language.HI -> "योजना का नाम खोजें"
                    Language.ML -> "പദ്ധതി പേര് തിരയുക"
                    Language.TA -> "திட்டத்தின் பெயரை தேடவும்"
                }
            ) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            trailingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Scheme List
        LazyColumn {
            val filtered = schemes.filter {
                (selectedState == "All" || it.state == selectedState) &&
                        it.name.contains(query, ignoreCase = true)
            }

            items(filtered) { scheme ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = scheme.name,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(text = description(scheme))

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = when(language) {
                                Language.EN -> "State: ${scheme.state}"
                                Language.HI -> "राज्य: ${scheme.state}"
                                Language.ML -> "സംസ്ഥാനം: ${scheme.state}"
                                Language.TA -> "மாநிலம்: ${scheme.state}"
                            },
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }

    // 🔹 Load schemes when state changes
    LaunchedEffect(selectedState) {
        schemes = if (selectedState == "All") {
            dao.getAllSchemes()
        } else {
            dao.getSchemesForRegion(selectedState)
        }
    }
}