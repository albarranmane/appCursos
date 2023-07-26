package com.example.appcursos.ui.miscursos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appcursos.DisponibleActivity
import com.example.appcursos.databinding.FragmentMiscursosBinding
import com.example.appcursos.ui.buscar.CursosAdapter
import com.example.appcursos.ui.cursos.Curso
import com.google.firebase.database.*


class MiscursosFragment : Fragment(), CursosAdapter.OnItemClickListener {

    private var _binding: FragmentMiscursosBinding? = null
    private val binding get() = _binding!!

    private lateinit var databaseReference: DatabaseReference

    private lateinit var cursosAdapter: CursosAdapter
    private val cursosList = mutableListOf<Curso>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMiscursosBinding.inflate(inflater, container, false)
        val miscursosViewModel =
            ViewModelProvider(this).get(MiscursosViewModel::class.java)

        _binding = FragmentMiscursosBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // Obtener una referencia a la base de datos de cursos
        databaseReference = FirebaseDatabase.getInstance().getReference("cursos")

        // Configurar el RecyclerView con un LayoutManager y el adaptador
        cursosAdapter = CursosAdapter(cursosList)
        cursosAdapter.setOnItemClickListener(this) // Establecer el listener en el adaptador
        binding.recyclerViewCursos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cursosAdapter
        }

        // Agregar un Listener a la referencia de la base de datos para obtener los cursos disponibles
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cursosList.clear() // Limpiamos la lista antes de agregar nuevos datos
                for (cursoSnapshot in snapshot.children) {
                    val curso = cursoSnapshot.getValue(Curso::class.java)
                    curso?.let {
                        if (it.disponible) {
                            cursosList.add(it)
                        }
                    }
                }

                // Notificar al adaptador que los datos han cambiado y deben actualizarse en el RecyclerView
                cursosAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error en caso de que ocurra
                // Por ejemplo, mostrar un mensaje de error o registrar el error en el log
            }
        })

        return root
    }

    override fun onItemClick(curso: Curso) {
        if (curso.disponible) {
            // Abrir el DisponibleActivity aquí cuando el curso está disponible
            val intent = Intent(requireContext(), DisponibleActivity::class.java)
            // Puedes pasar datos adicionales al DisponibleActivity a través del intent si es necesario
            intent.putExtra("curso_id", curso.id)
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "El curso no está disponible actualmente.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}