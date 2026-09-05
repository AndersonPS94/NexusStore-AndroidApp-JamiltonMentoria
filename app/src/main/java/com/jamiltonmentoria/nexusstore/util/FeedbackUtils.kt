package com.jamiltonmentoria.nexusstore.util

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.jamiltonmentoria.nexusstore.R

object FeedbackUtils {

    fun playLikeSound(context: Context) {
        playSound(context, R.raw.likesound)
        vibrate(context, 50)
    }

    fun playFavoriteSound(context: Context) {
        playSound(context, R.raw.favoriteclicksound)
        vibrate(context, 70)
    }

    fun playCheckSound(context: Context) {
        playSound(context, R.raw.checksound)
        vibrate(context, 100)
    }

    private fun playSound(context: Context, resId: Int) {
        try {
            MediaPlayer.create(context, resId)?.apply {
                setOnCompletionListener { release() }
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun vibrate(context: Context, duration: Long) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(duration)
        }
    }
}
