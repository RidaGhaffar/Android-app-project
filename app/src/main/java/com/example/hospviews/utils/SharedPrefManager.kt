    package com.example.hospviews.utils

    import android.content.Context
    import android.content.SharedPreferences
    import com.example.hospviews.models.Doctor
    import com.example.hospviews.models.Patient
    import com.google.gson.Gson
    import com.google.gson.reflect.TypeToken

    class SharedPrefManager(context: Context) {

        private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        companion object {
            private const val PREF_NAME = "hospital_management_prefs"
            private const val KEY_LAST_TAB = "last_opened_tab"
            private const val KEY_PATIENT_STATUS = "patient_status_"
            private const val KEY_PATIENT_DOCTOR = "patient_doctor_"
            private const val KEY_FIRST_LAUNCH = "first_launch"

            // New keys for dashboard data
            private const val KEY_TOTAL_PATIENTS = "total_patients"
            private const val KEY_TOTAL_DOCTORS = "total_doctors"
            private const val KEY_CHECKED_IN = "checked_in"
            private const val KEY_CHECKED_OUT = "checked_out"
        }

        fun saveLastOpenedTab(tabIndex: Int) {
            prefs.edit().putInt(KEY_LAST_TAB, tabIndex).apply()
        }

        fun getLastOpenedTab(): Int {
            return prefs.getInt(KEY_LAST_TAB, 0)
        }

        fun savePatientStatus(patientId: Int, status: String) {
            prefs.edit().putString("$KEY_PATIENT_STATUS$patientId", status).apply()
        }

        fun getPatientStatus(patientId: Int): String? {
            return prefs.getString("$KEY_PATIENT_STATUS$patientId", null)
        }

        fun savePatientDoctor(patientId: Int, doctor: String) {
            prefs.edit().putString("$KEY_PATIENT_DOCTOR$patientId", doctor).apply()
        }
        fun getPatientStatusCount(targetStatus: String): Int {
            val allEntries = prefs.all
            return allEntries.count { (key, value) ->
                key.startsWith(KEY_PATIENT_STATUS) && value == targetStatus
            }
        }
        fun saveDoctorList(doctors: List<Doctor>) {
            val gson = Gson()
            val json = gson.toJson(doctors)
            prefs.edit().putString("doctor_list", json).apply()
        }

        fun getDoctorList(): List<Doctor> {
            val gson = Gson()
            val json = prefs.getString("doctor_list", null)
            val type = object : TypeToken<List<Doctor>>() {}.type
            return if (json != null) gson.fromJson(json, type) else emptyList()
        }



        fun getPatientDoctor(patientId: Int): String? {
            return prefs.getString("$KEY_PATIENT_DOCTOR$patientId", null)
        }

        fun isFirstLaunch(): Boolean {
            return prefs.getBoolean(KEY_FIRST_LAUNCH, true)
        }

        fun setFirstLaunchComplete() {
            prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()
        }
        fun savePatientList(patients: List<Patient>) {
            val gson = Gson()
            val json = gson.toJson(patients)
            prefs.edit().putString("patient_list", json).apply()
        }

        fun getPatientList(): List<Patient> {
            val gson = Gson()
            val json = prefs.getString("patient_list", null)
            val type = object : TypeToken<List<Patient>>() {}.type
            return if (json != null) gson.fromJson(json, type) else emptyList()
        }

        // ======= DASHBOARD DATA METHODS =======

        fun saveTotalPatientsCount(count: Int) {
            prefs.edit().putInt(KEY_TOTAL_PATIENTS, count).apply()
        }

        fun getTotalPatientsCount(): Int {
            return prefs.getInt(KEY_TOTAL_PATIENTS, 0) // Default to 0 if not found
        }

        fun saveTotalDoctorsCount(count: Int) {
            prefs.edit().putInt(KEY_TOTAL_DOCTORS, count).apply()
        }

        fun getTotalDoctorsCount(): Int {
            return prefs.getInt(KEY_TOTAL_DOCTORS, 0) // Default to 0 if not found
        }

        fun saveCheckedInCount(count: Int) {
            prefs.edit().putInt(KEY_CHECKED_IN, count).apply()
        }

        fun getCheckedInCount(): Int {
            return prefs.getInt(KEY_CHECKED_IN, 0) // Default to 0 if not found
        }

        fun saveCheckedOutCount(count: Int) {
            prefs.edit().putInt(KEY_CHECKED_OUT, count).apply()
        }

        fun getCheckedOutCount(): Int {
            return prefs.getInt(KEY_CHECKED_OUT, 0) // Default to 0 if not found
        }
    }