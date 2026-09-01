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
import com.jamiltonmentoria.nexusstore.api.DummyJsonService
import com.jamiltonmentoria.nexusstore.api.RetrofitCustom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val dummyJsonAPI by lazy {
        RetrofitCustom().showDummyJson()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtons()

        binding.textoResultado.text = "carregando..."

        CoroutineScope(Dispatchers.IO).launch {
            val resposta = dummyJsonAPI.showProducts()
            if (resposta.isSuccessful && resposta.body() != null) {
                val listaProdutos = resposta.body()

                var textoExibicao = ""
                listaProdutos?.products?.forEach { produto ->
                    textoExibicao += " ${produto.id}) ${produto.title} \n"
                }
                withContext(Dispatchers.Main) {
                    binding.textoResultado.text = textoExibicao
                }

            } else {
                withContext(Dispatchers.Main) {
                    binding.textoResultado.text = "Erro ao carregar dados"
                }
            }
        }
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
