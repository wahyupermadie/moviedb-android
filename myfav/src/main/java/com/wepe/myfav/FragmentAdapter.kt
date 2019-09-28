package com.wepe.myfav

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.ArrayList

class FragmentAdapter (fm : FragmentManager, fragmentList: ArrayList<Fragment>, val context: Context) : FragmentStatePagerAdapter(fm){

    private var fragments = ArrayList<Fragment>()

    init {
        fragments = fragmentList
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        0 -> context.getString(R.string.movies)
        else -> context.getString(R.string.tv)
    }

    override fun getCount(): Int = 2
}