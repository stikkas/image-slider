package com.example.imageslider

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.imageslider.model.Slide

class PagerAdapter(
    activity: AppCompatActivity,
    private val slides: List<Int>
) : FragmentStateAdapter(activity) {
    override fun getItemCount() = slides.size

    override fun createFragment(position: Int): Fragment {
        return PageFragment.newInstance(position)
    }
}