package com.example.hospviews.fragments

import android.app.AlertDialog
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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hospviews.R
import com.example.hospviews.activities.DoctorDetailActivity
import com.example.hospviews.adapters.DoctorAdapter
import com.example.hospviews.interfaces.FragmentCommunication
import com.example.hospviews.models.Doctor
import com.example.hospviews.utils.SharedPrefManager

class DoctorListFragment : Fragment() {

    private lateinit var rvDoctors: RecyclerView
    private lateinit var btnAddDoctor: Button
    private lateinit var adapter: DoctorAdapter
    private lateinit var communication: FragmentCommunication

    private val doctors = mutableListOf<Doctor>() // Empty initially


    companion object {
        const val TAG = "DoctorListFragment"
    }

    // ===== FRAGMENT LIFECYCLE METHODS =====
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach called")

        // Fragment communication setup (if needed)
        if (context is FragmentCommunication) {
            communication = context
        } else {
            try {
                communication = context as FragmentCommunication
            } catch (e: ClassCastException) {
                throw ClassCastException("$context must implement FragmentCommunication")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_doctor_list, container, false)
    }

    fun refreshData() {
        Log.d(TAG, "refreshData called")
        // Example: update list or notify adapter (you can improve this)
        adapter.notifyDataSetChanged()
        Toast.makeText(context, "Doctor list refreshed", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        val sharedPref = SharedPrefManager(requireContext())
        val savedDocs = sharedPref.getDoctorList()
        doctors.clear()
        doctors.addAll(savedDocs)
        sharedPref.saveTotalDoctorsCount(doctors.size) // Add this too

        adapter.notifyDataSetChanged()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPrefManager = SharedPrefManager(requireContext())
        doctors.addAll(sharedPrefManager.getDoctorList())
        rvDoctors = view.findViewById(R.id.rvDoctors)
        btnAddDoctor = view.findViewById(R.id.btnAddDoctor)

        // Set up RecyclerView
        rvDoctors.layoutManager = LinearLayoutManager(context)

        //Initialize the adapter

        adapter = DoctorAdapter(doctors) { doctor ->
            val intent = Intent(requireContext(), DoctorDetailActivity::class.java).apply {
                putExtra("DOCTOR_NAME", doctor.name)
                putExtra("DOCTOR_SPECIALIZATION", doctor.specialization)
            }
            startActivity(intent)
        }
        rvDoctors.adapter = adapter

        // Set up button click listener
        btnAddDoctor.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_add_doctor, null)
            val etName = dialogView.findViewById<EditText>(R.id.etDoctorName)
            val etSpecialization = dialogView.findViewById<EditText>(R.id.etSpecialization)

            AlertDialog.Builder(requireContext())
                .setTitle("Add New Doctor")
                .setView(dialogView)
                .setPositiveButton("Add") { _, _ ->
                    val name = etName.text.toString()
                    val specialization = etSpecialization.text.toString()

                    val newDoctor = Doctor(
                        id = doctors.size + 1,
                        name = name,
                        specialization = specialization,
                        isAvailable = true
                    )

                    doctors.add(newDoctor)
                    adapter.notifyItemInserted(doctors.size - 1)

                    // Save to SharedPrefs
                    sharedPrefManager.saveDoctorList(doctors)
                    sharedPrefManager.saveTotalDoctorsCount(doctors.size)
                    Toast.makeText(context, "Doctor Added", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }


        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
    }
}