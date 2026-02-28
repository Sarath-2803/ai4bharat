package com.example.ai4bharat.uiPages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai4bharat.Language
import com.example.ai4bharat.Scheme
import com.example.ai4bharat.SchemeDao
import kotlinx.coroutines.launch

// 🔹 Chat message model
// 🔹 Chat message model
data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

// 🔹 Chat ViewModel
class ChatViewModel(private val dao: SchemeDao) : ViewModel() {

    var messages = mutableStateListOf<ChatMessage>()
        private set

    var currentScheme by mutableStateOf<Scheme?>(null)
        private set

    var selectedLanguage by mutableStateOf(Language.EN)
        private set

    var remainingSuggestions = mutableStateListOf<String>()
        private set

    fun setLanguage(language: Language) {
        selectedLanguage = language

        if (messages.isEmpty()) {
            val welcome = when (selectedLanguage) {
                Language.HI -> "किसी योजना का नाम दर्ज करें। उदाहरण: PM Kisan"
                Language.ML -> "ഒരു പദ്ധതിയുടെ പേര് നൽകുക. ഉദാഹരണം: PM Kisan"
                Language.TA -> "ஒரு திட்டத்தின் பெயரை உள்ளிடவும். உதாரணம்: PM Kisan"
                else -> "Enter a scheme name. Example: PM Kisan"
            }
            messages.add(ChatMessage(welcome, false))
        }
    }

    fun sendUserMessage(text: String) {
        if (text.isBlank()) return

        messages.add(ChatMessage(text, true))

        viewModelScope.launch {
            val reply = handleUserMessage(text)
            messages.add(reply)
        }
    }

    private suspend fun handleUserMessage(text: String): ChatMessage {

        // 🔹 If user clicked suggestion
        if (currentScheme != null && remainingSuggestions.contains(text)) {

            val response = when (selectedLanguage) {
                Language.HI -> when (text) {
                    "पात्रता" -> currentScheme!!.eligibilityHi
                    "लाभ" -> currentScheme!!.benefitsHi
                    "आवेदन कैसे करें" -> currentScheme!!.howToApplyHi
                    else -> ""
                }
                Language.ML -> when (text) {
                    "യോഗ്യത" -> currentScheme!!.eligibilityMl
                    "ലാഭങ്ങൾ" -> currentScheme!!.benefitsMl
                    "അപേക്ഷ എങ്ങനെ ചെയ്യാം" -> currentScheme!!.howToApplyMl
                    else -> ""
                }
                Language.TA -> when (text) {
                    "தகுதி" -> currentScheme!!.eligibilityTa
                    "நன்மைகள்" -> currentScheme!!.benefitsTa
                    "எப்படி விண்ணப்பிக்கலாம்" -> currentScheme!!.howToApplyTa
                    else -> ""
                }
                else -> when (text) {
                    "Eligibility" -> currentScheme!!.eligibilityEn
                    "Benefits" -> currentScheme!!.benefitsEn
                    "How to apply" -> currentScheme!!.howToApplyEn
                    else -> ""
                }
            }

            remainingSuggestions.remove(text)

            // If no suggestions left → show other schemes
            if (remainingSuggestions.isEmpty()) {

                val otherSchemes = dao.getAllSchemes()
                    .filter { it.id != currentScheme!!.id }
                    .take(3)

                currentScheme = null

                val otherText = when (selectedLanguage) {
                    Language.HI -> "आप अन्य योजनाएँ भी देख सकते हैं:\n\n"
                    Language.ML -> "നിങ്ങൾക്ക് മറ്റ് പദ്ധതികളും കാണാം:\n\n"
                    Language.TA -> "நீங்கள் மற்ற திட்டங்களையும் பார்க்கலாம்:\n\n"
                    else -> "You can also check these schemes:\n\n"
                }

                return ChatMessage(
                    response + "\n\n" +
                            otherText +
                            otherSchemes.joinToString("\n") { "• ${it.name}" },
                    false
                )
            }

            return ChatMessage(response, false)
        }

        // 🔹 Otherwise treat input as scheme name
        val scheme = findBestMatchingScheme(text, dao)

        if (scheme == null) {
            val guide = when (selectedLanguage) {
                Language.HI -> "कृपया किसी योजना का सही नाम दर्ज करें। उदाहरण: PM Kisan"
                Language.ML -> "ദയവായി ഒരു പദ്ധതിയുടെ പേര് നൽകുക. ഉദാഹരണം: PM Kisan"
                Language.TA -> "தயவுசெய்து சரியான திட்டத்தின் பெயரை உள்ளிடவும். உதாரணம்: PM Kisan"
                else -> "Please enter a valid scheme name. Example: PM Kisan"
            }
            return ChatMessage(guide, false)
        }

        currentScheme = scheme

        remainingSuggestions.clear()
        remainingSuggestions.addAll(
            when (selectedLanguage) {
                Language.HI -> listOf("पात्रता", "लाभ", "आवेदन कैसे करें")
                Language.ML -> listOf("യോഗ്യത", "ലാഭങ്ങൾ", "അപേക്ഷ എങ്ങനെ ചെയ്യാം")
                Language.TA -> listOf("தகுதி", "நன்மைகள்", "எப்படி விண்ணப்பிக்கலாம்")
                else -> listOf("Eligibility", "Benefits", "How to apply")
            }
        )

        val description = when (selectedLanguage) {
            Language.HI -> scheme.descriptionHi
            Language.ML -> scheme.descriptionMl
            Language.TA -> scheme.descriptionTa
            else -> scheme.descriptionEn
        }

        return ChatMessage(description, false)
    }
}

