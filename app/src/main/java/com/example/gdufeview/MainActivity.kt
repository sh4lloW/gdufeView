package com.example.gdufeview

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gjiazhe.panoramaimageview.GyroscopeObserver
import com.gjiazhe.panoramaimageview.PanoramaImageView
import com.royrodriguez.transitionbutton.TransitionButton

class MainActivity : AppCompatActivity() {
    private var gyroscopeObserver: GyroscopeObserver? = null
    private var frontButton: TransitionButton?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gyroscopeObserver = GyroscopeObserver()
        gyroscopeObserver!!.setMaxRotateRadian(Math.PI / 2)
        val panoramaImageView = findViewById<View>(R.id.front_image) as PanoramaImageView
        panoramaImageView.setGyroscopeObserver(gyroscopeObserver)
        frontButton = findViewById(R.id.front_button)
        frontButton?.setOnClickListener {
            frontButton!!.startAnimation()
            val handler: Handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                val isSuccessful = true;
                if (isSuccessful) {
                    frontButton!!.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND)
                        {
                            val intent = Intent(baseContext, ListViewActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            startActivity(intent)
                        }
                }else {
                    frontButton!!.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                }
            }, 1500)
        }
    }
}

