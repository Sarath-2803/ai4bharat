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

    // 🔹 State selection for Home screen
    var selectedState by remember { mutableStateOf("Kerala") }
    var stateDropdownExpanded by remember { mutableStateOf(false) }
    val states = listOf("Kerala", "TN", "Maharashtra", "Karnataka", "All") // Add more states as needed

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI4Bharat") },
                actions = {
                    Row {
                        // Language dropdown
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

                        Spacer(modifier = Modifier.width(8.dp))

                        // State dropdown
                        Box {
                            TextButton(onClick = { stateDropdownExpanded = true }) {
                                Text(selectedState)
                            }
                            DropdownMenu(
                                expanded = stateDropdownExpanded,
                                onDismissRequest = { stateDropdownExpanded = false }
                            ) {
                                states.forEach { state ->
                                    DropdownMenuItem(
                                        text = { Text(state) },
                                        onClick = {
                                            selectedState = state
                                            stateDropdownExpanded = false
                                        }
                                    )
                                }
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
                0 -> {
                    // 🔹 Home Screen
                    val homeViewModel = remember { HomeViewModel() }
                    HomeScreen(
                        viewModel = homeViewModel,
                        selectedLanguage = selectedLanguage,
                        selectedState = selectedState
                    )
                }

                1 -> SchemeScreen(dao = dao, language = selectedLanguage)
                2 -> {
                    val chatViewModel = remember { ChatViewModel(dao) }
                    ChatBotScreen(
                        viewModel = chatViewModel,
                        selectedLanguage = selectedLanguage
                    )
                }

                3 -> TranslateScreen()

                4 -> EmergencyScreen(language = selectedLanguage)
//                    Text(
//                    text = when (selectedLanguage) {
//                        Language.EN -> "Profile Screen"
//                        Language.HI -> "प्रोफ़ाइल स्क्रीन"
//                        Language.ML -> "പ്രൊഫൈൽ സ്ക്രീൻ"
//                        Language.TA -> "சுயவிவர திரை"
//                    },
//                    modifier = Modifier.padding(16.dp)
//                )
            }
        }
    }
}