// 🔹 Chat UI with language selector
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBotScreen(
    viewModel: ChatViewModel,
    selectedLanguage: Language
) {
    var userInput by remember { mutableStateOf("") }

    LaunchedEffect(selectedLanguage) {
        viewModel.setLanguage(selectedLanguage)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(viewModel.messages) { index, msg ->

                Column {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (msg.isUser)
                            Arrangement.End
                        else Arrangement.Start
                    ) {
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            color = if (msg.isUser)
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant,
                            tonalElevation = 4.dp,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text(
                                text = msg.text,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }

                    val isLast = index == viewModel.messages.lastIndex
                    val isBot = !msg.isUser

                    if (isLast && isBot && viewModel.remainingSuggestions.isNotEmpty()) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, bottom = 8.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            viewModel.remainingSuggestions.forEach { suggestion ->
                                AssistChip(
                                    onClick = {
                                        viewModel.sendUserMessage(suggestion)
                                    },
                                    label = { Text(suggestion) },
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = userInput,
                onValueChange = { userInput = it },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        when (selectedLanguage) {
                            Language.HI -> "योजना का नाम लिखें..."
                            Language.ML -> "പദ്ധതിയുടെ പേര് എഴുതുക..."
                            Language.TA -> "திட்டத்தின் பெயரை எழுதவும்..."
                            else -> "Type scheme name..."
                        }
                    )
                }
            )

            IconButton(onClick = {
                viewModel.sendUserMessage(userInput)
                userInput = ""
            }) {
                Icon(Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}
// 🔹 Scheme detection
suspend fun findBestMatchingScheme(query: String, dao: SchemeDao): Scheme? {
    val schemes = dao.getAllSchemes()
    val queryWords = query.lowercase().split(Regex("\\W+")).filter { it.isNotBlank() }

    fun schemeNameScore(schemeName: String): Int {
        val schemeWords = schemeName.lowercase().split(Regex("\\W+")).filter { it.isNotBlank() }
        return schemeWords.count { queryWords.contains(it) }
    }

    val scored = schemes.map { scheme ->
        var score = 0
        score += schemeNameScore(scheme.name) * 3
        if (queryWords.contains(scheme.category.lowercase())) score += 2
        if (scheme.state.lowercase() != "all" && queryWords.contains(scheme.state.lowercase())) score += 2
        scheme.tags.split(",").forEach { if (queryWords.contains(it.trim().lowercase())) score += 1 }
        scheme to score
    }

    val best = scored.maxByOrNull { it.second }
    return if (best != null && best.second >= 3) best.first else null
}

// 🔹 Generate multilingual response
fun generateResponse(query: String, scheme: Scheme?, language: Language): String {
    if (scheme == null) return ""

    fun field(en: String, hi: String, ml: String, ta: String) = when(language) {
        Language.EN -> en
        Language.HI -> hi
        Language.ML -> ml
        Language.TA -> ta
    }

    val lower = query.lowercase()
    return when {
        lower.contains("eligibility") -> field(scheme.eligibilityEn, scheme.eligibilityHi, scheme.eligibilityMl, scheme.eligibilityTa)
        lower.contains("benefit") -> field(scheme.benefitsEn, scheme.benefitsHi, scheme.benefitsMl, scheme.benefitsTa)
        lower.contains("apply") -> field(scheme.howToApplyEn, scheme.howToApplyHi, scheme.howToApplyMl, scheme.howToApplyTa)
        lower.contains("what") || lower.contains("about") ->
            field(scheme.descriptionEn, scheme.descriptionHi, scheme.descriptionMl, scheme.descriptionTa) +
                    "\n\nYou can ask about:\n• Eligibility\n• Benefits\n• How to apply"
        else -> "You are asking about ${scheme.name}.\nTry asking:\n• What is eligibility?\n• What are benefits?\n• How to apply?"
    }
}

// 🔹 Find all schemes in a state
suspend fun findSchemesByState(query: String, dao: SchemeDao): List<Scheme> {
    val schemes = dao.getAllSchemes()
    val lower = query.lowercase()
    val detectedState = schemes.map { it.state }.distinct().firstOrNull {
        it.lowercase() != "all" && lower.contains(it.lowercase())
    }
    return if (detectedState != null) schemes.filter { it.state.equals(detectedState, true) } else emptyList()
}