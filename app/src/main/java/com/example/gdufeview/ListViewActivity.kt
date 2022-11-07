package com.example.gdufeview

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.gdufeview.UI.Utils
import com.nineoldandroids.view.ViewHelper

class ListViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view)
        val parent = findViewById<View>(R.id.parent)
        parent.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                Utils.removeOnGlobalLayoutListenerCompat(parent, this)
                setImageBitmap(
                    findViewById<View>(R.id.card_photo_1).findViewById<View>(R.id.photo) as ImageView,
                    R.drawable.library
                )
                setImageBitmap(
                    findViewById<View>(R.id.card_photo_2).findViewById<View>(R.id.photo) as ImageView,
                    R.drawable.first_teaching_building
                )
                setImageBitmap(
                    findViewById<View>(R.id.card_photo_3).findViewById<View>(R.id.photo) as ImageView,
                    R.drawable.laboratory_building
                )
                setImageBitmap(
                    findViewById<View>(R.id.card_photo_4).findViewById<View>(R.id.photo) as ImageView,
                    R.drawable.comprehensive_building
                )
            }
        })
    }

    private fun setImageBitmap(imageView: ImageView, resId: Int) {
        val bitmap = Utils.decodeSampledBitmapFromResource(
            resources,
            resId, imageView.measuredWidth, imageView.measuredHeight
        )
        sPhotoCache.put(resId, bitmap)
        imageView.setImageBitmap(bitmap)
    }

    override fun onResume() {
        super.onResume()
        if (!Utils.hasLollipop()) {
            ViewHelper.setAlpha(findViewById(R.id.card_photo_1), 1.0f)
            ViewHelper.setAlpha(findViewById(R.id.card_photo_2), 1.0f)
            ViewHelper.setAlpha(findViewById(R.id.card_photo_3), 1.0f)
            ViewHelper.setAlpha(findViewById(R.id.card_photo_4), 1.0f)
        }
    }

    fun showPhoto(view: View) {
        val intent = Intent()
        intent.setClass(this, DetailActivity::class.java)
        var resId = 0
        when (view.id) {
            R.id.show_photo_1 -> {
                intent.putExtra("lat", 37.6329946)
                    .putExtra("lng", -122.4938344)
                    .putExtra("zoom", 14.0f)
                    .putExtra("title", "图书馆")
                    .putExtra("description", resources.getText(R.string.descript_library))
                    .putExtra("photo", R.drawable.library)
                resId = R.id.card_photo_1
            }
            R.id.show_photo_2 -> {
                intent.putExtra("lat", 37.73284)
                    .putExtra("lng", -122.503065)
                    .putExtra("zoom", 15.0f)
                    .putExtra("title", "第一教学楼")
                    .putExtra("description", resources.getText(R.string.descript_first_teaching_building))
                    .putExtra("photo", R.drawable.first_teaching_building)
                resId = R.id.card_photo_2
            }
            R.id.show_photo_3 -> {
                intent.putExtra("lat", 36.861897)
                    .putExtra("lng", -111.374438)
                    .putExtra("zoom", 11.0f)
                    .putExtra("title", "实验楼")
                    .putExtra("description", resources.getText(R.string.descript_laboratory_building))
                    .putExtra("photo", R.drawable.laboratory_building)
                resId = R.id.card_photo_3
            }
            R.id.show_photo_4 -> {
                intent.putExtra("lat", 36.596125)
                    .putExtra("lng", -118.1604282)
                    .putExtra("zoom", 9.0f)
                    .putExtra("title", "综合楼")
                    .putExtra("description", resources.getText(R.string.descript_comprehensive_building))
                    .putExtra("photo", R.drawable.comprehensive_building)
                resId = R.id.card_photo_4
            }
        }
        startActivityGingerBread(view, intent, resId)
    }

    private fun startActivityGingerBread(view: View, intent: Intent, resId: Int) {
        val screenLocation = IntArray(2)
        view.getLocationOnScreen(screenLocation)
        intent.putExtra("left", screenLocation[0]).putExtra("top", screenLocation[1])
            .putExtra("width", view.width).putExtra("height", view.height)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    companion object {
        var sPhotoCache = SparseArray<Bitmap>(4)
    }
}