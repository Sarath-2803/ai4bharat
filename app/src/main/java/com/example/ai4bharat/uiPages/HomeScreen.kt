package com.example.ai4bharat.uiPages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.rememberAsyncImagePainter
import com.example.ai4bharat.Language
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.speech.tts.TextToSpeech
import android.widget.Toast
import java.util.*

/** -------------------------------
 * Data model for news/announcements
 * ------------------------------- */
data class NewsItem(
    val title: String,
    val description: String,
    val url: String = "",
    val source: String = "",
    val publishedAt: String = "",
    val imageUrl: String = ""
)

/** -------------------------------
 * ViewModel to load news
 * ------------------------------- */
class HomeViewModel : ViewModel() {

    var announcements by mutableStateOf<List<NewsItem>>(emptyList())
        private set

    var nationalNews by mutableStateOf<List<NewsItem>>(emptyList())
        private set

    var localNews by mutableStateOf<List<NewsItem>>(emptyList())
        private set

    /** Load news (mock API calls for now) */
    fun loadNews(state: String) {
        viewModelScope.launch {

            // 🔹 Mock delay
            delay(1000)

            // 🔹 Mock Announcements
            announcements = listOf(
                NewsItem(
                    title = "PM Kisan Scheme Extended",
                    description = "Government announces extension of PM Kisan scheme for 2026",
                    source = "PIB",
                    publishedAt = "2026-03-03"
                ),
                NewsItem(
                    title = "Digital India Week",
                    description = "Digital India Week to promote e-governance across India",
                    source = "MyGov",
                    publishedAt = "2026-03-01"
                )
            )

            // 🔹 Mock National News
            nationalNews = listOf(
                NewsItem(
                    title = "India Launches Satellite",
                    description = "New earth observation satellite launched successfully",
                    source = "The Hindu",
                    publishedAt = "2026-03-02",
                    imageUrl = "https://via.placeholder.com/150"
                ),
                NewsItem(
                    title = "Budget 2026 Announced",
                    description = "Finance Ministry announces the union budget for 2026",
                    source = "The Indian Express",
                    publishedAt = "2026-03-01",
                    imageUrl = "https://via.placeholder.com/150"
                )
            )

            // 🔹 Mock Local News
            localNews = when(state) {
                "Kerala" -> listOf(
                    NewsItem(
                        title = "Kerala Tourism Festival",
                        description = "Kerala government announces tourism festival 2026",
                        source = "Kerala Govt",
                        publishedAt = "2026-03-01"
                    ),
                    NewsItem(
                        title = "Flood Preparedness Campaign",
                        description = "State authorities start awareness drive for monsoon floods",
                        source = "Kerala Govt",
                        publishedAt = "2026-03-02"
                    )
                )
                "TN" -> listOf(
                    NewsItem(
                        title = "Chennai Smart City Project",
                        description = "Tamil Nadu government inaugurates smart city initiatives",
                        source = "TN Govt",
                        publishedAt = "2026-03-01"
                    )
                )
                else -> emptyList()
            }
        }
    }
}

/** -------------------------------
 * HomeScreen Composable
 * ------------------------------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    selectedLanguage: Language,
    selectedState: String
) {
    val context = LocalContext.current

    // TTS
    var ttsReady by remember { mutableStateOf(false) }
    val tts = remember {
        TextToSpeech(context) { status ->
            ttsReady = status == TextToSpeech.SUCCESS
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    // Load news once
    LaunchedEffect(selectedState) {
        viewModel.loadNews(selectedState)
    }

    // Helper for multilingual text (basic example)
    fun localized(titleEn: String, titleHi: String, titleMl: String, titleTa: String): String {
        return when(selectedLanguage) {
            Language.HI -> titleHi
            Language.ML -> titleMl
            Language.TA -> titleTa
            else -> titleEn
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        /** -------------------------------
         * Official Announcements Section
         * ------------------------------- */
        item {
            Text(
                text = localized("Official Announcements","सरकारी घोषणाएँ","അധികൃത പ്രഖ്യാപനങ്ങൾ","அதிகாரி அறிவிப்புகள்"),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(viewModel.announcements) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(item.title, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(item.description)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(item.source, style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(item.publishedAt, style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = {
                            if(ttsReady) {
                                tts.language = Locale("en")
                                tts.speak(item.title + ". " + item.description, TextToSpeech.QUEUE_FLUSH, null, null)
                            }
                        }) {
                            Text("🔊")
                        }
                    }
                }
            }
        }

        /** -------------------------------
         * National News Section
         * ------------------------------- */
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = localized("National News","राष्ट्रीय समाचार","ദേശീയ വാർത്തകൾ","தேசிய செய்திகள்"),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            LazyRow {
                items(viewModel.nationalNews) { item ->
                    Card(
                        modifier = Modifier
                            .width(250.dp)
                            .padding(end = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            if(item.imageUrl.isNotEmpty()) {
                                Image(
                                    painter = rememberAsyncImagePainter(item.imageUrl),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                            Text(item.title, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(item.source, style = MaterialTheme.typography.labelSmall)
                            Spacer(modifier = Modifier.height(4.dp))
                            IconButton(onClick = {
                                if(ttsReady) {
                                    tts.language = Locale("en")
                                    tts.speak(item.title + ". " + item.description, TextToSpeech.QUEUE_FLUSH, null, null)
                                }
                            }) {
                                Text("🔊")
                            }
                        }
                    }
                }
            }
        }

        /** -------------------------------
         * Local/State News Section
         * ------------------------------- */
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = localized("Local News","स्थानीय समाचार","പ്രാദേശിക വാർത്തകൾ","உள்ளூர் செய்திகள்"),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(viewModel.localNews) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(item.title, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(item.description)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(item.source, style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(item.publishedAt, style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = {
                            if(ttsReady) {
                                tts.language = Locale("en")
                                tts.speak(item.title + ". " + item.description, TextToSpeech.QUEUE_FLUSH, null, null)
                            }
                        }) {
                            Text("🔊")
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}