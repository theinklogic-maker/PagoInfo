package com.stuch.pagoinfo.model

import android.content.Context
import android.content.SharedPreferences

class PagoRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("pagoinfo_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_ALIAS   = "alias"
        private const val KEY_TITULAR = "titular"
        private const val KEY_BANCO   = "banco"
        private const val KEY_TTS     = "mensaje_tts"

        @Volatile
        private var INSTANCE: PagoRepository? = null

        fun getInstance(context: Context): PagoRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: PagoRepository(context.applicationContext).also { INSTANCE = it }
            }
    }

    fun guardar(datos: DatosPago) {
        prefs.edit()
            .putString(KEY_ALIAS,   datos.alias)
            .putString(KEY_TITULAR, datos.titular)
            .putString(KEY_BANCO,   datos.banco)
            .putString(KEY_TTS,     datos.mensajeTTS)
            .apply()
    }

    fun cargar(): DatosPago = DatosPago(
        alias      = prefs.getString(KEY_ALIAS,   "") ?: "",
        titular    = prefs.getString(KEY_TITULAR, "") ?: "",
        banco      = prefs.getString(KEY_BANCO,   "") ?: "",
        mensajeTTS = prefs.getString(KEY_TTS,
            "Buen día, gracias por su viaje. El alias de pago es: ") ?: ""
    )

    fun hayDatosConfigurados(): Boolean = cargar().alias.isNotBlank()
}
