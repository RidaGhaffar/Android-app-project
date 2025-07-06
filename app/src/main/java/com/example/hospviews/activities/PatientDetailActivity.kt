package com.example.hospviews.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.hospviews.R
import com.example.hospviews.utils.SharedPrefManager

class PatientDetailActivity : AppCompatActivity() {

    private lateinit var tvPatientName: TextView
    private lateinit var tvPatientAge: TextView
    private lateinit var tvPatientStatus: TextView
    private lateinit var tvAssignedDoctor: TextView
    private lateinit var tvPatientPhone: TextView  // Add TextView for phone number
    private lateinit var btnCheckOut: Button
    private lateinit var btnAssignDoctor: Button
    private lateinit var btnEditStatus: Button
    private lateinit var btnCall: Button

    private lateinit var sharedPref: SharedPrefManager
    private var patientId: Int = 0
    private var patientName: String = ""
    private var patientAge: Int = 0
    private var patientStatus: String = ""
    private var assignedDoctor: String = ""
    private var patientPhone: String = "" // Add variable for phone number

    companion object {
        const val TAG = "PatientDetailActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")

        setContentView(R.layout.activity_patient_detail)

        sharedPref = SharedPrefManager(this)

        setupViews()
        getIntentData()
        displayPatientInfo()
        setupClickListeners()
    }

    private fun setupViews() {
        tvPatientName = findViewById(R.id.tvPatientName)
        tvPatientAge = findViewById(R.id.tvPatientAge)
        tvPatientStatus = findViewById(R.id.tvPatientStatus)
        tvAssignedDoctor = findViewById(R.id.tvAssignedDoctor)
        tvPatientPhone = findViewById(R.id.tvPatientPhone) // Initialize TextView
        btnCheckOut = findViewById(R.id.btnCheckOut)
        btnAssignDoctor = findViewById(R.id.btnAssignDoctor)
        btnEditStatus = findViewById(R.id.btnEditStatus)
        btnCall = findViewById(R.id.btnCall)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Patient Details"
    }
    private fun updatePatientInList() {
        val patients = sharedPref.getPatientList().toMutableList()
        val index = patients.indexOfFirst { it.id == patientId }
        if (index != -1) {
            val updatedPatient = patients[index].copy(
                status = patientStatus,
                doctor = assignedDoctor
            )
            patients[index] = updatedPatient
            sharedPref.savePatientList(patients)
        }
    }


    private fun getIntentData() {
        patientId = intent.getIntExtra("PATIENT_ID", 0)
        patientName = intent.getStringExtra("PATIENT_NAME") ?: ""
        patientAge = intent.getIntExtra("PATIENT_AGE", 0)

        // Load latest status from SharedPref if available
        val savedStatus = sharedPref.getPatientStatus(patientId)
        patientStatus = savedStatus ?: intent.getStringExtra("PATIENT_STATUS") ?: ""

        // Load latest doctor from SharedPref if available
        val savedDoctor = sharedPref.getPatientDoctor(patientId)
        assignedDoctor = savedDoctor ?: intent.getStringExtra("PATIENT_DOCTOR") ?: "Not Assigned"

        patientPhone = intent.getStringExtra("PATIENT_PHONE") ?: ""
    }

    private fun displayPatientInfo() {
        tvPatientName.text = patientName
        tvPatientAge.text = "Age: $patientAge"
        tvPatientStatus.text = "Status: $patientStatus"
        tvAssignedDoctor.text = "Doctor: $assignedDoctor"
        tvPatientPhone.text = "Phone: $patientPhone" // Display phone number

        // Update button based on status
        if (patientStatus == "Checked Out") {
            btnCheckOut.text = "Check In"
            btnCheckOut.setBackgroundColor(getColor(androidx.appcompat.R.color.abc_color_highlight_material))
        }
    }

    private fun setupClickListeners() {
        btnCheckOut.setOnClickListener {
            handleCheckInOut()
        }

        btnAssignDoctor.setOnClickListener {
            showDoctorAssignmentDialog()
        }

        btnEditStatus.setOnClickListener {
            showStatusEditDialog()
        }

        // Implicit Intent - Call functionality
        btnCall.setOnClickListener {
            showCallDialog()
        }
    }

    private fun handleCheckInOut() {
        val newStatus = if (patientStatus == "Checked Out") "Checked In" else "Checked Out"

        AlertDialog.Builder(this)
            .setTitle("Confirm Action")
            .setMessage("Are you sure you want to ${if (newStatus == "Checked In") "check in" else "check out"} $patientName?")
            .setPositiveButton("Yes") { _, _ ->
                patientStatus = newStatus
                displayPatientInfo()
                Toast.makeText(this, "$patientName has been $newStatus", Toast.LENGTH_SHORT).show()

                // Save to SharedPreferences
                sharedPref.savePatientStatus(patientId, newStatus)
                updatePatientInList()

            }
            .setNegativeButton("No", null)
            .show()

    }

    private fun showDoctorAssignmentDialog() {
        val doctors = sharedPref.getDoctorList()

        if (doctors.isEmpty()) {
            Toast.makeText(this, "No doctors available to assign", Toast.LENGTH_SHORT).show()
            return
        }

        // Extract doctor names for display
        val doctorNames = doctors.map { it.name }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Assign Doctor")
            .setItems(doctorNames) { _, which ->
                assignedDoctor = doctorNames[which]
                tvAssignedDoctor.text = "Doctor: $assignedDoctor"
                Toast.makeText(this, "Doctor assigned: $assignedDoctor", Toast.LENGTH_SHORT).show()

                // Save to SharedPreferences
                sharedPref.savePatientDoctor(patientId, assignedDoctor)
                updatePatientInList()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }



    private fun showStatusEditDialog() {
        val statuses = arrayOf("Waiting", "In Treatment", "Under Observation", "Ready for Discharge", "Checked Out")

        AlertDialog.Builder(this)
            .setTitle("Update Status")
            .setItems(statuses) { _, which ->
                patientStatus = statuses[which]
                displayPatientInfo()
                Toast.makeText(this, "Status updated: $patientStatus", Toast.LENGTH_SHORT).show()

                // Save to SharedPreferences
                sharedPref.savePatientStatus(patientId, patientStatus)
                updatePatientInList()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showCallDialog() {
        //val input = EditText(this)
        //input.hint = "Enter phone number"
        //input.inputType = android.text.InputType.TYPE_CLASS_PHONE

        AlertDialog.Builder(this)
            .setTitle("Call Patient")
            //.setView(input)
            .setMessage("Call ${tvPatientName.text}?")
            .setPositiveButton("Call") { _, _ ->
                //val phoneNumber = input.text.toString()
                if (patientPhone.isNotEmpty()) {
                    // Implicit Intent - Make a call
                    val callIntent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:$patientPhone")
                    }
                    startActivity(callIntent)
                } else {
                    Toast.makeText(this, "No phone number available", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.patient_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_share -> {
                // Implicit Intent - Share patient info
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "Patient: $patientName\nAge: $patientAge\nStatus: $patientStatus\nDoctor: $assignedDoctor\nPhone: $patientPhone")
                }
                startActivity(Intent.createChooser(shareIntent, "Share Patient Info"))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}