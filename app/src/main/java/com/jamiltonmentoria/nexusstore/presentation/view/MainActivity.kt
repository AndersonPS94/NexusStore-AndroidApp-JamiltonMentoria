package com.jamiltonmentoria.nexusstore.presentation.view

import android.os.Bundle
import android.content.Intent
import android.animation.ObjectAnimator
import android.animation.AnimatorSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jamiltonmentoria.nexusstore.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import android.animation.Animator
import android.animation.AnimatorListenerAdapter

import android.view.animation.AnimationUtils
import com.jamiltonmentoria.nexusstore.R
import android.view.SoundEffectConstants
import android.os.Vibrator
import android.os.VibrationEffect
import android.content.Context
import android.media.AudioManager

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setOnExitAnimationListener { splashScreenProvider ->
            val iconView = splashScreenProvider.iconView

            val floatUp = ObjectAnimator.ofFloat(iconView, View.TRANSLATION_Y, 0f, -40f).apply {
                duration = 500
                interpolator = AccelerateDecelerateInterpolator()
            }
            val floatDown = ObjectAnimator.ofFloat(iconView, View.TRANSLATION_Y, -40f, 0f).apply {
                duration = 500
                interpolator = AccelerateDecelerateInterpolator()
            }

            val fadeOut = ObjectAnimator.ofFloat(splashScreenProvider.view, View.ALPHA, 1f, 0f).apply {
                duration = 300
            }

            AnimatorSet().apply {
                playSequentially(floatUp, floatDown)
                play(fadeOut).after(floatDown)
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        splashScreenProvider.remove()
                    }
                })
                start()
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtons()
    }

    private fun setupButtons() {
        val buttons = listOf(
            binding.btnProducts,
            binding.btnCarts,
            binding.btnUsers,
            binding.btnPosts
        )

        val animation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        buttons.forEach { button ->
            button.startAnimation(animation)
            
            button.setOnClickListener {
                it.startAnimation(animation)
                audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK, 1.0f)
                vibrateDevice()

                if (button == binding.btnProducts) {
                    startActivity(Intent(this, com.jamiltonmentoria.nexusstore.presentation.products.ProductListActivity::class.java))
                } else if (button == binding.btnPosts) {
                    startActivity(Intent(this, com.jamiltonmentoria.nexusstore.presentation.posts.PostListActivity::class.java))
                }
            }
        }
    }

    private fun vibrateDevice() {
        val vibrator = getSystemService(Vibrator::class.java)
        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}
