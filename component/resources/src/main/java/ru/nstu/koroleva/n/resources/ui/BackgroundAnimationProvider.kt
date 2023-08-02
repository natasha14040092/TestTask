package ru.nstu.koroleva.n.resources.ui

import android.graphics.drawable.AnimationDrawable

fun setBackgroundAnimation(animDrawable: AnimationDrawable) {
    animDrawable.setEnterFadeDuration(10)
    animDrawable.setExitFadeDuration(2000)
    animDrawable.start()
}