package com.example.ai4bharat.uiPages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ai4bharat.Language
import com.example.ai4bharat.SchemeDao

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ai4BharatApp(dao: SchemeDao?) {
    if (dao == null) {
        Text("Database not available")
        return
    }

    var selectedTab by remember { mutableStateOf(0) }
    var selectedLanguage by remember { mutableStateOf(Language.EN) }

    var languageDropdownExpanded by remember { mutableStateOf(false) }
    val languageOptions = Language.values().toList()
    val tabs = listOf("Home", "Schemes", "AI", "Translate", "Emergency")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI4Bharat") },
                actions = {
                    Box {
                        TextButton(onClick = { languageDropdownExpanded = true }) {
                            Text(selectedLanguage.name)
                        }
                        DropdownMenu(
                            expanded = languageDropdownExpanded,
                            onDismissRequest = { languageDropdownExpanded = false }
                        ) {
                            languageOptions.forEach { lang ->
                                DropdownMenuItem(
                                    text = { Text(lang.name) },
                                    onClick = {
                                        selectedLanguage = lang
                                        languageDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, title ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        label = { Text(title) },
                        icon = { Text(" ") } // placeholder icon
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                0 -> Text(
                    text = when (selectedLanguage) {
                        Language.EN -> "Home Screen"
                        Language.HI -> "होम स्क्रीन"
                        Language.ML -> "ഹോം സ്ക്രീൻ"
                        Language.TA -> "முகப்பு திரை"
                    },
                    modifier = Modifier.padding(16.dp)
                )

                1 -> SchemeScreen(dao = dao, language = selectedLanguage)
                2 -> {
                    val chatViewModel = remember { ChatViewModel(dao) }
                    ChatBotScreen(
                        viewModel = chatViewModel,
                        selectedLanguage = selectedLanguage
                    )
                }

                3 -> Text(
                    text = when (selectedLanguage) {
                        Language.EN -> "Categories Screen"
                        Language.HI -> "श्रेणियाँ स्क्रीन"
                        Language.ML -> "വിഭാഗങ്ങൾ സ്ക്രീൻ"
                        Language.TA -> "வகைகள் திரை"
                    },
                    modifier = Modifier.padding(16.dp)
                )

                4 -> Text(
                    text = when (selectedLanguage) {
                        Language.EN -> "Profile Screen"
                        Language.HI -> "प्रोफ़ाइल स्क्रीन"
                        Language.ML -> "പ്രൊഫൈൽ സ്ക്രീൻ"
                        Language.TA -> "சுயவிவர திரை"
                    },
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}