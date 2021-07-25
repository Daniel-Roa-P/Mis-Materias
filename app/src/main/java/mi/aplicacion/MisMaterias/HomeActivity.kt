package mi.aplicacion.MisMaterias

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

enum class ProviderType{
    BASIC,
    GOOGLE

}

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")

        setUp(email ?: "", provider ?: "")

        val prefs = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email",email)
        prefs.putString("provider", provider)
        prefs.apply()

    }

    private fun setUp(email: String, provider: String) {

        title = "inicio"
        emailText.text = email
        password.text = provider

        button2.setOnClickListener {

            val prefs = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

            FirebaseAuth.getInstance().signOut()
            onBackPressed()

        }

    }
}