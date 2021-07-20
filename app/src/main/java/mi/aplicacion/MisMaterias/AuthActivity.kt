package mi.aplicacion.MisMaterias

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)


        setup()

    }

    private fun setup() {

        title = "Autentication"
        registerButton.setOnClickListener {

            if(etCorreoAuth.text.isNotEmpty() && etClaveAuth.text.isNotEmpty()){

                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(etCorreoAuth.text.toString() , etClaveAuth.text.toString() ).addOnCompleteListener {

                        if (it.isSuccessful){

                            showHome(it.result?.user?.email ?: "", ProviderType.BASIC)

                        } else {

                            showAlert()

                        }

                }

            }

        }

        loginButton.setOnClickListener {

            if(etCorreoAuth.text.isNotEmpty() && etClaveAuth.text.isNotEmpty()){

                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(etCorreoAuth.text.toString() , etClaveAuth.text.toString() ).addOnCompleteListener {

                    if (it.isSuccessful){

                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)

                    } else {

                        showAlert()

                    }

                }

            }

        }

    }

    private fun showHome(email: String, provider: ProviderType) {

        val homeIntent = Intent(this, HomeActivity::class.java).apply {

            putExtra("email", email)
            putExtra("provider", provider.name)

        }

        startActivity(homeIntent)

    }

    private fun showAlert() {


        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando el usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog:AlertDialog = builder.create()

        dialog.show()

    }
}