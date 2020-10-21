package com.example.ferretexapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserHandle
import android.util.Log
import android.util.Pair
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.ferretexapp.DataModels.User
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registro.*


class Registro : AppCompatActivity() {
    //Autentication Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var nameEditText: TextView
    private lateinit var emailEditText: TextView
    private lateinit var passwordEditText: TextView
    private lateinit var confirmPasswordEditText: TextView
    private var user : FirebaseUser?=null
//=================================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
//=====================================================================
        //Definicion de las variables para la animación
        val signUpImageView = findViewById<ImageView>(R.id.signUpImageView)
        val bienvenidoLabel = findViewById<TextView>(R.id.bienvenidoLabel)
        val continuarLabel = findViewById<TextView>(R.id.continuarLabel)

        val nuevoUsuario = findViewById<TextView>(R.id.nuevoUsuario)
//=======================================================================
        //Firebase Autentication
        auth=FirebaseAuth.getInstance()
        nameEditText= findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText= findViewById(R.id.passwordEditText)
        confirmPasswordEditText= findViewById(R.id.confirmPasswordEditText)

        val inicioRegistro = findViewById<Button>(R.id.inicioRegistro)
        val btnVolverLogin = findViewById<Button>(R.id.btnVolverLogin)
//====================================================================================
        //Animaciones
        val animacionParaArriba = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba)
        signUpImageView.animation = animacionParaArriba;
        bienvenidoLabel.animation = animacionParaArriba;
        continuarLabel.animation = animacionParaArriba;
        inicioRegistro.animation = animacionParaArriba;
        nuevoUsuario.animation = animacionParaArriba;
        btnVolverLogin.animation = animacionParaArriba;
//=====================================================================================
        //Actividad de los botones
        /*inicioRegistro.setOnClickListener(){
            Log.d("Setup","Setup del boton")
            setup()
        }
        */
        btnVolverLogin.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
//=====================================================================================
    }
    fun registrar(view:View){
        setup()
        Log.w("Grabar","gg")
    }
    //Firebase Autentication
    //Setup
    //Funcion para Registrarse
    private fun setup() {
        var name: String = nameEditText.text.toString()
        var email: String = emailEditText.text.toString()
        var password: String = passwordEditText.text.toString()
        var confirmpassword: String = confirmPasswordEditText.text.toString()
        if (email!="" && password!="") {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user: FirebaseUser? = auth.currentUser
                        Log.d("dbref", "Referencia a Base de Datos")
                        grabar(name,email)

                    } else {
                        Log.w("Error", "${task.exception?.message}")
                    }
                }
        }

    }
    private fun DbReference():DatabaseReference{
        val dbRef=FirebaseDatabase.getInstance().reference
        return dbRef
    }

    private fun grabar(name:String,email:String){
        try {
            val dbref = DbReference()
            val key = dbref.child("Usuarios").push().key
            val userObject = User(name, email)
            val postValues = userObject.toMap()
            val childUpdates = HashMap<String,Any>()
            childUpdates["/Usuarios/$key"] = postValues
            dbref.updateChildren(childUpdates)
        }catch (ex:Throwable)
        {
            Toast.makeText(this,"Error${ex.message}",Toast.LENGTH_LONG).show()
        }
    }
}
