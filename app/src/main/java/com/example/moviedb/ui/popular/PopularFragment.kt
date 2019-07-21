package com.example.moviedb.ui.popular

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
import com.example.moviedb.adapter.PopularAdapter
import com.example.moviedb.service.model.popular.ResultsItem
import com.example.moviedb.ui.detail.DetailActivity
import com.example.moviedb.utils.Constant
import kotlinx.android.synthetic.main.popular_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class PopularFragment() : Fragment(){
    private val popularViewModel : PopularViewModel by viewModel()
    private lateinit var popularAdapter : PopularAdapter
    private var popularList : List<ResultsItem>? = null
    private lateinit var dialog : ProgressDialog
    private var currentPage = 1
    private var isFetchingMovies: Boolean = true
    private lateinit var manager : LinearLayoutManager

    companion object{
        fun instance() : PopularFragment{
            val args = Bundle()
            val fragment = PopularFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.popular_fragment, container, false)
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
            popularViewModel.getPopularMovies(page)?.observe(this, Observer {
                popularList = it?.results
                dialog.dismiss()
                setAdapter()
            })
        }else{
            popularViewModel.getLocalMovies()?.observe(this, Observer {
                popularList = it
                dialog.dismiss()
                setAdapter()
            })
        }
    }

    private fun setAdapter() {
        popularList?.forEach {
            popularAdapter.addData(it)
        }
        popularAdapter.notifyDataSetChanged()
        isFetchingMovies = false
    }

    @SuppressLint("WrongConstant")
    private fun init() {
        dialog = ProgressDialog(context)
        dialog.setMessage("Fetching data...")
        dialog.setCancelable(false)
        dialog.show()
        manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        popularAdapter = PopularAdapter(mutableListOf()) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("details", it)
            startActivity(intent)
        }
        with(rv_movies){
            this.layoutManager = manager
            this.adapter = popularAdapter
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