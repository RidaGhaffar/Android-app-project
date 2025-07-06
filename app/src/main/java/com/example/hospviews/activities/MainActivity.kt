package com.example.hospviews.activities
    import android.content.Intent
    import android.os.Bundle
    import android.util.Log
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import androidx.viewpager2.widget.ViewPager2
    import com.example.hospviews.R
    import com.example.hospviews.adapters.ViewPagerAdapter
    import com.example.hospviews.interfaces.FragmentCommunication
    import com.example.hospviews.models.Doctor
    import com.example.hospviews.models.Patient
    import com.example.hospviews.utils.SharedPrefManager

    import com.google.android.material.tabs.TabLayout
    import com.google.android.material.tabs.TabLayoutMediator

    class MainActivity : AppCompatActivity(), FragmentCommunication {

        private lateinit var viewPager: ViewPager2
        private lateinit var tabLayout: TabLayout
        private lateinit var sharedPref: SharedPrefManager
        private lateinit var adapter: ViewPagerAdapter

        companion object {
            const val TAG = "com.example.hospviews.activities.MainActivity"
        }

        // ===== ACTIVITY LIFECYCLE METHODS =====
        override fun onCreate(savedInstanceState: Bundle?) {

            super.onCreate(savedInstanceState)
            window.decorView.systemUiVisibility = android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
            actionBar?.hide() // only if you're using native ActionBar
            supportActionBar?.hide()
            Log.d(TAG, "onCreate called")

            setContentView(R.layout.activity_main)

            // Initialize SharedPreferences
            sharedPref = SharedPrefManager(this)

            setupViews()
            setupViewPager()

            Toast.makeText(this, "Hospital Management System Started", Toast.LENGTH_SHORT).show()
        }

        override fun onStart() {
            super.onStart()
            Log.d(TAG, "onStart called")
        }

        override fun onResume() {
            super.onResume()
            Log.d(TAG, "onResume called")
            // Restore last opened tab
            viewPager.currentItem = sharedPref.getLastOpenedTab()
        }

        override fun onPause() {
            super.onPause()
            Log.d(TAG, "onPause called")
            // Save current tab position
            sharedPref.saveLastOpenedTab(tabLayout.selectedTabPosition)
        }

        override fun onStop() {
            super.onStop()
            Log.d(TAG, "onStop called")
        }

        override fun onDestroy() {
            super.onDestroy()
            Log.d(TAG, "onDestroy called")
        }

        override fun onRestart() {
            super.onRestart()
            Log.d(TAG, "onRestart called")
        }

        private fun setupViews() {
            viewPager = findViewById(R.id.viewPager)
            tabLayout = findViewById(R.id.tabLayout)
        }

        private fun setupViewPager() {
            adapter = ViewPagerAdapter(this)
            viewPager.adapter = adapter

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "Dashboard"
                    1 -> "Patients"
                    2 -> "Doctors"
                    else -> "Tab"
                }
            }.attach()
        }

        // ===== FRAGMENT COMMUNICATION INTERFACE IMPLEMENTATION =====
        override fun onPatientSelected(patient: Patient) {
            // Explicit Intent to PatientDetailActivity
            val intent = Intent(this, PatientDetailActivity::class.java).apply {
                putExtra("PATIENT_ID", patient.id)
                putExtra("PATIENT_NAME", patient.name)
                putExtra("PATIENT_AGE", patient.age)
                putExtra("PATIENT_STATUS", patient.status)
//                putExtra("PATIENT_DOCTOR", patient.assignedDoctor)
            }
            startActivity(intent)
        }

        override fun onDoctorSelected(doctor: Doctor) {
            // Explicit Intent to DoctorDetailActivity
            val intent = Intent(this, DoctorDetailActivity::class.java).apply {
                putExtra("DOCTOR_ID", doctor.id)
                putExtra("DOCTOR_NAME", doctor.name)
                putExtra("DOCTOR_SPECIALIZATION", doctor.specialization)
            }
            startActivity(intent)
        }

        override fun refreshPatientList() {
            // Communication between fragments
            adapter.refreshPatientFragment()
        }
    }
