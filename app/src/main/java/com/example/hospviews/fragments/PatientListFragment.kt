package com.example.hospviews.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hospviews.R
import com.example.hospviews.activities.PatientDetailActivity
import com.example.hospviews.adapters.PatientAdapter
import com.example.hospviews.interfaces.FragmentCommunication
import com.example.hospviews.models.Patient
import com.example.hospviews.utils.SharedPrefManager
import java.text.SimpleDateFormat
import java.util.*

class PatientListFragment : Fragment(), PatientAdapter.OnItemClickListener {

    private lateinit var rvPatients: RecyclerView
    private lateinit var btnAddPatient: Button
    private lateinit var adapter: PatientAdapter
    private lateinit var communication: FragmentCommunication

    // Sample patient data (replace with data from a database in a real app)
    private val patients = mutableListOf<Patient>()

    companion object {
        const val TAG = "PatientListFragment"

        fun getCurrentTime(): String {
            return SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()).format(Date())
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentCommunication) {
            communication = context
        } else {
            throw RuntimeException("$context must implement FragmentCommunication")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_patient_list, container, false)
        return view
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        setupViews(view)
//        setupRecyclerView()
//        // Add Patient Button click listener
//        val sharedPref = SharedPrefManager(requireContext())
//        sharedPref.saveTotalPatientsCount(patients.size) // Reset count
//
//        btnAddPatient.setOnClickListener {
//            showAddPatientDialog()
//        }
//
//        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in)) //Example Animation
//    }
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setupViews(view)

    val sharedPref = SharedPrefManager(requireContext())
    val savedPatients = sharedPref.getPatientList()
    patients.clear()
    patients.addAll(savedPatients)

    setupRecyclerView()

    sharedPref.saveTotalPatientsCount(patients.size)

    btnAddPatient.setOnClickListener {
        showAddPatientDialog()
    }

    view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
}


    private fun setupViews(view: View) {
        rvPatients = view.findViewById(R.id.rvPatients)
        btnAddPatient = view.findViewById(R.id.btnAddPatient)
    }

    private fun setupRecyclerView() {
        adapter = PatientAdapter(patients, this)
        rvPatients.adapter = adapter
        rvPatients.layoutManager = LinearLayoutManager(context)
    }

    private fun showAddPatientDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Add New Patient")

        val input = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_patient, null)

        val etName = input.findViewById<EditText>(R.id.etName)
        val etAge = input.findViewById<EditText>(R.id.etAge)
        val etRoom = input.findViewById<EditText>(R.id.etRoom)
        val etPhone = input.findViewById<EditText>(R.id.etPhone)

        builder.setView(input)

        builder.setPositiveButton("Add") { dialog, _ ->
            val name = etName.text.toString()
            val ageStr = etAge.text.toString()
            val room = etRoom.text.toString()
            val phone = etPhone.text.toString()

            if (name.isNotEmpty() && ageStr.isNotEmpty() && room.isNotEmpty() && phone.isNotEmpty()) {
                try {
                    val age = ageStr.toInt()
                    val newPatient = Patient(
                        id = generateUniqueId(), // Function to create a unique ID (see below)
                        name = name,
                        age = age,
                        status = "Waiting", // Default status
                        doctor = "Not Assigned", // Default doctor
                        checkInTime = getCurrentTime(),
                        room = room,
                        phoneNumber = phone
                    )

                    addPatient(newPatient)

                } catch (e: NumberFormatException) {
                    Toast.makeText(context, "Invalid age", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }
    override fun onResume() {
        super.onResume()
        val sharedPref = SharedPrefManager(requireContext())
        val savedPatients = sharedPref.getPatientList()
        patients.clear()
        patients.addAll(savedPatients)
        adapter.notifyDataSetChanged()
    }



    private fun addPatient(patient: Patient) {
    patients.add(patient)
    adapter.notifyItemInserted(patients.size - 1)
    rvPatients.scrollToPosition(patients.size - 1)

    val sharedPref = SharedPrefManager(requireContext())
    sharedPref.savePatientList(patients) // Save full updated list
    sharedPref.saveTotalPatientsCount(patients.size)

    val checkedInCount = sharedPref.getCheckedInCount()
    sharedPref.saveCheckedInCount(checkedInCount + 1)
}
    private fun generateUniqueId(): Int {
        return (System.currentTimeMillis() % 100000).toInt()
    }

    override fun onItemClick(patient: Patient) {
        // Launch the PatientDetailActivity
        val intent = Intent(context, PatientDetailActivity::class.java).apply {
            putExtra("PATIENT_ID", patient.id)
            putExtra("PATIENT_NAME", patient.name)
            putExtra("PATIENT_AGE", patient.age)
            putExtra("PATIENT_STATUS", patient.status)
            putExtra("PATIENT_DOCTOR", patient.doctor)
            putExtra("PATIENT_PHONE", patient.phoneNumber)  // Send phone number
        }
        startActivity(intent)
    }

    fun refreshData() {
        Log.d(TAG, "refreshData called")
        adapter.notifyDataSetChanged()
        Toast.makeText(context, "Patient list refreshed", Toast.LENGTH_SHORT).show()
    }
}