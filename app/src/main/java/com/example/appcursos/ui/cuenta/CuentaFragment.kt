package com.example.appcursos.ui.cuenta

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.appcursos.InicioActivity
import com.example.appcursos.R
import com.example.appcursos.databinding.FragmentCuentaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class CuentaFragment : Fragment() {

    private var _binding: FragmentCuentaBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCuentaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Obtener la referencia a FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        val cuentaViewModel = ViewModelProvider(this).get(CuentaViewModel::class.java)

        val textView: TextView = binding.textCuenta
        cuentaViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val switchDarkMode = root.findViewById<Switch>(R.id.switchDarkMode)

// Verificar el modo actual y actualizar el estado del Switch en consecuencia
        switchDarkMode.isChecked = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

// Agregar el listener para cambiar el modo cuando el Switch cambia de estado
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Cambiar al modo oscuro
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                // Cambiar al modo claro
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            // Aplicar los cambios inmediatamente
            activity?.recreate()
        }

        val btnCerrarSesion: Button = root.findViewById(R.id.btnCerrarSesion)
        btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener la referencia a FirebaseAuth y el usuario actual
        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser!!

        // Configurar el TextView para mostrar la información de la cuenta
        val tvUserInfo: TextView = view.findViewById(R.id.tvUserInfo)

        // Verificar si el campo displayName es null y mostrar un mensaje alternativo si es el caso
        val displayName = currentUser.displayName
        val displayNameText = if (displayName != null) {
            "Nombre: $displayName"
        } else {
            "Nombre: (No especificado)"
        }

        val userInfo = "Usuario actual: \n" +
                "$displayNameText \n" +
                "Email: ${currentUser.email} \n" +
                "UID: ${currentUser.uid}"

        tvUserInfo.text = userInfo
    }

    private fun cerrarSesion() {
        // Mostrar un AlertDialog para confirmar el cierre de sesión
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Cerrar sesión")
        alertDialogBuilder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
        alertDialogBuilder.setPositiveButton("Sí") { dialogInterface: DialogInterface, _: Int ->
            // Llamar al método signOut() para cerrar la sesión
            firebaseAuth.signOut()

            // Aquí puedes navegar a la actividad de inicio de sesión o a cualquier otra actividad
            val intent = Intent(requireContext(), InicioActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()

            dialogInterface.dismiss()
        }
        alertDialogBuilder.setNegativeButton("No") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}