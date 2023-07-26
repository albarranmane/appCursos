package com.example.appcursos.ui.cursos

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appcursos.R
import com.example.appcursos.databinding.FragmentCursosBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CursosFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private var _binding: FragmentCursosBinding? = null
    private val binding get() = _binding!!

    private val cursosList = mutableListOf<Curso>() // Agregamos la lista de cursos

    private lateinit var sesionesAdapter: SesionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCursosBinding.inflate(inflater, container, false)
        val view = binding.root

        // Verificar la conexión a Internet antes de cargar los datos
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        val isInternetAvailable = networkInfo != null && networkInfo.isConnected

        if (!isInternetAvailable) {
            Toast.makeText(
                requireContext(),
                "No hay conexión a Internet. Los datos no se pueden cargar.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            // Obtener una referencia a la base de datos
            val cursosRefDatabase: DatabaseReference =
                FirebaseDatabase.getInstance().getReference("cursos")

            // Configurar el RecyclerView con un LayoutManager y el adaptador
            sesionesAdapter = SesionAdapter(cursosList)
            binding.recyclerViewSesiones.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = sesionesAdapter
            }

            // Agregar un Listener a la referencia de la base de datos para obtener los cursos
            cursosRefDatabase.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    cursosList.clear() // Limpiamos la lista antes de agregar nuevos datos
                    for (cursoSnapshot in snapshot.children) {
                        val curso = cursoSnapshot.getValue(Curso::class.java)
                        curso?.let {
                            cursosList.add(it)
                        }
                    }

                    Log.d("CursosFragment", "Número de cursos: ${cursosList.size}")
                    // Notificar al adaptador que los datos han cambiado y deben actualizarse en el RecyclerView
                    sesionesAdapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejar el error en caso de que ocurra
                    Log.e("CursosFragment", "Error al obtener datos: ${error.message}")
                }
            })

        }

        return view
    }

    override fun onResume() {
        super.onResume()

        // Cargar los datos desde Firebase cada vez que el fragmento se reanuda
        loadCursosFromDatabase()
    }

    // Función para cargar los cursos desde la base de datos
    private fun loadCursosFromDatabase() {
        // Aquí implementa la lógica para cargar los datos desde Firebase
        // Puedes seguir el ejemplo que mencioné anteriormente
    }

    private inner class SesionAdapter(var sesionesList: List<Curso>) :
        RecyclerView.Adapter<SesionAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_sesion, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val curso = sesionesList[position]
            holder.textViewDescripcion.text = curso.descripcion
            holder.textViewNombre.text = curso.name

            // Cargamos la imagen del curso en el ImageView utilizando el nombre de recurso
            curso.imagen?.let { imageName ->
                val resourceId = holder.itemView.context.resources.getIdentifier(
                    imageName, "drawable",
                    holder.itemView.context.packageName
                )
                holder.imageViewCurso.setImageResource(resourceId)
            }

            // Mostramos la calificación del curso en el RatingBar
            curso.rating?.let { rating ->
                holder.ratingBarCurso.rating = rating
            }

            // Agregar un listener al RatingBar para detectar cambios en la calificación
            holder.ratingBarCurso.setOnRatingBarChangeListener { _, newRating, _ ->
                // Actualizar la calificación del curso en Firebase
                updateCursoRating(curso, newRating)
            }
            // Agregar un click listener al elemento del RecyclerView para mostrar la disponibilidad del curso
            holder.itemView.setOnClickListener {
                showCursoAvailabilityDialog(curso)
            }
        }

        private fun showCursoAvailabilityDialog(curso: Curso) {
            // Aquí agregamos la lógica para obtener la disponibilidad del curso desde la base de datos
            val cursoRefDatabase: DatabaseReference =
                FirebaseDatabase.getInstance().getReference("cursos").child(curso.id ?: "")

            cursoRefDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val isCursoAvailable =
                        snapshot.child("disponible").getValue(Boolean::class.java) ?: false

                    val message = if (isCursoAvailable) {
                        "Curso Disponible"
                    } else {
                        "Curso No Disponible"
                    }

                    val alertDialog = AlertDialog.Builder(requireContext())
                        .setTitle("Disponibilidad del Curso")
                        .setMessage(message)
                        .setPositiveButton("Aceptar") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()

                    alertDialog.show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(
                        "SesionAdapter",
                        "Error al obtener la disponibilidad del curso: ${error.message}"
                    )
                }
            })
        }

        override fun getItemCount(): Int {
            return sesionesList.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textViewDescripcion: TextView = itemView.findViewById(R.id.textViewDescripcion)
            val textViewNombre: TextView = itemView.findViewById(R.id.textViewNombre)
            val imageViewCurso: ImageView = itemView.findViewById(R.id.imageViewCurso)
            val ratingBarCurso: RatingBar =
                itemView.findViewById(R.id.ratingBarCurso) // Agregamos la declaración de ratingBarCurso
        }
    }

    private fun updateCursoRating(curso: Curso, newRating: Float) {
        // Obtener una referencia a la base de datos del curso que se actualizará
        val cursoRefDatabase: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("cursos").child(curso.id ?: "")

        // Crear un mapa con el campo de calificación y su nuevo valor
        val updateMap = mapOf("rating" to newRating)

        // Actualizar la calificación del curso en la base de datos
        cursoRefDatabase.updateChildren(updateMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("SesionAdapter", "Calificación del curso actualizada exitosamente")
                } else {
                    Log.e(
                        "SesionAdapter",
                        "Error al actualizar la calificación del curso: ${task.exception?.message}"
                    )
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

data class Curso(
    val id: String? = null,
    val descripcion: String? = null,
    val name: String? = null,
    val imagen: String? = null,
    val rating: Float? = null,
    val disponible: Boolean = false // Agregamos el campo para almacenar la disponibilidad del curso
)

