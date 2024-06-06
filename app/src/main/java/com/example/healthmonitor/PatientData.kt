package com.example.healthmonitor

import java.io.Serializable

data class PatientData(
    var id : Int = 1,
    var name : String = "name",
    var age: Int = 30,
    var imgResId :Int =R.drawable.patient) : Serializable
