package com.example.gdufeview.ui

import com.nineoldandroids.animation.Animator

abstract class AnimatorListener : Animator.AnimatorListener {
    override fun onAnimationStart(animation: Animator?) {}
    override fun onAnimationCancel(animation: Animator?) {}
    override fun onAnimationRepeat(animation: Animator?) {}
}