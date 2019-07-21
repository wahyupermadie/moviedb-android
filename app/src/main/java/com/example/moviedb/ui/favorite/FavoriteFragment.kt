package com.example.moviedb.ui.favorite

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviedb.R
import com.example.moviedb.adapter.PopularAdapter
import com.example.moviedb.service.local.MoviesDao
import com.example.moviedb.service.model.popular.ResultsItem
import com.example.moviedb.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.favorit_fragment.*
import org.koin.android.ext.android.inject

class FavoriteFragment : Fragment(){
    private lateinit var popularAdapter : PopularAdapter
    private var listData : MutableList<ResultsItem> = mutableListOf()
    private val moviesDao : MoviesDao by inject()
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
        fetchData()
    }

    private fun fetchData() {
        moviesDao.getFavorite(true)
            .observe(this, Observer {
                listData.clear()
                listData.addAll(it)
                setAdapter()
            })
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    @SuppressLint("WrongConstant")
    private fun setAdapter() {
        popularAdapter = PopularAdapter(listData){
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("details", it)
            startActivity(intent)
        }

        with(rv_movies){
            this.adapter = popularAdapter
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }
}