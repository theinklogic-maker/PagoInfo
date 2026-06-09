package com.stuch.pagoinfo.service

import android.content.Intent
import androidx.car.app.Screen
import androidx.car.app.Session

class PagoInfoSession : Session() {

    override fun onCreateScreen(intent: Intent): Screen {
        return PagoInfoScreen(carContext)
    }
}
