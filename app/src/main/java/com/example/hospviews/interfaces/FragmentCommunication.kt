package com.example.hospviews.interfaces

import com.example.hospviews.models.Doctor
import com.example.hospviews.models.Patient


interface FragmentCommunication {
    fun onPatientSelected(patient: Patient)
    fun onDoctorSelected(doctor: Doctor)
    fun refreshPatientList()
}
