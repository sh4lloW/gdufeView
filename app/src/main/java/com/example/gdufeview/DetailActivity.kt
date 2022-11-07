package com.example.gdufeview

import android.graphics.Bitmap
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.ImageView
import com.example.gdufeview.ui.AnimatorListener
import com.example.gdufeview.ui.Utils
import com.nineoldandroids.animation.Animator
import com.nineoldandroids.view.ViewHelper
import com.nineoldandroids.view.ViewPropertyAnimator

class DetailActivity : AbstractDetailActivity() {
    private var animatedHero: ImageView? = null
    override fun postCreate() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        animatedHero = findViewById(R.id.animated_photo)
        ViewHelper.setAlpha(container, 0f)
        animatedHero!!.setImageBitmap(photo)
    }

    private fun setupPhoto(resource: Int): Bitmap {
        val bitmap: Bitmap = ListViewActivity.sPhotoCache.get(resource)
        hero!!.setImageBitmap(bitmap)
        animatedHero!!.setImageBitmap(bitmap)
        return bitmap
    }

    override fun setupEnterAnimation() {
        animatedHero!!.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                Utils.removeOnGlobalLayoutListenerCompat(animatedHero!!, this)
                runEnterAnimation()
            }
        })
    }

    fun runEnterAnimation() {
        val bundle = intent.extras
        val thumbnailTop = bundle!!.getInt("top")
        val thumbnailLeft = bundle.getInt("left")
        val thumbnailWidth = bundle.getInt("width")
        val thumbnailHeight = bundle.getInt("height")
        val mWidthScale = thumbnailWidth.toFloat() / animatedHero!!.width
        val mHeightScale = thumbnailHeight.toFloat() / animatedHero!!.height
        ViewHelper.setPivotX(animatedHero, 0f)
        ViewHelper.setPivotY(animatedHero, 0f)
        ViewHelper.setScaleX(animatedHero, mWidthScale)
        ViewHelper.setScaleY(animatedHero, mHeightScale)
        ViewHelper.setTranslationX(animatedHero, thumbnailLeft.toFloat())
        ViewHelper.setTranslationY(animatedHero, thumbnailTop.toFloat())
        ViewPropertyAnimator.animate(animatedHero).scaleX(1f).scaleY(1f).translationX(0f)
            .translationY(0f).setInterpolator(
                sDecelerator
            ).setListener(object : AnimatorListener() {
                override fun onAnimationEnd(animation: Animator?) {
                    ViewPropertyAnimator.animate(animatedHero).alpha(0f)
                    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                }
            })
        ViewPropertyAnimator.animate(container).alpha(1f)
    }

    override fun setupExitAnimation() {
        ViewPropertyAnimator.animate(animatedHero).alpha(1f)
        runExitAnimation()
    }

    private fun runExitAnimation() {
        val bundle = intent.extras
        val thumbnailTop = bundle!!.getInt("top")
        val thumbnailLeft = bundle.getInt("left")
        val thumbnailWidth = bundle.getInt("width")
        val thumbnailHeight = bundle.getInt("height")
        val mWidthScale = thumbnailWidth.toFloat() / animatedHero!!.width
        val mHeightScale = thumbnailHeight.toFloat() / animatedHero!!.height
        ViewHelper.setPivotX(animatedHero, 0f)
        ViewHelper.setPivotY(animatedHero, 0f)
        ViewHelper.setScaleX(animatedHero, 1f)
        ViewHelper.setScaleY(animatedHero, 1f)
        ViewHelper.setTranslationX(animatedHero, 0f)
        ViewHelper.setTranslationY(animatedHero, 0f)
        ViewPropertyAnimator.animate(animatedHero).scaleX(mWidthScale).scaleY(mHeightScale)
            .translationX(thumbnailLeft.toFloat()).translationY(thumbnailTop.toFloat())
            .setInterpolator(
                sAccelerator
            ).setListener(object : AnimatorListener() {
                override fun onAnimationEnd(animation: Animator?) {
                    finish()
                }
            })
        ViewPropertyAnimator.animate(container).alpha(0f)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    companion object {
        private val sDecelerator: Interpolator = DecelerateInterpolator()
        private val sAccelerator: Interpolator = AccelerateInterpolator()
    }
}
