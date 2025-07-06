package com.example.hospviews.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.hospviews.R
import com.example.hospviews.utils.SharedPrefManager

class DashboardFragment : Fragment() {

    private lateinit var tvTotalPatients: TextView
    private lateinit var tvTotalDoctors: TextView
    private lateinit var tvCheckedIn: TextView
    private lateinit var tvCheckedOut: TextView
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var tvWaiting: TextView
    private lateinit var tvUnderObservation: TextView
    private lateinit var tvReadyForDischarge: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        return view
    }
    override fun onResume() {
        super.onResume()
        updateDashboardData()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTotalPatients = view.findViewById(R.id.tvTotalPatients)
        tvTotalDoctors = view.findViewById(R.id.tvTotalDoctors)
        tvCheckedIn = view.findViewById(R.id.tvCheckedIn)
        tvCheckedOut = view.findViewById(R.id.tvCheckedOut)
        tvUnderObservation = view.findViewById(R.id.tvUnderObservation)
        tvReadyForDischarge = view.findViewById(R.id.tvReadyForDischarge)

        sharedPrefManager = SharedPrefManager(requireContext())



    }

    private fun updateDashboardData() {
        val totalPatients = sharedPrefManager.getTotalPatientsCount()
        val totalDoctors = sharedPrefManager.getTotalDoctorsCount()
        val inTreatment = sharedPrefManager.getPatientStatusCount("In Treatment")
        val underObservation = sharedPrefManager.getPatientStatusCount("Under Observation")
        val readyForDischarge = sharedPrefManager.getPatientStatusCount("Ready for Discharge")
        val checkedOut = sharedPrefManager.getPatientStatusCount("Checked Out")

        tvTotalPatients.text = totalPatients.toString()
        tvTotalDoctors.text = totalDoctors.toString()
        tvCheckedIn.text = inTreatment.toString()
        tvUnderObservation.text = underObservation.toString()
        tvReadyForDischarge.text = readyForDischarge.toString()
        tvCheckedOut.text = checkedOut.toString()
    }
}