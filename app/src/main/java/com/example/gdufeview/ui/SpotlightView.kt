package com.example.gdufeview.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import com.example.gdufeview.R

class SpotlightView(context: Context, attrs: AttributeSet?) :
    View(context, attrs) {
    private var mTargetId = 0
    private var mMask: Bitmap? = null
    private var mMaskX = 0f
    private var mMaskY = 0f
    private var mMaskScale = 0f
    private val mShaderMatrix = Matrix()
    private var mTargetBitmap: Bitmap? = null
    private val mPaint = Paint()
    private var mCallback: AnimationSetupCallback? = null

    interface AnimationSetupCallback {
        fun onSetupAnimation(spotlight: SpotlightView?)
    }

    var maskScale: Float
        get() = mMaskScale
        set(maskScale) {
            mMaskScale = maskScale
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val maskW = mMask!!.width / 2.0f
        val maskH = mMask!!.height / 2.0f
        val x = mMaskX - maskW * mMaskScale
        val y = mMaskY - maskH * mMaskScale
        mShaderMatrix.setScale(1.0f / mMaskScale, 1.0f / mMaskScale)
        mShaderMatrix.preTranslate(-x, -y)
        mPaint.shader.setLocalMatrix(mShaderMatrix)
        canvas.translate(x, y)
        canvas.scale(mMaskScale, mMaskScale)
        canvas.drawBitmap(mMask!!, 0.0f, 0.0f, mPaint)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                createShader()
                maskScale = 1.0f
                if (mCallback != null) {
                    mCallback!!.onSetupAnimation(this@SpotlightView)
                }
                Utils.removeOnGlobalLayoutListenerCompat(this@SpotlightView, this)
            }
        })
    }

    private fun createShader() {
        val target = rootView.findViewById<View>(mTargetId)
        mTargetBitmap = createBitmap(target)
        val targetShader = createShader(mTargetBitmap)
        mPaint.shader = targetShader
    }

    companion object {
        private fun createShader(b: Bitmap?): Shader {
            return BitmapShader(b!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        }

        private fun createBitmap(target: View): Bitmap {
            val b = Bitmap.createBitmap(target.width, target.height, Bitmap.Config.ARGB_8888)
            val c = Canvas(b)
            target.draw(c)
            return b
        }

        private fun convertToAlphaMask(b: Bitmap): Bitmap {
            val a = Bitmap.createBitmap(b.width, b.height, Bitmap.Config.ALPHA_8)
            val c = Canvas(a)
            c.drawBitmap(b, 0.0f, 0.0f, null)
            return a
        }
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SpotlightView, 0, 0)
        try {
            mTargetId = a.getResourceId(R.styleable.SpotlightView_target, 0)
            val maskId = a.getResourceId(R.styleable.SpotlightView_mask, 0)
            mMask = convertToAlphaMask(
                BitmapFactory.decodeResource(
                    resources, maskId
                )
            )
            Log.d("Spotlight", "c=" + mMask!!.config)
        } catch (e: Exception) {
            Log.e("Spotlight", "Error while creating the view:", e)
        } finally {
            a.recycle()
        }
    }
}