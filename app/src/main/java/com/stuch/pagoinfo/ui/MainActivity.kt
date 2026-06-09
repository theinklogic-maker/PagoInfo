package com.stuch.pagoinfo.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.stuch.pagoinfo.databinding.ActivityMainBinding
import com.stuch.pagoinfo.model.DatosPago
import com.stuch.pagoinfo.model.PagoRepository
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repo: PagoRepository
    private var tts: TextToSpeech? = null

    private val ttsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val mensaje = intent?.getStringExtra("mensaje") ?: return
            tts?.speak(mensaje, TextToSpeech.QUEUE_FLUSH, null, "pagoinfo_car")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repo = PagoRepository.getInstance(this)

        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale("es", "AR")
            }
        }

        val filter = IntentFilter("com.stuch.pagoinfo.TTS_SPEAK")
        registerReceiver(ttsReceiver, filter, RECEIVER_NOT_EXPORTED)

        cargarFormulario()

        binding.btnGuardar.setOnClickListener { guardar() }
        binding.btnProbarTTS.setOnClickListener { probarTTS() }
    }

    private fun cargarFormulario() {
        val d = repo.cargar()
        binding.editAlias.setText(d.alias)
        binding.editTitular.setText(d.titular)
        binding.editBanco.setText(d.banco)
        binding.editMensajeTTS.setText(d.mensajeTTS)
    }

    private fun guardar() {
        val alias = binding.editAlias.text.toString().trim()
        if (alias.isBlank()) {
            Toast.makeText(this, "El alias es obligatorio", Toast.LENGTH_SHORT).show()
            return
        }
        repo.guardar(
            DatosPago(
                alias      = alias,
                titular    = binding.editTitular.text.toString().trim(),
                banco      = binding.editBanco.text.toString().trim(),
                mensajeTTS = binding.editMensajeTTS.text.toString().trim()
            )
        )
        Toast.makeText(this, "Guardado ✓", Toast.LENGTH_SHORT).show()
    }

    private fun probarTTS() {
        guardar()
        val d = repo.cargar()
        val mensaje = buildString {
            append(d.mensajeTTS.trimEnd())
            if (d.alias.isNotBlank())   append(" ${d.alias}.")
            if (d.banco.isNotBlank())   append(" Banco ${d.banco}.")
            if (d.titular.isNotBlank()) append(" A nombre de ${d.titular}.")
        }
        tts?.speak(mensaje, TextToSpeech.QUEUE_FLUSH, null, "pagoinfo_tts")
    }

    override fun onDestroy() {
        unregisterReceiver(ttsReceiver)
        tts?.shutdown()
        super.onDestroy()
    }
}
