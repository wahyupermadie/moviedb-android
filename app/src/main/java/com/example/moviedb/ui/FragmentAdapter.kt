package com.example.moviedb.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

class FragmentAdapter (fm : FragmentManager, fragmentList: ArrayList<Fragment>) : FragmentStatePagerAdapter(fm){

    private var fragments = ArrayList<Fragment>()

    init {
        fragments = fragmentList
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        0 -> "Popular"
        1 -> "Top Rated"
        else -> "Favorite"
    }

    override fun getCount(): Int = 3
}