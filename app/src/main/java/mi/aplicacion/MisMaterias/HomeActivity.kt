package mi.aplicacion.MisMaterias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

enum class ProviderType{

    BASIC

}

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")

        setUp(email ?: "", provider ?: "")

    }

    private fun setUp(email: String, provider: String) {

        title = "inicio"
        emailText.text = email
        password.text = provider

        button2.setOnClickListener {

            FirebaseAuth.getInstance().signOut()
            onBackPressed()

        }

    }
}