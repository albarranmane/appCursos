package com.example.appcursos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AccederActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acceder)

        FirebaseApp.initializeApp(this)
        firebaseAuth = Firebase.auth

        val txtemail: TextView = findViewById(R.id.edtAccederCorreo)
        val txtpass: TextView = findViewById(R.id.edtAccederPassword)
        val btnacceder: Button = findViewById(R.id.btnAcceder)
        val btnaccederegistrarse: TextView = findViewById(R.id.btnAccederRegistrarse)
        val btnrestablecerpass: TextView = findViewById(R.id.btnAccederRestablecerContraseña)

        // Botón Acceder
        btnacceder.setOnClickListener {
            val email = txtemail.text.toString().trim()
            val password = txtpass.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Ingresa tu correo y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                signIn(email, password)
            }
        }

        // Boton Registrarse
        btnaccederegistrarse.setOnClickListener {
            val i = Intent(this, RegistrarseActivity::class.java)
            startActivity(i)
        }

        // Boton Restablecer password
        btnrestablecerpass.setOnClickListener {
            val i = Intent(this, RestablecerpassActivity::class.java)
            startActivity(i)
        }
    }

    private fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user != null && user.isEmailVerified) {
                        // Autenticación exitosa y correo verificado
                        Toast.makeText(this, "Autenticación Exitosa", Toast.LENGTH_SHORT).show()
                        val i = Intent(this, MainActivity::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        // Usuario no existe, el correo no ha sido verificado o la autenticación falló
                        Toast.makeText(this, "Cuenta no existe o correo no verificado", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Error de autenticación
                    Toast.makeText(this, "Error de email o contraseña", Toast.LENGTH_SHORT).show()
                }
            }
    }
}