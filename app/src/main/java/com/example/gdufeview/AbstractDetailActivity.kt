package com.example.gdufeview

import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette

abstract class AbstractDetailActivity : AppCompatActivity() {
    var hero: ImageView? = null
    var photo: Bitmap? = null
    var container: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        hero = findViewById<View>(R.id.show_photo) as ImageView
        container = findViewById(R.id.container)
        photo = setupPhoto(intent.getIntExtra("photo", R.drawable.library))
        colorize(photo)
        setupText()
        postCreate()
        setupEnterAnimation()
    }

    abstract fun postCreate()
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun applyPalette(palette: Palette) {
        container!!.setBackgroundColor(palette.getDarkMutedColor(getColor(R.color.default_dark_muted)))
        val titleView = findViewById<View>(R.id.title) as TextView
        titleView.setTextColor(palette.getVibrantColor(getColor(R.color.default_vibrant)))
        val descriptionView = findViewById<View>(R.id.description) as TextView
        descriptionView.setTextColor(palette.getLightVibrantColor(getColor(R.color.default_light_vibrant)))
    }

    private fun setupPhoto(resource: Int): Bitmap {
        val bitmap: Bitmap = ListViewActivity.sPhotoCache.get(resource)
        hero!!.setImageBitmap(bitmap)
        return bitmap
    }

    private fun colorize(photo: Bitmap?) {
        val palette = Palette.from(photo!!).generate()
        applyPalette(palette)
    }

    private fun setupText() {
        val titleView = findViewById<View>(R.id.title) as TextView
        titleView.text = intent.getStringExtra("title")
        val descriptionView = findViewById<View>(R.id.description) as TextView
        descriptionView.text = intent.getStringExtra("description")
    }

    override fun onBackPressed() {
        setupExitAnimation()
    }

    abstract fun setupEnterAnimation()
    abstract fun setupExitAnimation()
}