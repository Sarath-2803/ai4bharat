package com.example.ai4bharat

import com.example.ai4bharat.uiPages.SchemeScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.lifecycle.lifecycleScope
import com.example.ai4bharat.ui.theme.Ai4BharatTheme
import com.example.ai4bharat.uiPages.Ai4BharatApp
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao: SchemeDao? = try {
            AppDatabase.getDatabase(this).schemeDao()
        } catch (e: Exception) {
            null
        }


        if (dao != null) {
            // Insert dummy schemes when app starts
            lifecycleScope.launch {
                DatabaseInitializer.populateIfEmpty(dao)
            }
        }

        setContent {
            if (dao != null) {
                Ai4BharatTheme {
                    Ai4BharatApp(dao)
                }

            }
        }

//        if (checkSelfPermission(android.Manifest.permission.RECORD_AUDIO)
//            != android.content.pm.PackageManager.PERMISSION_GRANTED
//        ) {
//            requestPermissions(
//                arrayOf(android.Manifest.permission.RECORD_AUDIO),
//                1
//            )
//        }
//    }
    }
}




