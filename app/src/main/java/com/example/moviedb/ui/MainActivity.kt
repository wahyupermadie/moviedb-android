package com.example.moviedb.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.moviedb.R
import com.example.moviedb.ui.favorite.FavoriteFragment
import com.example.moviedb.ui.popular.PopularFragment
import com.example.moviedb.ui.toprated.TopRatedFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(){
    lateinit var mAdapter: FragmentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupFragment()
        initUi()
    }

    private fun initUi() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Popular Movies"
        view_pager.setCurrentItem(0, true)
    }

    private fun setupFragment() {
        val fragmentList: ArrayList<Fragment> = arrayListOf()
        fragmentList.add(PopularFragment.instance())
        fragmentList.add(TopRatedFragment.instance())
        fragmentList.add(FavoriteFragment.instance())
        mAdapter = FragmentAdapter(supportFragmentManager, fragmentList)
        view_pager.adapter = mAdapter
        tab_layout.setupWithViewPager(view_pager)
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 -> view_pager.setCurrentItem(0, true)
                    1 -> view_pager.setCurrentItem(1, true)
                    2 -> view_pager.setCurrentItem(2, true)
                }
            }

        })
    }
}
