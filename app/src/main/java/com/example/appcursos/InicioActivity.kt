package com.example.appcursos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics

class InicioActivity : AppCompatActivity() {

    private lateinit var viewFlipper: ViewFlipper
    private lateinit var btnInicioAcceder: Button
    private lateinit var btnInicioRegistrarse: Button


    override fun onCreate(savedInstanceState: Bundle?) {

        Thread.sleep(2000)
        setTheme(R.style.AppTheme)

        //Analytics Event
        val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integración de Firebase completa")
        analytics.logEvent("InitScreen", bundle)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        viewFlipper = findViewById(R.id.viewFlipper)

        // Configurar la animación
        viewFlipper.setInAnimation(this, android.R.anim.fade_in)
        viewFlipper.setOutAnimation(this, android.R.anim.fade_out)

        // Iniciar la animación automática
        viewFlipper.isAutoStart = true
        viewFlipper.flipInterval = 3000 // Intervalo en milisegundos entre cambios de elementos

        // Opcional: Controlar manualmente el cambio de elementos
        viewFlipper.setOnClickListener {
            viewFlipper.showNext()
        }
        //Boton ACCEDER
        btnInicioAcceder = findViewById(R.id.btnInicioAcceder)

        btnInicioAcceder.setOnClickListener {
            val intent = Intent(this, AccederActivity::class.java)
            startActivity(intent)

        }

        //Boton REGISTRARSE
        btnInicioRegistrarse = findViewById(R.id.btnInicioRegistrarse)

        btnInicioRegistrarse.setOnClickListener {
            val intent = Intent(this, RegistrarseActivity::class.java)
            startActivity(intent)

        }
    }
}