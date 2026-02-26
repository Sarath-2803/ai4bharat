package com.example.ai4bharat

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(this)
        val dao = db.schemeDao()


        // Insert dummy schemes when app starts
        lifecycleScope.launch {
            DatabaseInitializer.populateIfEmpty(dao)
        }

        setContent {
            Ai4BharatApp(dao)
        }

        if (checkSelfPermission(android.Manifest.permission.RECORD_AUDIO)
            != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.RECORD_AUDIO),
                1
            )
        }
    }
}

@Composable
fun Ai4BharatApp(dao: SchemeDao) {

    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf("Home", "Schemes","AI", "Translate", "Emergency")

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, title ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        label = { Text(title) },
                        icon = { } // You can add icons later
                    )
                }
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier.padding(paddingValues)
        ) {

            when (selectedTab) {
                0 -> Text("Home Screen", modifier = Modifier.padding(16.dp))
                1 -> SchemeScreen(dao)
                2 -> ChatBotScreen()
                3 -> Text("Categories Screen", modifier = Modifier.padding(16.dp))
                4 -> Text("Profile Screen", modifier = Modifier.padding(16.dp))
            }

        }
    }
}

@Composable
fun ChatBotScreen() {

    val context = LocalContext.current

    var message by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<String>()) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // 🔹 Chat Title
        Text(
            text = "Ask your doubts here",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium
        )

        // 🔹 Chat Messages
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            items(messages) { msg ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = msg,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        // 🔹 Input Area
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type your question...") },
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            // 🎤 Voice Button
            IconButton(
                onClick = {
                    // TODO: Add voice recognition logic
                    Toast.makeText(context, "Voice input clicked", Toast.LENGTH_SHORT).show()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice Input"
                )
            }

            // 📤 Send Button
            IconButton(
                onClick = {
                    if (message.isNotBlank()) {
                        messages = messages + "You: $message"
                        message = ""
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send"
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
    fun SchemeScreen(dao: SchemeDao) {

        var query by remember { mutableStateOf("") }
        var result by remember { mutableStateOf("") }
        var selectedState by remember { mutableStateOf("Kerala") }
        var expanded by remember { mutableStateOf(false) }

        val scope = rememberCoroutineScope()
        val states = listOf("All","Kerala", "TN")

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            Text(text = "Find all government schemes")

            Spacer(modifier = Modifier.height(8.dp))

            // 🔹 State Dropdown Filter
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

                                // 🔹 Load all schemes for selected state
                                scope.launch {
                                    val schemes = dao.getSchemesForRegion(selectedState)

                                    result = if (schemes.isNotEmpty()) {
                                        schemes.joinToString("\n\n") {
                                            "${it.name}\n${it.description}\nState: ${it.state}"
                                        }
                                    } else {
                                        "No schemes found for $selectedState"
                                    }
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 🔹 Search Box (Full Width + Search Icon Inside)
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search scheme name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            val schemes = dao.getSchemesForRegion(selectedState)

                            val filtered = if (query.isBlank()) {
                                schemes
                            } else {
                                schemes.filter {
                                    it.name.contains(query, ignoreCase = true)
                                }
                            }

                            result = if (filtered.isNotEmpty()) {
                                filtered.joinToString("\n\n") {
                                    "${it.name}\n${it.description}\nState: ${it.state}"
                                }
                            } else {
                                "No scheme found for $selectedState"
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Result Display
            Text(text = result)
        }

        // 🔹 Load all schemes by default on first launch
        LaunchedEffect(selectedState) {
            val schemes = dao.getSchemesForRegion(selectedState)

            result = if (schemes.isNotEmpty()) {
                schemes.joinToString("\n\n") {
                    "${it.name}\n${it.description}\nState: ${it.state}"
                }
            } else {
                "No schemes found for $selectedState"
            }
        }
    }



