package com.example.appcursos

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class DisponibleActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var sesionesAdapter: SesionesAdapter
    private var isImageShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disponible)

        // Imagen reglamento
        val textView = findViewById<TextView>(R.id.btnlinkCursoAndroid)
        val originalBackground: Drawable? = textView.background
        val imageDrawable: Drawable? = getDrawable(R.drawable.reglamento)

        // Ajustar el tamaño de la imagen (80x80 píxeles)
        val scaledImageDrawable = scaleImage(imageDrawable, 320, 320)

        textView.setOnClickListener {
            if (!isImageShown) {
                // Mostrar la imagen debajo del texto con el tamaño ajustado cuando se hace clic en el texto
                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, scaledImageDrawable)
                isImageShown = true
            } else {
                // Mostrar el fondo original cuando se hace clic nuevamente en el texto
                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                isImageShown = false
            }
        }

        // Obtener los datos del Intent
        val cursoId = intent.getStringExtra("curso_id")

        // Asegurarse de que el cursoId no sea nulo y corresponda al "curso1"
        if (cursoId == "curso1") {
            // Inicializar Firebase Realtime Database
            databaseReference = FirebaseDatabase.getInstance().reference.child("cursos").child("curso1").child("sesiones")

            // Configurar el RecyclerView
            recyclerView = findViewById(R.id.recyclerViewSesiones)
            recyclerView.layoutManager = LinearLayoutManager(this)
            sesionesAdapter = SesionesAdapter()
            recyclerView.adapter = sesionesAdapter

            // Obtener las sesiones del curso1 desde Firebase Realtime Database
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val sesiones = mutableListOf<Sesion>()

                    for (sesionSnapshot in dataSnapshot.children) {
                        val nombreSesion = sesionSnapshot.child("nombre").getValue(String::class.java)
                        val tituloSesion = sesionSnapshot.child("titulo").getValue(String::class.java)
                        val completadaSesion = sesionSnapshot.child("completada").getValue(Boolean::class.java)

                        if (nombreSesion != null && tituloSesion != null && completadaSesion != null) {
                            val sesion = Sesion(nombreSesion, tituloSesion, completadaSesion)
                            sesiones.add(sesion)
                        }
                    }

                    // Mostrar las sesiones en el RecyclerView
                    sesionesAdapter.setSesiones(sesiones)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Manejar el error en caso de que ocurra
                }
            })
        }
    }

    private fun scaleImage(drawable: Drawable?, width: Int, height: Int): Drawable? {
        if (drawable == null) return null

        val bitmap = (drawable as BitmapDrawable).bitmap
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
        return BitmapDrawable(resources, scaledBitmap)
    }
}

data class Sesion(val nombre: String, val titulo: String, var completada: Boolean)
