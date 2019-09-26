package com.example.moviedb.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.ACTION_LOCALE_SETTINGS
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.moviedb.R
import com.example.moviedb.ui.favorite.FavoriteFragment
import com.example.moviedb.ui.popular.PopularFragment
import com.example.moviedb.ui.setting.SettingActivity
import com.example.moviedb.ui.toprated.TopRatedFragment
import com.example.moviedb.ui.tvshow.TvShowFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
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
        supportActionBar?.title = "Popular Movies"
        view_pager.setCurrentItem(0, true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.change_language -> {
                val mIntent = Intent(ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.setting -> {
                startActivity<SettingActivity>()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    private fun setupFragment() {
        val fragmentList: ArrayList<Fragment> = arrayListOf()
        fragmentList.add(PopularFragment.instance())
        fragmentList.add(TopRatedFragment.instance())
        fragmentList.add(TvShowFragment.instance())
        fragmentList.add(FavoriteFragment.instance())

        mAdapter = FragmentAdapter(supportFragmentManager, fragmentList, this)
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
                        supportActionBar?.title = "Popular Movies"
                        view_pager.setCurrentItem(0, true)
                    }
                    1 -> {
                        supportActionBar?.title = "Top Rated Movies"
                        view_pager.setCurrentItem(1, true)
                    }
                    2 -> {
                        supportActionBar?.title = "Tv Show"
                        view_pager.setCurrentItem(2, true)
                    }
                    3 -> {
                        supportActionBar?.title = "Favorite"
                        view_pager.setCurrentItem(3, true)
                    }
                }
            }

        })
    }
}
