import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appcursos.R

class SesionAdapter(private val sesionesList: List<String>) :
    RecyclerView.Adapter<SesionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_sesion, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sesion = sesionesList[position].split(" - ")
        holder.textViewDescripcion.text = sesion[0]
        holder.textViewNombre.text = sesion[1]
    }

    override fun getItemCount(): Int {
        return sesionesList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewDescripcion: TextView = itemView.findViewById(R.id.textViewDescripcion)
        val textViewNombre: TextView = itemView.findViewById(R.id.textViewNombre)
    }
}