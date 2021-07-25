package mi.aplicacion.MisMaterias

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {

    val GOOGLE_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        setup()
        session()

    }

    override fun onStart() {
        super.onStart()

        const_auth.visibility = View.VISIBLE

    }

    private fun session() {

        val prefs = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider",null)

        if (email!= null && provider!=null){

            const_auth.visibility = View.INVISIBLE
            showHome(email, ProviderType.valueOf(provider))

        }


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

        googleButton.setOnClickListener {

            val googleConf =GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)

            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GOOGLE_SIGN_IN){

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {

                val account = task.getResult(ApiException::class.java)

                if (account != null){

                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {

                        if (it.isSuccessful){

                            showHome(account.email ?: "", ProviderType.GOOGLE)

                        } else {

                            showAlert()

                        }

                    }

                }

            } catch (e : ApiException){

                showAlert()

            }

        }

    }

}