package com.example.moviedb.ui.favorite

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
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
import com.example.moviedb.ui.detail.DetailMovieActivity
import kotlinx.android.synthetic.main.favorit_fragment.*
import org.koin.android.ext.android.inject

class FavoriteFragment : Fragment(){
    private lateinit var popularAdapter : PopularAdapter
    private var listData : MutableList<ResultsItem> = mutableListOf()
    private val moviesDao : MoviesDao by inject()
    private var recyclerViewState : Parcelable? = null
    val LIST_STATE_KEY = "RECYCLER_VIEW_STATE"

    companion object{
        fun instance() : FavoriteFragment{
            val args = Bundle()
            val fragment = FavoriteFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        recyclerViewState = rv_movies.layoutManager?.onSaveInstanceState()
        outState.putParcelable(LIST_STATE_KEY, recyclerViewState)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.favorit_fragment, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState!=null){
            // getting recyclerview position
            recyclerViewState = savedInstanceState.getParcelable(LIST_STATE_KEY)
            rv_movies.layoutManager?.onRestoreInstanceState(recyclerViewState)
        }
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
            val intent = Intent(context, DetailMovieActivity::class.java)
            intent.putExtra("details", it)
            startActivity(intent)
        }

        with(rv_movies){
            this.adapter = popularAdapter
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        rv_movies.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }
}