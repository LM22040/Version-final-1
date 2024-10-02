package com.example.whatsapptranslate

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.os.Environment

class PermissionSettingsActivity : AppCompatActivity() {

    private val RECORD_AUDIO_PERMISSION_CODE = 1
    private val STORAGE_PERMISSION_CODE = 2
    private val NOTIFICATION_PERMISSION_CODE = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_settings)

        val switchMicrophone: Switch = findViewById(R.id.switch_microphone)
        val switchStorage: Switch = findViewById(R.id.switch_storage)
        val switchNotifications: Switch = findViewById(R.id.switch_notifications)
        val btnAcceptPermissions: Button = findViewById(R.id.btn_accept_permissions)
        val tvPermissionInfo: TextView = findViewById(R.id.tv_permission_info)

        switchMicrophone.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                requestMicrophonePermission()
            }
        }

        switchStorage.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                requestStoragePermission()
            }
        }

        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                requestNotificationPermission()
            }
        }

        btnAcceptPermissions.setOnClickListener {
            if (allPermissionsGranted()) {
                openActivationScreen()
            } else {
                // Mostrar un mensaje al usuario indicando que debe conceder todos los permisos
            }
        }
    }

    private fun requestMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), RECORD_AUDIO_PERMISSION_CODE)
        }
    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Para Android 11 y superior, usamos MANAGE_EXTERNAL_STORAGE
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        } else {
            // Para versiones anteriores a Android 11
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), NOTIFICATION_PERMISSION_CODE)
            }
        } else {
            // Para versiones anteriores a Android 13, las notificaciones no requieren un permiso específico
        }
    }

    private fun allPermissionsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
               (Build.VERSION.SDK_INT < Build.VERSION_CODES.R || Environment.isExternalStorageManager()) &&
               (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)
    }

    private fun openActivationScreen() {
        val intent = Intent(this, ActivationActivity::class.java)
        startActivity(intent)
        finish() // Cierra esta actividad para que el usuario no pueda volver atrás
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RECORD_AUDIO_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso concedido
                } else {
                    // Permiso denegado
                    findViewById<Switch>(R.id.switch_microphone).isChecked = false
                }
            }
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso concedido
                } else {
                    // Permiso denegado
                    findViewById<Switch>(R.id.switch_storage).isChecked = false
                }
            }
            NOTIFICATION_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso concedido
                } else {
                    // Permiso denegado
                    findViewById<Switch>(R.id.switch_notifications).isChecked = false
                }
            }
        }
    }
}
