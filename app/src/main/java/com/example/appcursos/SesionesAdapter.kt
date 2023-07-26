package com.example.appcursos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class SesionesAdapter : RecyclerView.Adapter<SesionesAdapter.SesionViewHolder>() {

    private var sesionesList: List<Sesion> = emptyList()

    fun setSesiones(sesiones: List<Sesion>) {
        sesionesList = sesiones
        notifyDataSetChanged()
    }

    class SesionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNombreSesion: TextView = itemView.findViewById(R.id.textViewNombreSesion)
        val textViewTituloSesion: TextView = itemView.findViewById(R.id.textViewTituloSesion)
        val checkBoxSesionCompletada: CheckBox = itemView.findViewById(R.id.checkBoxSesionCompletada)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SesionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_disponible, parent, false)
        return SesionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SesionViewHolder, position: Int) {
        val sesion = sesionesList[position]
        holder.textViewNombreSesion.text = sesion.nombre
        holder.textViewTituloSesion.text = sesion.titulo

        // Establecer el estado del CheckBox según el estado de completada de la sesión
        holder.checkBoxSesionCompletada.isChecked = sesion.completada

        // Agregar un listener al CheckBox para detectar cambios en el estado
        holder.checkBoxSesionCompletada.setOnCheckedChangeListener { _, isChecked ->
            // Actualizar el estado de completada de la sesión en la base de datos Firebase
            val sesionRef = FirebaseDatabase.getInstance().reference
                .child("cursos")
                .child("curso1")
                .child("sesiones")
                .child(position.toString()) // Suponiendo que las sesiones están numeradas de 0 a n

            sesionRef.child("completada").setValue(isChecked)
        }
    }

    override fun getItemCount(): Int {
        return sesionesList.size
    }
}


