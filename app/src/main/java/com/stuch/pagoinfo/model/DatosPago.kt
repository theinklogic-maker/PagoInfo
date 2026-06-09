package com.stuch.pagoinfo.model

data class DatosPago(
    val alias: String = "",
    val titular: String = "",
    val banco: String = "",
    val mensajeTTS: String = "Buen día, gracias por su viaje. El alias de pago es: "
)
