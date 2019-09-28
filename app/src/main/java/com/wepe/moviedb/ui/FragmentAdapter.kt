package com.wepe.moviedb.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.wepe.moviedb.R
import java.util.*

class FragmentAdapter (fm : FragmentManager, fragmentList: ArrayList<Fragment>, val context: Context) : FragmentStatePagerAdapter(fm){

    private var fragments = ArrayList<Fragment>()

    init {
        fragments = fragmentList
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        0 -> context.getString(R.string.popular)
        1 -> context.getString(R.string.top_rated)
        2 -> context.getString(R.string.tv_show)
        else -> context.getString(R.string.favorite)
    }

    override fun getCount(): Int = 4
}