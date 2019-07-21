package com.example.moviedb.ui.toprated

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.adapter.TopRatedAdapter
import com.example.moviedb.service.model.popular.ResultsItem
import com.example.moviedb.ui.detail.DetailActivity
import com.example.moviedb.utils.Constant
import kotlinx.android.synthetic.main.toprated_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class TopRatedFragment() : Fragment(){
    private val topRatedVM : TopRatedViewModel by viewModel()
    private lateinit var topratedAdapter : TopRatedAdapter
    private var topratedList : List<ResultsItem>? = null
    private lateinit var dialog : ProgressDialog
    private var currentPage = 1
    private var isFetchingMovies: Boolean = true
    private lateinit var manager : LinearLayoutManager
    companion object{
        fun instance() : TopRatedFragment{
            val args = Bundle()
            val fragment = TopRatedFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.toprated_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
        fetchData(currentPage)
        scrollListener()
    }

    private fun fetchData(page : Int) {
        isFetchingMovies = true
        if (Constant.isConnected(context)){
            topRatedVM.getTopRatedMovie(page)?.observe(this, Observer {
                topratedList = it?.results
                dialog.dismiss()
                setAdapter()
            })
        }else{
            topRatedVM.getTopRatedLocalData()?.observe(this, Observer {
                topratedList = it
                dialog.dismiss()
                setAdapter()
            })
        }
    }

    private fun setAdapter() {
        topratedList?.forEach {
            topratedAdapter.addData(it)
        }
        topratedAdapter.notifyDataSetChanged()
        isFetchingMovies = false
    }

    @SuppressLint("WrongConstant")
    private fun init() {
        dialog = ProgressDialog(context)
        dialog.setMessage("Fetching data...")
        dialog.setCancelable(false)
        dialog.show()
        manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        topratedAdapter = TopRatedAdapter(mutableListOf()) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("details", it)
            startActivity(intent)
        }
        with(rv_movies){
            this.layoutManager = manager
            this.adapter = topratedAdapter
        }
    }
    private fun scrollListener() {
        rv_movies.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = manager.itemCount
                val visibleItemCount = manager.childCount
                val firstVisibleItem = manager.findFirstVisibleItemPosition()
                if(firstVisibleItem + visibleItemCount >= totalItemCount / 2){
                    if (!isFetchingMovies){
                        currentPage += 1
                        fetchData(currentPage)
                    }
                }
            }
        })
    }
}