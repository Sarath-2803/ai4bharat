package com.example.ai4bharat.uiPages

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.ai4bharat.Language
import androidx.compose.foundation.lazy.items

// 🔹 Emergency info model
data class EmergencyInfo(
    val titleEn: String,
    val titleHi: String,
    val titleMl: String,
    val titleTa: String,
    val instructionsEn: String,
    val instructionsHi: String,
    val instructionsMl: String,
    val instructionsTa: String
)

// 🔹 Sample emergency tutorials
val emergencyList = listOf(
    EmergencyInfo(
        titleEn = "CPR (Cardiopulmonary Resuscitation)",
        titleHi = "सीपीआर (कार्डियोपल्मोनरी रिससिटेशन)",
        titleMl = "സിപിആർ (കാർഡിയോപൾമണറി റിസസിറ്റേഷൻ)",
        titleTa = "சி.பி.ஆர் (கார்டியோபல்மனரி ரிசசிடேஷன்)",
        instructionsEn = "1. Check responsiveness\n2. Call emergency services\n3. Start chest compressions and rescue breaths\n4. Continue until help arrives",
        instructionsHi = "1. प्रतिक्रिया जांचें\n2. आपातकालीन सेवाओं को बुलाएँ\n3. छाती संपीड़न और साँसें दें\n4. मदद आने तक जारी रखें",
        instructionsMl = "1. പ്രതികരണം പരിശോധിക്കുക\n2. അടിയന്തര സേവനങ്ങളെ വിളിക്കുക\n3. നെഞ്ച് കംപ്രഷനുകളും ശ്വാസം നൽകുക\n4. സഹായം വരുന്നത് വരെ തുടരുക",
        instructionsTa = "1. பதில்வைச் சரிபார்க்கவும்\n2. அவசர சேவைகளை அழைக்கவும்\n3. மார்பு மசாஜ் மற்றும் சுவாசம் கொடுக்கவும்\n4. உதவி வரும்வரை தொடரவும்"
    ),
    EmergencyInfo(
        titleEn = "First Aid for Cuts",
        titleHi = "काटने पर प्राथमिक उपचार",
        titleMl = "കഷണങ്ങൾക്ക് പ്രാഥമിക ചികിത്സ",
        titleTa = "கத்தலுக்கு முதல் உதவி",
        instructionsEn = "1. Clean the wound\n2. Apply antiseptic\n3. Cover with bandage\n4. Seek medical help if needed",
        instructionsHi = "1. घाव साफ करें\n2. एंटीसेप्टिक लगाएँ\n3. पट्टी बांधें\n4. आवश्यकता होने पर चिकित्सकीय सहायता लें",
        instructionsMl = "1. മുറിവ് കഴുകുക\n2. ആന്റിസെപ്റ്റിക് ഉപയോഗിക്കുക\n3. ബാൻഡേജ് പൂട്ടുക\n4. ആവശ്യമായാൽ മെഡിക്കൽ സഹായം തേടുക",
        instructionsTa = "1. காயத்தை சுத்தமாக்கவும்\n2. அன்டிசெப்டிக் பயன்படுத்தவும்\n3. பட்டி பூட்டவும்\n4. தேவையானால் மருத்துவ உதவி பெறவும்"
    ),
    EmergencyInfo(
        titleEn = "Drowning",
        titleHi = "डूबने पर",
        titleMl = "മുഴുകലിൽ",
        titleTa = "மூழ்கும் போது",
        instructionsEn = "1. Call for help immediately\n2. Do not jump in recklessly\n3. Use a flotation device\n4. Perform CPR if needed",
        instructionsHi = "1. तुरंत मदद बुलाएँ\n2. लापरवाही से कूदें नहीं\n3. तैरने का साधन उपयोग करें\n4. जरूरत पड़ने पर CPR करें",
        instructionsMl = "1. ഉടൻ സഹായം വിളിക്കുക\n2. അശ്രദ്ധയായി ചാടരുത്\n3. ഫ്ലോട്ടേഷൻ ഉപകരണം ഉപയോഗിക്കുക\n4. ആവശ്യമായാൽ CPR നടത്തുക",
        instructionsTa = "1. உடனே உதவி அழைக்கவும்\n2. சீரழிவாக குதிக்க வேண்டாம்\n3. பாய்ந்த சுட்டியை பயன்படுத்தவும்\n4. தேவையானால் CPR செய்யவும்"
    )
)

// 🔹 Emergency helplines
val helplines = mapOf(
    "Police" to "100",
    "Fire" to "101",
    "Ambulance" to "102",
    "Women Helpline" to "1091"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyScreen(language: Language) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 🔹 Tutorials
        items(emergencyList) { info ->
            var expanded by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { expanded = !expanded },
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = when(language) {
                            Language.HI -> info.titleHi
                            Language.ML -> info.titleMl
                            Language.TA -> info.titleTa
                            else -> info.titleEn
                        },
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (expanded) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = when(language) {
                                Language.HI -> info.instructionsHi
                                Language.ML -> info.instructionsMl
                                Language.TA -> info.instructionsTa
                                else -> info.instructionsEn
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        // 🔹 Emergency Helpline buttons
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = when(language) {
                    Language.EN -> "Call Emergency Helpline"
                    Language.HI -> "आपातकालीन हेल्पलाइन कॉल करें"
                    Language.ML -> "അപകടഹെൽപ്ലൈൻ വിളിക്കുക"
                    Language.TA -> "அவசர உதவி அழைப்பு செய்யவும்"
                },
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))

            helplines.forEach { (name, number) ->
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:$number")
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text("$name : $number")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}