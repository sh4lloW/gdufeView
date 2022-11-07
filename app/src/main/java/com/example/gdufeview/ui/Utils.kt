package com.example.gdufeview.ui

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.view.ViewTreeObserver

object Utils {
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    fun decodeSampledBitmapFromResource(
        res: Resources?,
        resId: Int,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, options)
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, resId, options)
    }

    fun removeOnGlobalLayoutListenerCompat(
        v: View,
        listener: ViewTreeObserver.OnGlobalLayoutListener?
    ) {
        if (hasJellyBean()) {
            v.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        } else {
            v.viewTreeObserver.removeGlobalOnLayoutListener(listener)
        }
    }

    private fun hasJellyBean(): Boolean {
        return true
    }

    fun hasLollipop(): Boolean {
        return true
    }
}