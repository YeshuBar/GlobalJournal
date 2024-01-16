package de.byeshurun.globaljournal.splashLogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import de.byeshurun.globaljournal.R

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen_activity_layout)
        Handler().postDelayed({

            val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
            startActivity(intent)

            finish()
        }, 2000)
    }
}