package com.example.moviedb.ui

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.adapter.PopularAdapter
import com.example.moviedb.service.model.popular.ResultsItem
import com.example.moviedb.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(){
    private val mainViewModel : MainViewModel by viewModel()
    private lateinit var popularAdapter : PopularAdapter
    private var popularList : List<ResultsItem>? = null
    private lateinit var dialog : ProgressDialog
    private var currentPage = 1
    private var isFetchingMovies: Boolean = true
    private lateinit var manager : LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        fetchData(currentPage)
        scrollListener()
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

    private fun fetchData(page : Int) {
        toast(currentPage.toString())
        isFetchingMovies = true
        if (isConnected(this)){
            mainViewModel.getPopularMovies(page)?.observe(this, Observer {
                popularList = it?.results
                dialog.dismiss()
                setAdapter()
            })
        }else{
            mainViewModel.getLocalMovies(page)?.observe(this, Observer {
                popularList = it?.results
                dialog.dismiss()
                setAdapter()
            })
        }
    }

    private fun isConnected(ctx: Context?) : Boolean {
        val connectivityManager = ctx?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetwork != null
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
        dialog = indeterminateProgressDialog("Fetching data...","Loading...")
        dialog.setCancelable(false)
        dialog.show()
        manager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        popularAdapter = PopularAdapter(mutableListOf()) {
            startActivity<DetailActivity>("details" to it)
        }
        with(rv_movies){
            this.layoutManager = manager
            this.adapter = popularAdapter
        }
    }
}
