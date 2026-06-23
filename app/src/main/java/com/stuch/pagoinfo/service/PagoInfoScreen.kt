package com.stuch.pagoinfo.service

import androidx.car.app.CarContext
import androidx.car.app.CarToast
import androidx.car.app.Screen
import androidx.car.app.model.*
import com.stuch.pagoinfo.model.PagoRepository

class PagoInfoScreen(carContext: CarContext) : Screen(carContext) {

    // Usar carContext directamente (no .applicationContext) para acceder
    // a las SharedPreferences del proceso de PagoInfo correctamente
    private val repo by lazy { PagoRepository.getInstance(carContext) }

    override fun onGetTemplate(): Template {
        return try {
            buildTemplate()
        } catch (e: Exception) {
            errorTemplate()
        }
    }

    private fun errorTemplate() = ListTemplate.Builder()
        .setTitle("PagoInfo")
        .setHeaderAction(Action.APP_ICON)
        .setSingleList(
            ItemList.Builder()
                .addItem(Row.Builder()
                    .setTitle("Error al cargar")
                    .addText("Abri PagoInfo en el telefono y volve a intentar.")
                    .build())
                .build()
        ).build()

    private fun buildTemplate(): Template {
        val datos = repo.cargar()
        val rowsBuilder = ItemList.Builder()

        if (!repo.hayDatosConfigurados()) {
            rowsBuilder.addItem(Row.Builder()
                .setTitle("Sin configurar")
                .addText("Abri PagoInfo en el telefono para configurar los datos de pago.")
                .build())
        } else {
            rowsBuilder.addItem(Row.Builder()
                .setTitle(datos.alias)
                .addText("Alias de transferencia")
                .build())
            if (datos.titular.isNotBlank()) {
                rowsBuilder.addItem(Row.Builder()
                    .setTitle(datos.titular)
                    .addText("Titular")
                    .build())
            }
            if (datos.banco.isNotBlank()) {
                rowsBuilder.addItem(Row.Builder()
                    .setTitle(datos.banco)
                    .addText("Banco")
                    .build())
            }
        }

        return ListTemplate.Builder()
            .setTitle("Informacion de transferencia")
            .setHeaderAction(Action.APP_ICON)
            .setSingleList(rowsBuilder.build())
            .setActionStrip(ActionStrip.Builder()
                .addAction(Action.Builder()
                    .setTitle("Leer")
                    .setOnClickListener { leerEnVozAlta() }
                    .build())
                .addAction(Action.Builder()
                    .setTitle("Actualizar")
                    .setOnClickListener {
                        invalidate()
                        CarToast.makeText(carContext, "Actualizado", CarToast.LENGTH_SHORT).show()
                    }
                    .build())
                .build())
            .build()
    }

    private fun leerEnVozAlta() {
        try {
            val mensaje = repo.cargar().mensajeTTS
            if (mensaje.isBlank()) {
                CarToast.makeText(carContext, "Configura el mensaje en el telefono", CarToast.LENGTH_LONG).show()
                return
            }
            val intent = android.content.Intent("com.stuch.pagoinfo.TTS_SPEAK").apply {
                putExtra("mensaje", mensaje)
                setPackage(carContext.packageName)
            }
            carContext.applicationContext.sendBroadcast(intent)
            CarToast.makeText(carContext, "Leyendo...", CarToast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            CarToast.makeText(carContext, "Error al reproducir", CarToast.LENGTH_SHORT).show()
        }
    }
}
