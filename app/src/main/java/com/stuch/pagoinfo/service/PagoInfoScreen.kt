package com.stuch.pagoinfo.service

import android.os.Handler
import android.os.Looper
import androidx.car.app.CarContext
import androidx.car.app.CarToast
import androidx.car.app.Screen
import androidx.car.app.model.*
import com.stuch.pagoinfo.model.PagoRepository

class PagoInfoScreen(carContext: CarContext) : Screen(carContext) {

    private val repo = PagoRepository.getInstance(carContext.applicationContext)
    private val handler = Handler(Looper.getMainLooper())

    override fun onGetTemplate(): Template {
        return try { buildTemplate() } catch (e: Exception) { errorTemplate() }
    }

    private fun errorTemplate() = ListTemplate.Builder()
        .setTitle("PagoInfo")
        .setHeaderAction(Action.APP_ICON)
        .setSingleList(ItemList.Builder()
            .addItem(Row.Builder().setTitle("Error al cargar").addText("Abrí PagoInfo en el teléfono y volvé a intentar.").build())
            .build())
        .build()

    private fun buildTemplate(): Template {
        val datos = repo.cargar()
        val rowsBuilder = ItemList.Builder()

        if (!repo.hayDatosConfigurados()) {
            rowsBuilder.addItem(Row.Builder().setTitle("Sin configurar").addText("Abrí PagoInfo en el teléfono para configurar los datos de pago.").build())
        } else {
            rowsBuilder.addItem(Row.Builder().setTitle(datos.alias).addText("Alias de transferencia").build())
            if (datos.titular.isNotBlank()) rowsBuilder.addItem(Row.Builder().setTitle(datos.titular).addText("Titular").build())
            if (datos.banco.isNotBlank()) rowsBuilder.addItem(Row.Builder().setTitle(datos.banco).addText("Banco").build())
        }

        return ListTemplate.Builder()
            .setTitle("Información de transferencia")
            .setHeaderAction(Action.APP_ICON)
            .setSingleList(rowsBuilder.build())
            .setActionStrip(ActionStrip.Builder()
                .addAction(Action.Builder().setTitle("Leer").setOnClickListener { leerEnVozAlta() }.build())
                .addAction(Action.Builder().setTitle("Actualizar").setOnClickListener { invalidate(); CarToast.makeText(carContext, "Actualizado", CarToast.LENGTH_SHORT).show() }.build())
                .build())
            .build()
    }

    private fun leerEnVozAlta() {
        try {
            val mensaje = repo.cargar().mensajeTTS
            if (mensaje.isBlank()) { CarToast.makeText(carContext, "Configurá el mensaje en el teléfono", CarToast.LENGTH_LONG).show(); return }
            val intent = android.content.Intent("com.stuch.pagoinfo.TTS_SPEAK").apply { putExtra("mensaje", mensaje); setPackage("com.stuch.pagoinfo.debug") }
            carContext.applicationContext.sendBroadcast(intent)
            CarToast.makeText(carContext, "Leyendo...", CarToast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            CarToast.makeText(carContext, "Error al reproducir", CarToast.LENGTH_SHORT).show()
        }
    }
}