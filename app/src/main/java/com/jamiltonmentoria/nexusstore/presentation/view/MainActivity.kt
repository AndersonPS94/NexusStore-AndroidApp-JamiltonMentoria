package com.jamiltonmentoria.nexusstore.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jamiltonmentoria.nexusstore.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

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
        super.onCreate(savedInstanceState)
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
            }
        }
    }

    private fun vibrateDevice() {
        val vibrator = getSystemService(Vibrator::class.java)
        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}
