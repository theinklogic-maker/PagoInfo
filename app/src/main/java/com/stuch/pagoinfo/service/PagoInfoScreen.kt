package com.stuch.pagoinfo.service

import androidx.car.app.CarContext
import androidx.car.app.CarToast
import androidx.car.app.Screen
import androidx.car.app.model.*
import com.stuch.pagoinfo.model.PagoRepository

class PagoInfoScreen(carContext: CarContext) : Screen(carContext) {

    private val repo = PagoRepository.getInstance(carContext)

    override fun onGetTemplate(): Template {
        val datos = repo.cargar()

        if (!repo.hayDatosConfigurados()) {
            return MessageTemplate.Builder(
                "No hay datos configurados.\n\nAbrí PagoInfo en tu teléfono para ingresar el alias y los datos de pago."
            )
                .setTitle("PagoInfo")
                .setHeaderAction(Action.APP_ICON)
                .build()
        }

        val rowsBuilder = ItemList.Builder()

        rowsBuilder.addItem(
            Row.Builder()
                .setTitle(datos.alias)
                .addText("Alias de transferencia")
                .build()
        )

        if (datos.titular.isNotBlank()) {
            rowsBuilder.addItem(
                Row.Builder()
                    .setTitle(datos.titular)
                    .addText("Titular")
                    .build()
            )
        }

        if (datos.banco.isNotBlank()) {
            rowsBuilder.addItem(
                Row.Builder()
                    .setTitle(datos.banco)
                    .addText("Banco")
                    .build()
            )
        }

        val accionLeer = Action.Builder()
            .setTitle("Leer")
            .setOnClickListener { leerEnVozAlta() }
            .build()

        val accionActualizar = Action.Builder()
            .setTitle("Actualizar")
            .setOnClickListener {
                invalidate()
                CarToast.makeText(carContext, "Datos actualizados", CarToast.LENGTH_SHORT).show()
            }
            .build()

        return ListTemplate.Builder()
            .setTitle("Información de transferencia")
            .setHeaderAction(Action.APP_ICON)
            .setSingleList(rowsBuilder.build())
            .setActionStrip(
                ActionStrip.Builder()
                    .addAction(accionLeer)
                    .addAction(accionActualizar)
                    .build()
            )
            .build()
    }

    private fun leerEnVozAlta() {
        val datos = repo.cargar()
        val mensaje = buildString {
            append(datos.mensajeTTS.trimEnd())
            if (datos.alias.isNotBlank())   append(" ${datos.alias}.")
            if (datos.banco.isNotBlank())   append(" Banco ${datos.banco}.")
            if (datos.titular.isNotBlank()) append(" A nombre de ${datos.titular}.")
        }
        val intent = android.content.Intent("com.stuch.pagoinfo.TTS_SPEAK").apply {
            putExtra("mensaje", mensaje)
            setPackage(carContext.packageName)
        }
        carContext.sendBroadcast(intent)
        CarToast.makeText(carContext, datos.alias, CarToast.LENGTH_LONG).show()
    }
}
