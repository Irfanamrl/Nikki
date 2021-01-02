package id.ac.ui.cs.mobileprogramming.MuhammadIrfanAmrullah.Nikki.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.content.Intent
import id.ac.ui.cs.mobileprogramming.MuhammadIrfanAmrullah.Nikki.openGL.OpenGLView

class SplashActivity : AppCompatActivity() {
    companion object {
        private val SPLASH_TIME_OUT:Long = 3000 // 1 sec

    }

    private lateinit var openGLView : OpenGLView

    // This is the loading time of the splash screen
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        openGLView = OpenGLView(this)
        setContentView(openGLView)


        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

            startActivity(Intent(this, MainActivity::class.java))

            // close this activity
            finish()
        }, SPLASH_TIME_OUT)
    }

    override fun onResume() {
        super.onResume()
        openGLView.onResume()
    }

    override fun onPause() {
        super.onPause()
        openGLView.onPause()
    }
}