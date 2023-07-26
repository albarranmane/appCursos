package com.example.appcursos.ui.buscar

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appcursos.databinding.FragmentBuscarBinding
import com.example.appcursos.ui.cursos.Curso
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BuscarFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private var _binding: FragmentBuscarBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var cursosAdapter: CursosAdapter
    private val cursosList: MutableList<Curso> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuscarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.recyclerViewCursos
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        cursosAdapter = CursosAdapter(cursosList)
        recyclerView.adapter = cursosAdapter

        database = FirebaseDatabase.getInstance().reference.child("cursos")

        val etBusqueda = binding.etBusqueda
        etBusqueda.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim().toLowerCase()

                if (query.isNotEmpty()) {
                    buscarCursos(query)
                } else {
                    // Si el campo de búsqueda está vacío, puedes mostrar todos los cursos o limpiar los resultados.
                    cursosList.clear()
                    cursosAdapter.notifyDataSetChanged()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        return root
    }

    private fun buscarCursos(query: String) {
        database.orderByChild("name")
            .startAt(query)
            .endAt(query + "\uf8ff")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    cursosList.clear()
                    for (cursoSnapshot in snapshot.children) {
                        val curso = cursoSnapshot.getValue(Curso::class.java)
                        curso?.let {
                            cursosList.add(it)
                        }
                    }
                    cursosAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejo de errores si es necesario.
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}