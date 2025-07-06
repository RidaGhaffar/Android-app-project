package com.example.hospviews.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hospviews.fragments.DashboardFragment
import com.example.hospviews.fragments.DoctorListFragment
import com.example.hospviews.fragments.PatientListFragment

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val fragments = listOf(
        DashboardFragment(),
        PatientListFragment(),
        DoctorListFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun refreshPatientFragment() {
        if (fragments.size > 1 && fragments[1] is PatientListFragment) {
            (fragments[1] as PatientListFragment).refreshData()
        }
    }
}