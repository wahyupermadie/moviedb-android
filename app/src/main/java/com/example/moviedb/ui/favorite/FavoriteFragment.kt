package com.example.moviedb.ui.favorite

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.moviedb.R
import com.example.moviedb.adapter.PopularAdapter
import com.example.moviedb.service.local.MoviesDao
import com.example.moviedb.service.model.popular.ResultsItem
import com.example.moviedb.ui.favorite.movies.MoviesFavoriteFragment
import com.example.moviedb.ui.favorite.movies.TvShowFavFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.favorit_fragment.*
import org.koin.android.ext.android.inject


class FavoriteFragment : Fragment(), ViewPager.OnPageChangeListener,
    BottomNavigationView.OnNavigationItemSelectedListener{
    private val fragments = listOf(
        MoviesFavoriteFragment.instance(),
        TvShowFavFragment.instance()
    )

    companion object{
        fun instance() : FavoriteFragment{
            val args = Bundle()
            val fragment = FavoriteFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.favorit_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        nsvp_favorite.addOnPageChangeListener(this)
        nsvp_favorite.adapter = ViewPagerAdapter()
        nsvp_favorite.offscreenPageLimit = fragments.size

        btn_nav.setOnNavigationItemSelectedListener(this)
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        when(position){
            1 -> setupTitile(getString(R.string.fav_movies_title))
            else -> setupTitile(getString(R.string.fav_tv_show_title))
        }
    }

    private fun setupTitile(s: String) {
        (activity as AppCompatActivity).supportActionBar?.title = s
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.movies -> {
                nsvp_favorite.currentItem = 0
            }
            R.id.tv_show -> {
                nsvp_favorite.currentItem = 1
            }
            else -> nsvp_favorite.currentItem = 0
        }
        return true
    }

    inner class ViewPagerAdapter : FragmentPagerAdapter(childFragmentManager) {

        override fun getItem(position: Int): Fragment = fragments[position]

        override fun getCount(): Int = fragments.size
    }
}