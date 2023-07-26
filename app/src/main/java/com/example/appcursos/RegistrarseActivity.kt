package com.example.appcursos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrarseActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)

        val txtRegistrarseNombre: TextView = findViewById(R.id.edtRegistrarseNombre)
        val txtRegistrarseCorreo: TextView = findViewById(R.id.edtRegistrarseCorreo)
        val txtRegistrarsePassword: TextView = findViewById(R.id.edtRegistrarsePassword)
        val txtRegistrarseConfirmarPassword: TextView = findViewById(R.id.edtRegistrarseConfirmarPassword)
        val btnRegistrarse: Button = findViewById(R.id.btnRegistrarse)
        val btnRegistrarseAcceder: TextView = findViewById(R.id.btnRegistrarseAcceder)

        firebaseAuth = Firebase.auth

        btnRegistrarse.setOnClickListener {
            val nombre = txtRegistrarseNombre.text.toString().trim()
            val correo = txtRegistrarseCorreo.text.toString().trim()
            val password = txtRegistrarsePassword.text.toString()
            val confirmarPassword = txtRegistrarseConfirmarPassword.text.toString()

            // Validar que los campos no estén vacíos
            if (nombre.isEmpty() || correo.isEmpty() || password.isEmpty() || confirmarPassword.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar que las contraseñas coincidan
            if (password != confirmarPassword) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                txtRegistrarsePassword.requestFocus()
                return@setOnClickListener
            }

            // Crear la cuenta con Firebase Authentication
            createAccount(nombre, correo, password)
        }

        // Link Acceder
        btnRegistrarseAcceder.setOnClickListener {
            val intent = Intent(this, AccederActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createAccount(nombre: String, correo: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(correo, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Enviamos un correo de verificación al usuario registrado
                    val user = firebaseAuth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { verificationTask ->
                            if (verificationTask.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Cuenta creada. Requiere verificación del correo electrónico.",
                                    Toast.LENGTH_LONG
                                ).show()
                                // Limpiar los campos de registro
                                clearRegistrationFields()
                            } else {
                                // Ocurrió un error al enviar el correo de verificación
                                Toast.makeText(
                                    this,
                                    "Error al enviar el correo de verificación",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    // Ocurrió un error al crear la cuenta
                    Toast.makeText(
                        this,
                        "Error al crear la cuenta: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun clearRegistrationFields() {
        val txtRegistrarseNombre: TextView = findViewById(R.id.edtRegistrarseNombre)
        val txtRegistrarseCorreo: TextView = findViewById(R.id.edtRegistrarseCorreo)
        val txtRegistrarsePassword: TextView = findViewById(R.id.edtRegistrarsePassword)
        val txtRegistrarseConfirmarPassword: TextView = findViewById(R.id.edtRegistrarseConfirmarPassword)

        txtRegistrarseNombre.text = ""
        txtRegistrarseCorreo.text = ""
        txtRegistrarsePassword.text = ""
        txtRegistrarseConfirmarPassword.text = ""
    }
}