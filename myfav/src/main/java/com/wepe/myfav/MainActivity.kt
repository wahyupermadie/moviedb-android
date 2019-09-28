package com.wepe.myfav

import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.wepe.myfav.data.ResultsItemMovies
import com.wepe.myfav.data.ResultsItemTv
import com.wepe.myfav.movies.MovieFavorite
import com.wepe.myfav.tv.TvShowFragment
import com.wepe.myfav.utils.Constant.URI_MOVIES
import com.wepe.myfav.utils.Constant.URI_TV
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        supportActionBar?.title = "Movies"
        view_pager.setCurrentItem(0, true)
        setUpFragment()
    }

    private fun setUpFragment() {
        val fragmentList: java.util.ArrayList<Fragment> = arrayListOf()
        fragmentList.add(MovieFavorite.instance())
        fragmentList.add(TvShowFragment.instance())

        val mAdapter = FragmentAdapter(supportFragmentManager, fragmentList, this)
        view_pager.adapter = mAdapter
        tab_layout.setupWithViewPager(view_pager)
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 -> {
                        supportActionBar?.title = "Movies"
                        view_pager.setCurrentItem(0, true)
                    }
                    1 -> {
                        supportActionBar?.title = "Tv Show"
                        view_pager.setCurrentItem(1, true)
                    }
                }
            }

        })
    }
}
