package de.byeshurun.globaljournal.splashLogin

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import de.byeshurun.globaljournal.dashboard.DashboardActivity
import de.byeshurun.globaljournal.R

class LoginActivity : AppCompatActivity() {

    private lateinit var inputTextUsername: EditText
    private lateinit var inputTextPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_layout)

        val snackbar = Snackbar.make(
            findViewById(android.R.id.content),
            "Welcome To Global Journal. Please enter your username & password.",
            Snackbar.LENGTH_SHORT
        )
        snackbar.show()

        inputTextUsername = findViewById(R.id.input_text_username)
        inputTextPassword = findViewById(R.id.input_text_password)

        val loginButton: Button = findViewById(R.id.login)
        loginButton.setOnClickListener {
            onLoginButtonClicked()
        }

        inputTextPassword.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    when (keyCode) {
                        KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                            onLoginButtonClicked()
                            return true
                        }

                        else -> {}
                    }
                }
                return false
            }
        })
    }

    private fun onLoginButtonClicked() {
        val userName = "Bar"
        val userPassword = "12345"

        val enteredTextUsername = inputTextUsername.text.toString()
        val enteredTextPassword = inputTextPassword.text.toString()

        if (userName == enteredTextUsername && userPassword == enteredTextPassword) {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            inputTextUsername.error = "Credentials are incorrect"
            inputTextPassword.error = "Credentials are incorrect"
            Toast.makeText(this, "Credentials are incorrect", Toast.LENGTH_SHORT).show()
        }
    }
}
