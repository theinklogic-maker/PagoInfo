package com.stuch.pagoinfo.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.widget.ArrayAdapter
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
    private var voicesDisponibles: List<Voice> = emptyList()

    private val ttsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val mensaje = intent?.getStringExtra("mensaje") ?: return
            hablar(mensaje)
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
                cargarVoces()
            }
        }

        val filter = IntentFilter("com.stuch.pagoinfo.TTS_SPEAK")
        registerReceiver(ttsReceiver, filter, RECEIVER_NOT_EXPORTED)

        cargarFormulario()

        binding.btnGuardar.setOnClickListener { guardar() }
        binding.btnProbarTTS.setOnClickListener { probarTTS() }
    }

    private fun cargarVoces() {
        val todas = tts?.voices?.toList() ?: return
        voicesDisponibles = todas.filter {
            it.locale.language == "es" && !it.isNetworkConnectionRequired
        }.ifEmpty { todas.filter { !it.isNetworkConnectionRequired } }

        if (voicesDisponibles.isEmpty()) return

        val nombres = voicesDisponibles.map { voz ->
            val genero = when {
                voz.name.contains("female", ignoreCase = true) ||
                voz.name.contains("mujer", ignoreCase = true) -> "Femenina"
                voz.name.contains("male", ignoreCase = true) ||
                voz.name.contains("hombre", ignoreCase = true) -> "Masculina"
                else -> "Voz"
            }
            "$genero — ${voz.locale.displayLanguage} (${voz.name.take(20)})"
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombres)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerVoz.adapter = adapter

        val vozGuardada = repo.cargar().vozNombre
        val idx = voicesDisponibles.indexOfFirst { it.name == vozGuardada }
        if (idx >= 0) binding.spinnerVoz.setSelection(idx)
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
        val vozNombre = if (voicesDisponibles.isNotEmpty() &&
            binding.spinnerVoz.selectedItemPosition >= 0) {
            voicesDisponibles.getOrNull(binding.spinnerVoz.selectedItemPosition)?.name ?: ""
        } else ""

        repo.guardar(
            DatosPago(
                alias      = alias,
                titular    = binding.editTitular.text.toString().trim(),
                banco      = binding.editBanco.text.toString().trim(),
                mensajeTTS = binding.editMensajeTTS.text.toString().trim(),
                vozNombre  = vozNombre
            )
        )
        Toast.makeText(this, "Guardado ✓", Toast.LENGTH_SHORT).show()
    }

    private fun probarTTS() {
        guardar()
        val mensaje = repo.cargar().mensajeTTS
        hablar(mensaje)
    }

    private fun hablar(texto: String) {
        val vozNombre = repo.cargar().vozNombre
        if (vozNombre.isNotBlank()) {
            val voz = voicesDisponibles.firstOrNull { it.name == vozNombre }
            if (voz != null) tts?.voice = voz
        }
        tts?.speak(texto, TextToSpeech.QUEUE_FLUSH, null, "pagoinfo_tts")
    }

    override fun onDestroy() {
        unregisterReceiver(ttsReceiver)
        tts?.shutdown()
        super.onDestroy()
    }
}
