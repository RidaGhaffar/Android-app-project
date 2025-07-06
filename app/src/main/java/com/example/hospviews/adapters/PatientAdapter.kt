package com.example.hospviews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.hospviews.R
import com.example.hospviews.models.Patient

class PatientAdapter(
    private val patients: MutableList<Patient>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(patient: Patient)
    }

    inner class PatientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPatientName: TextView = itemView.findViewById(R.id.tvPatientName)
        val tvPatientAge: TextView = itemView.findViewById(R.id.tvPatientAge)
        val tvPatientStatus: TextView = itemView.findViewById(R.id.tvPatientStatus)
        val tvPatientDoctor: TextView = itemView.findViewById(R.id.tvPatientDoctor)
        val tvPatientRoom: TextView = itemView.findViewById(R.id.tvPatientRoom)
        val tvCheckInTime: TextView = itemView.findViewById(R.id.tvCheckInTime)
        val cardPatient: CardView = itemView.findViewById(R.id.cardPatient) //Add CardView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_patient, parent, false)
        return PatientViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val patient = patients[position]

        holder.tvPatientName.text = patient.name
        holder.tvPatientAge.text = "Age: ${patient.age}"
        holder.tvPatientStatus.text = "Status: ${patient.status}"
        holder.tvPatientDoctor.text = "Doctor: ${patient.doctor}"
        holder.tvPatientRoom.text = "Room: ${patient.room}"
        holder.tvCheckInTime.text = "Check-in: ${patient.checkInTime}"

        holder.cardPatient.setOnClickListener { // Set click listener on the card
            listener.onItemClick(patient)
        }
    }

    override fun getItemCount() = patients.size
}