package com.example.hospviews.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.hospviews.R
import com.example.hospviews.models.Doctor

class DoctorAdapter(
    private val doctors: List<Doctor>,
    private val onDoctorClick: (Doctor) -> Unit
) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    inner class DoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: CardView = itemView.findViewById(R.id.cardDoctor)
        private val tvName: TextView = itemView.findViewById(R.id.tvDoctorName)
        private val tvSpecialization: TextView = itemView.findViewById(R.id.tvSpecialization)

        fun bind(doctor: Doctor) {
            tvName.text = doctor.name
            tvSpecialization.text = "Specialization: ${doctor.specialization}"

            // Set availability color
            val availabilityColor = if (doctor.isAvailable) R.color.green else R.color.red

            cardView.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.button_click))
                onDoctorClick(doctor)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_doctor, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        holder.bind(doctors[position])
        holder.itemView.startAnimation(
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_in_left)
        )
    }

    override fun getItemCount(): Int = doctors.size
}