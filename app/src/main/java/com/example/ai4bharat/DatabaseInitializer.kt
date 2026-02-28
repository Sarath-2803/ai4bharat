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
                        category = "Farmer",
                        state = "All",
                        descriptionEn = "₹6000 per year financial support for small and marginal farmers.",
                        descriptionHi = "छोटे और सीमांत किसानों के लिए प्रति वर्ष ₹6000 वित्तीय सहायता।",
                        descriptionMl = "ചെറിയ കര്‍ഷകരും ലഘുവർക്ക് വർഷത്തിൽ ₹6000 ധനസഹായം.",
                        descriptionTa = "சிறிய மற்றும் எடையுள்ள விவசாயிகளுக்கு ஆண்டு ₹6000 நிதி உதவி.",
                        eligibilityEn = "Small and marginal farmers owning cultivable land.",
                        eligibilityHi = "खेती योग्य भूमि रखने वाले छोटे और सीमांत किसान।",
                        eligibilityMl = "ഉത്പാദനത്തിനുള്ള ഭൂമി ഉടമസ്ഥരായ ചെറിയ കര്‍ഷകര്‍.",
                        eligibilityTa = "உயர்தர நிலம் உடைய சிறிய மற்றும் எல்லைக்குட்பட்ட விவசாயிகள்.",
                        benefitsEn = "₹6000 per year paid in three installments.",
                        benefitsHi = "₹6000 प्रति वर्ष तीन किस्तों में भुगतान।",
                        benefitsMl = "വർഷത്തിൽ മൂന്ന് തവണയുള്ള എകൈ ₹6000.",
                        benefitsTa = "ஆண்டு ₹6000 மூன்று தவணைகளில் வழங்கப்படும்.",
                        howToApplyEn = "Apply through local agriculture office or PM Kisan portal.",
                        howToApplyHi = "स्थानीय कृषि कार्यालय या पीएम किसान पोर्टल के माध्यम से आवेदन करें।",
                        howToApplyMl = "പ്രാദേശിക കാർഷിക ഓഫീസിൽ അല്ലെങ്കിൽ PM Kisan പോർട്ടലിൽ അപേക്ഷിക്കാം.",
                        howToApplyTa = "உள்ளூர் விவசாய அலுவலகம் அல்லது PM Kisan போர்டல் மூலம் விண்ணப்பிக்கவும்.",
                        tags = "farmer, agriculture, income support, rural"
                    ),

                    Scheme(
                        name = "Ayushman Bharat",
                        category = "Health",
                        state = "All",
                        descriptionEn = "Health insurance coverage up to ₹5 lakh per family per year.",
                        descriptionHi = "स्वास्थ्य बीमा कवरेज प्रति परिवार प्रति वर्ष ₹5 लाख तक।",
                        descriptionMl = "ഓരോ കുടുംബത്തിനും വർഷത്തിൽ ₹5 ലക്ഷം വരെ ആരോഗ്യ ഇൻഷുറൻസ്.",
                        descriptionTa = "ஒரு குடும்பத்திற்கு வருடத்திற்கு ₹5 லட்சம் வரை சுகாதார காப்பீடு.",
                        eligibilityEn = "Economically vulnerable families identified by SECC data.",
                        eligibilityHi = "आर्थिक रूप से कमजोर परिवार जो SECC डेटा द्वारा पहचाने गए।",
                        eligibilityMl = "ആര്ത്ഥികമായി ബലഹീനമായ കുടുംബങ്ങൾ SECC ഡാറ്റ അനുസരിച്ച്.",
                        eligibilityTa = "பொருளாதாரமாக பாதிக்கப்பட்ட குடும்பங்கள் SECC தரவின் அடிப்படையில்.",
                        benefitsEn = "Cashless health insurance up to ₹5 lakh per year.",
                        benefitsHi = "₹5 लाख तक कैशलेस स्वास्थ्य बीमा।",
                        benefitsMl = "വർഷത്തിൽ ₹5 ലക്ഷം വരെ ക്യാഷ്‌ലസ് ആരോഗ്യ ഇൻഷുറൻസ്.",
                        benefitsTa = "வருடத்திற்கு ₹5 லட்சம் வரை கேஷ்லெஸ் சுகாதார காப்பீடு.",
                        howToApplyEn = "Check eligibility at empaneled hospital or official portal.",
                        howToApplyHi = "एंपेनल अस्पताल या आधिकारिक पोर्टल पर पात्रता जांचें।",
                        howToApplyMl = "എമ്പാനൽ ആശുപത്രിയിൽ അല്ലെങ്കിൽ ഔദ്യോഗിക പോർട്ടലിൽ യോഗ്യത പരിശോധിക്കുക.",
                        howToApplyTa = "எம்பேன்ல் மருத்துவமனை அல்லது அதிகாரப்பூர்வ போர்டலில் தகுதியை சரிபார்க்கவும்.",
                        tags = "health, insurance, hospital, medical, poor"
                    ),

                    Scheme(
                        name = "PM Awas Yojana",
                        category = "Housing",
                        state = "All",
                        descriptionEn = "Affordable housing scheme for urban and rural poor.",
                        descriptionHi = "शहरी और ग्रामीण गरीबों के लिए किफायती आवास योजना।",
                        descriptionMl = "നഗരവും ഗ്രാമവും ദരിദ്രർക്കുള്ള വിലക്കുറഞ്ഞ വീട് പദ്ധതി.",
                        descriptionTa = "நகர்ப்புற மற்றும் கிராமப்புற ஏழைகளுக்கு குறைந்த விலையுள்ள வீட்டு திட்டம்.",
                        eligibilityEn = "Economically weaker sections and low-income families.",
                        eligibilityHi = "आर्थिक रूप से कमजोर वर्ग और कम आय वाले परिवार।",
                        eligibilityMl = "ആര്ത്ഥികമായി ബലഹീന വിഭാഗങ്ങൾ, കുറഞ്ഞ വരുമാന കുടുംബങ്ങൾ.",
                        eligibilityTa = "பொருளாதார ரீதியாக பலவீனமான குடும்பங்கள் மற்றும் குறைந்த வருமானம் கொண்டவர்கள்.",
                        benefitsEn = "Financial assistance for house construction.",
                        benefitsHi = "घर निर्माण के लिए वित्तीय सहायता।",
                        benefitsMl = "വീട് നിർമ്മാണത്തിനുള്ള ധനസഹായം.",
                        benefitsTa = "வீட்டு கட்டுமானத்திற்கு நிதி உதவி.",
                        howToApplyEn = "Apply via state housing department or online portal.",
                        howToApplyHi = "राज्य आवास विभाग या ऑनलाइन पोर्टल के माध्यम से आवेदन करें।",
                        howToApplyMl = "സംസ്ഥാന ഹൗസിംഗ് വകുപ്പ് അല്ലെങ്കിൽ ഓൺലൈൻ പോർട്ടലിലൂടെ അപേക്ഷിക്കുക.",
                        howToApplyTa = "மாநில வீட்டு துறை அல்லது ஆன்லைன் போர்டல் மூலம் விண்ணப்பிக்கவும்.",
                        tags = "housing, home, subsidy, urban, rural"
                    ),

                    Scheme(
                        name = "MGNREGA",
                        category = "Employment",
                        state = "All",
                        descriptionEn = "100 days of guaranteed wage employment for rural households.",
                        descriptionHi = "ग्रामीण परिवारों के लिए 100 दिनों की गारंटीड वेतन रोजगार।",
                        descriptionMl = "ഗ്രാമീണ കുടുംബങ്ങൾക്ക് 100 ദിവസത്തെ വരുമാനം ഉറപ്പുള്ള തൊഴിൽ.",
                        descriptionTa = "கிராமப்புற குடும்பங்களுக்கு 100 நாட்கள் உறுதி செய்யப்பட்ட சம்பள வேலை.",
                        eligibilityEn = "Adult members of rural households willing to do unskilled labor.",
                        eligibilityHi = "अशिक्षित श्रम करने के इच्छुक ग्रामीण परिवार के वयस्क सदस्य।",
                        eligibilityMl = "അനനുഭവ തൊഴിലാളി ചെയ്യാൻ തയ്യാറായ ഗ്രാമീണ കുടുംബ അംഗങ്ങൾ.",
                        eligibilityTa = "திறமையற்ற வேலை செய்ய விரும்பும் கிராமப்புற குடும்ப உறுப்பினர்கள்.",
                        benefitsEn = "100 days guaranteed wage employment.",
                        benefitsHi = "100 दिनों की गारंटीड वेतन रोजगार।",
                        benefitsMl = "100 ദിവസത്തെ ഉറപ്പുള്ള വേതനം.",
                        benefitsTa = "100 நாட்கள் உறுதி செய்யப்பட்ட சம்பள வேலை.",
                        howToApplyEn = "Register at Gram Panchayat office.",
                        howToApplyHi = "ग्राम पंचायत कार्यालय में पंजीकरण करें।",
                        howToApplyMl = "ഗ്രാം പഞ്ചായത്ത് ഓഫീസിൽ രജിസ്റ്റർ ചെയ്യുക.",
                        howToApplyTa = "கிராம பஞ்சாயத்து அலுவலகத்தில் பதிவு செய்யவும்.",
                        tags = "employment, rural, job, wages, labor"
                    ),

                    Scheme(
                        name = "PM Jan Dhan Yojana",
                        category = "Finance",
                        state = "All",
                        descriptionEn = "Zero balance bank account for financial inclusion.",
                        descriptionHi = "वित्तीय समावेशन के लिए शून्य शेष बैंक खाता।",
                        descriptionMl = "ആർത്ഥിക ഉൾപ്പെടുത്തലിനായി സീറോ ബാലൻസ് ബാങ്ക് അക്കൗണ്ട്.",
                        descriptionTa = "நிதி சேர்க்கைக்கான பூஜ்ய இருப்பு வங்கி கணக்கு.",
                        eligibilityEn = "Any Indian citizen without a bank account.",
                        eligibilityHi = "किसी भी भारतीय नागरिक के पास बैंक खाता न हो।",
                        eligibilityMl = "ബാങ്ക് അക്കൗണ്ട് ഇല്ലാത്ത ഏത് ഭാരതീയനും.",
                        eligibilityTa = "வங்கிக் கணக்கு இல்லாத எந்த இந்திய குடிமகனும்.",
                        benefitsEn = "Zero balance account, RuPay card, insurance cover.",
                        benefitsHi = "शून्य शेष खाता, रूपे कार्ड, बीमा कवर।",
                        benefitsMl = "സീറോ ബാലൻസ് അക്കൗണ്ട്, RuPay കാർഡ്, ഇൻഷുറൻസ്.",
                        benefitsTa = "பூஜ்ய இருப்பு கணக்கு, RuPay கார்டு, காப்பீடு.",
                        howToApplyEn = "Visit nearest bank with Aadhaar.",
                        howToApplyHi = "नज़दीकी बैंक जाएँ और आधार दिखाएँ।",
                        howToApplyMl = "ഏറ്റവും അടുത്ത ബാങ്കിൽ അടാർ കാണിക്കുക.",
                        howToApplyTa = "அடுத்த வங்கிக்கு செல்லுங்கள் மற்றும் ஆதார் காட்டவும்.",
                        tags = "bank, finance, account, insurance, poor"
                    )

                    // Add other schemes here similarly...
                )

                schemes.forEach { dao.insertScheme(it) }
            }
        }
    }
}