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

    //var bienvenidoLabel: TextView? = null
    //var continuarLabel: TextView? = null
    //var nuevoUsuario: TextView? = null
    //var signUpImageView: ImageView? = null
    //var usuarioTextField: TextInputLayout? = null
    //var contrasenaTextField: TextInputLayout? = null
    //=========================================================
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

        //signUpImageView = findViewById(R.id.signUpImageView)
        //bienvenidoLabel = findViewById(R.id.bienvenidoLabel)
        //continuarLabel = findViewById(R.id.continuarLabel)
        //val usuarioTextField = findViewById<EditText>(R.id.usuarioTextField)
        //contrasenaTextField = findViewById(R.id.contrasenaTextField)
        val signUpImageView = findViewById<ImageView>(R.id.signUpImageView)
        val bienvenidoLabel = findViewById<TextView>(R.id.bienvenidoLabel)
        val continuarLabel = findViewById<TextView>(R.id.continuarLabel)
        //val nameEditText = findViewById<EditText>(R.id.nameEditText)
        //val inicioSesion = findViewById<MaterialButton>(R.id.inicioSesion)
        val nuevoUsuario = findViewById<TextView>(R.id.nuevoUsuario)
        val btnNuevoUsuario = findViewById<Button>(R.id.btnNuevoUsuario)

//=======================================================================
        //Firebase Autentication
        auth=FirebaseAuth.getInstance()
        nameEditText= findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText= findViewById(R.id.passwordEditText)
        confirmPasswordEditText= findViewById(R.id.confirmPasswordEditText)

        val inicioRegistro = findViewById<Button>(R.id.inicioRegistro)
        val btnVolverLogin = findViewById<Button>(R.id.btnVolverLogin)
//================================================================================
        val emailEditText = findViewById<TextView>(R.id.emailEditText)
        //val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        //val confirmPasswordEditText = findViewById<EditText>(R.id.confirmPasswordEditText)
        //val btnIngresar = findViewById(android.R.id.btnIngresar)
        //Importo al método cuando el usuario hace click en el botón nuevoUsuario
        //btnNuevoUsuario2.setOnClickListener(View.OnClickListener { transitionBack() })
//====================================================================================
        //Animaciones
        val animacionParaArriba = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba)
        signUpImageView.animation = animacionParaArriba;
        bienvenidoLabel.animation = animacionParaArriba;
        continuarLabel.animation = animacionParaArriba;
        //usuarioTextField.animation = animacionParaArriba;
        inicioRegistro.animation = animacionParaArriba;
        nuevoUsuario.animation = animacionParaArriba;
        btnVolverLogin.animation = animacionParaArriba;

//=====================================================================================
        //Actividad de los botones
        inicioRegistro.setOnClickListener(){
            setup()
        }

        btnVolverLogin.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
//=====================================================================================
    }
    fun registrar(view:View){
        // Setup
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
            val childUpdates = HashMap<String,Any> ()
            childUpdates["/Usuarios/$key"] = postValues
            dbref.updateChildren(childUpdates)
        }catch (ex:Throwable)
        {
            Toast.makeText(this,"Error${ex.message}",Toast.LENGTH_LONG).show()
        }
    }
}
/*
    //Firebase Prueba Autentication Version 1
    //Mensaje de Verificación
    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    //Pasaje de variables a la HomeActivity que sería el Login
    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent = Intent(this, LoginActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
            //Como le paso las otras dos variables
            //putExtra("nombre",nombre)
        }
        startActivity(homeIntent)
    }
}
    //Función para las transiciones
    override fun onBackPressed() {
        transitionBack()
    }
    //Método para volver atras
    fun transitionBack() {
        val intent = Intent(this@Registro, LoginActivity::class.java)

    //Arreglo de las animaciones que se van a realizar
        val pairs: Array<Pair<*, *>?> = arrayOfNulls(7)
        pairs[0] =
            Pair<View?, String>(signUpImageView, "logoImageTrans")
        pairs[1] =
            Pair<View?, String>(bienvenidoLabel, "textTrans")
        pairs[2] = Pair<View?, String>(
            continuarLabel,
            "iniciaSesionTextTrans"
        )
        pairs[3] = Pair<View?, String>(
            usuarioSignUpTextField,
            "emailInputTextTrans"
        )
        pairs[4] = Pair<View?, String>(
            contrasenaTextField,
            "passwordInputTextTrans"
        )
        pairs[5] =
            Pair<View?, String>(inicioSesion, "buttonSignInTrans")
        pairs[6] =
            Pair<View?, String>(nuevoUsuario, "newUserTrans")

        //Código para verificar si se cuenta con la versión
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val options = ActivityOptions.makeSceneTransitionAnimation(this@SignUpActivity, *pairs)
            startActivity(intent, options.toBundle())
        } else {
            startActivity(intent)
            finish()
        }
    }
}*/
