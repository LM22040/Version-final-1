package com.example.whatsapptranslate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Aquí puedes iniciar las otras actividades o realizar cualquier lógica necesaria
        // Por ejemplo, para iniciar la actividad LanguageSelectionActivity:
        startActivity(Intent(this, LanguageSelectionActivity::class.java))
    }
}