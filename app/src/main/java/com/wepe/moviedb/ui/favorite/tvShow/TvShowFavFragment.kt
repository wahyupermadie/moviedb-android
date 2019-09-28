package com.wepe.moviedb.ui.favorite.movies

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
import com.wepe.moviedb.R
import com.wepe.moviedb.adapter.TvShowAdapter
import com.wepe.moviedb.service.local.TvShowDao
import com.wepe.moviedb.service.model.popular.tvShow.ResultsItem
import com.wepe.moviedb.ui.detailTvShow.DetailTvShowActivity
import com.wepe.moviedb.utils.Constant.LIST_STATE_KEY
import kotlinx.android.synthetic.main.fragment_tv_show.*
import org.koin.android.ext.android.inject

class TvShowFavFragment : Fragment(){
    private lateinit var tvShowAdapter : TvShowAdapter
    private var listData : MutableList<ResultsItem> = mutableListOf()
    private val tvShowDao : TvShowDao by inject()
    private var recyclerViewState : Parcelable? = null

    companion object{
        fun instance() : TvShowFavFragment{
            val args = Bundle()
            val fragment = TvShowFavFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        recyclerViewState = rv_tv_show.layoutManager?.onSaveInstanceState()
        outState.putParcelable(LIST_STATE_KEY, recyclerViewState)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tv_show, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState!=null){
            // getting recyclerview position
            recyclerViewState = savedInstanceState.getParcelable(LIST_STATE_KEY)
            rv_tv_show.layoutManager?.onRestoreInstanceState(recyclerViewState)
            listData
        }
        fetchData()
    }

    private fun fetchData() {
        tvShowDao.getFavorite(true)
            .observe(viewLifecycleOwner, Observer {
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
        tvShowAdapter = TvShowAdapter(listData){
            val intent = Intent(context, DetailTvShowActivity::class.java)
            intent.putExtra("details", it)
            startActivity(intent)
        }

        with(rv_tv_show){
            this.adapter = tvShowAdapter
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        rv_tv_show.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }
}