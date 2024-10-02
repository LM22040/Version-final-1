package com.example.whatsapptranslate

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ActivationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activation)

        val tvSelectedLanguage: TextView = findViewById(R.id.tv_selected_language)
        val btnOpenWhatsApp: Button = findViewById(R.id.btn_open_whatsapp)

        // Obtener el idioma seleccionado de las preferencias compartidas
        val sharedPreferences = getSharedPreferences("WhatsAppTranslatePrefs", MODE_PRIVATE)
        val selectedLanguage = sharedPreferences.getString("selected_language", "Español") ?: "Español"

        tvSelectedLanguage.text = "Idioma seleccionado: $selectedLanguage"

        btnOpenWhatsApp.setOnClickListener {
            openWhatsApp()
        }
    }

    private fun openWhatsApp() {
        val whatsappPackageName = "com.whatsapp"
        try {
            val intent = packageManager.getLaunchIntentForPackage(whatsappPackageName)
            if (intent != null) {
                startActivity(intent)
            } else {
                // WhatsApp no está instalado, redirigir a la Play Store
                goToPlayStore(whatsappPackageName)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            // WhatsApp no está instalado, redirigir a la Play Store
            goToPlayStore(whatsappPackageName)
        }
    }

    private fun goToPlayStore(packageName: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (e: android.content.ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
        }
    }
}
