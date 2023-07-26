package com.example.appcursos.ui.buscar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appcursos.R
import com.example.appcursos.ui.cursos.Curso

class CursosAdapter(private val cursosList: List<Curso>) :
    RecyclerView.Adapter<CursosAdapter.buscarViewHolder>() {

    // Interfaz para el listener de clics en los cursos
    interface OnItemClickListener {
        fun onItemClick(curso: Curso)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class buscarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cursoName: TextView = itemView.findViewById(R.id.textViewCursoName)
        val cursoDescripcion: TextView = itemView.findViewById(R.id.textViewCursoDescripcion)
        val imageViewCurso: ImageView = itemView.findViewById(R.id.imageViewCurso)
        // Agrega aquí las vistas adicionales que desees mostrar en cada elemento de la lista.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): buscarViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_buscar, parent, false)
        return buscarViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: buscarViewHolder, position: Int) {
        val curso = cursosList[position]
        holder.cursoName.text = curso.name
        holder.cursoDescripcion.text = curso.descripcion

        // Cargar la imagen del curso desde el directorio drawable
        val resourceId = holder.itemView.context.resources.getIdentifier(
            curso.imagen, "drawable", holder.itemView.context.packageName
        )
        holder.imageViewCurso.setImageResource(resourceId)

        // Vincula aquí las vistas adicionales y datos para cada elemento de la lista.

        // Agregar un OnClickListener al itemView para detectar el clic en el curso
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(curso)
        }
    }

    override fun getItemCount(): Int {
        return cursosList.size
    }
}