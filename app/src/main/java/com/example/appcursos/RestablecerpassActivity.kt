package com.example.appcursos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RestablecerpassActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restablecerpass)

        val txtRestablecerPass: EditText = findViewById(R.id.edtRestablecerPass)
        val btnRestablecerPass: Button = findViewById(R.id.btnRestablecerPass)
        val btnLinkAcceder: TextView = findViewById(R.id.btnLinkAcceder)

        firebaseAuth = Firebase.auth

        // Botón para restablecer la contraseña
        btnRestablecerPass.setOnClickListener {
            val email = txtRestablecerPass.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Ingresa tu dirección de correo electrónico", Toast.LENGTH_SHORT).show()
            } else {
                sendPasswordReset(email)
            }
        }

        // Botón para acceder (enlace)
        btnLinkAcceder.setOnClickListener {
            val intent = Intent(this, AccederActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun sendPasswordReset(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Se ha enviado un correo para restablecer la contraseña", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "No se pudo enviar el correo para restablecer la contraseña", Toast.LENGTH_SHORT).show()
                }
            }
    }
}