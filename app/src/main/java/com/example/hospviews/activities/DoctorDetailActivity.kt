package com.example.hospviews.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.hospviews.R
import com.example.hospviews.utils.SharedPrefManager

class DoctorDetailActivity : AppCompatActivity() {

    private lateinit var tvDoctorName: TextView
    private lateinit var tvSpecialization: TextView
    private lateinit var btnRemove: Button
    private lateinit var sharedPrefManager: SharedPrefManager

    private var doctorName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_detail)

        sharedPrefManager = SharedPrefManager(this)

        setupViews()
        displayDoctorInfo()
        setupClickListeners()
    }

    private fun setupViews() {
        tvDoctorName = findViewById(R.id.tvDoctorName)
        tvSpecialization = findViewById(R.id.tvSpecialization)
        btnRemove = findViewById(R.id.btnRemove)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Doctor Details"
    }

    private fun displayDoctorInfo() {
        doctorName = intent.getStringExtra("DOCTOR_NAME") ?: ""
        val specialization = intent.getStringExtra("DOCTOR_SPECIALIZATION") ?: ""

        tvDoctorName.text = doctorName
        tvSpecialization.text = "Specialization: $specialization"
    }

    private fun setupClickListeners() {
        btnRemove.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Remove Doctor")
                .setMessage("Are you sure you want to remove Dr. $doctorName?")
                .setPositiveButton("Yes") { _, _ ->
                    val updatedList = sharedPrefManager.getDoctorList().filter {
                        it.name != doctorName
                    }
                    sharedPrefManager.saveDoctorList(updatedList)
                    Toast.makeText(this, "Doctor removed", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
