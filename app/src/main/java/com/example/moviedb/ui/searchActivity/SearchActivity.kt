package com.example.moviedb.ui.searchActivity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Adapter
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.moviedb.R
import com.example.moviedb.adapter.PopularAdapter
import com.example.moviedb.adapter.TvShowAdapter
import com.example.moviedb.service.model.popular.ResultsItem
import com.example.moviedb.ui.detail.DetailMovieActivity
import com.example.moviedb.ui.detailTvShow.DetailTvShowActivity
import com.example.moviedb.utils.AlertOpenAppsWorker
import com.example.moviedb.utils.Resources
import com.example.moviedb.utils.ViewExtention
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit



class SearchActivity : AppCompatActivity(), ViewExtention{
    private lateinit var dialog : ProgressDialog
    private lateinit var movieAdapter: PopularAdapter
    private lateinit var tvAdapter: TvShowAdapter
    private val viewModel : SearchViewModel by viewModel()
    private lateinit var type : String
    private val workManager = WorkManager.getInstance(application)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
        viewModel.setListener(this, this)

        dialog = indeterminateProgressDialog("Please Wait...","Fetching")
        dialog.setCancelable(false)
        dialog.dismiss()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        type = intent.getStringExtra("value")
        when(type){
            "movies" -> {
                supportActionBar?.title = "Search Movies"
                viewModel.movies.observe(this, Observer {
                    dialog.dismiss()
                    setupDataMovies(it.results)
                })
            }
            "tv" -> {
                supportActionBar?.title = "Search Tv"
                viewModel.tv.observe(this, Observer {
                    dialog.dismiss()
                    setupDataMovies(it.results)
                })
            }
        }
    }

    private fun <T> setupDataMovies(results: List<T>?) {
        movieAdapter = PopularAdapter(mutableListOf()){
            val intent = Intent(this, DetailMovieActivity::class.java)
            intent.putExtra("details", it)
            startActivity(intent)
        }
        tvAdapter = TvShowAdapter(mutableListOf()){
            val intent = Intent(this, DetailTvShowActivity::class.java)
            intent.putExtra("details", it)
            startActivity(intent)
        }

        when(type){
            "movies" -> {
                results?.forEach {
                    movieAdapter.addData(it as ResultsItem)
                }
                with(rv_search){
                    this.adapter = movieAdapter
                    this.layoutManager = LinearLayoutManager(context)
                }
                movieAdapter.notifyDataSetChanged()
            }
            "tv" -> {
                results?.forEach {
                    tvAdapter.addData(it as com.example.moviedb.service.model.popular.tvShow.ResultsItem)
                }
                with(rv_search){
                    this.adapter = tvAdapter
                    this.layoutManager = LinearLayoutManager(context)
                }
                tvAdapter.notifyDataSetChanged()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        searchItem?.expandActionView()
        val searchView = searchItem?.actionView as SearchView
        searchView.isFocusable = false
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                dialog.show()
                when(type){
                    "movies"->{
                        viewModel.searchMovie(p0.toString())
                        runWorker()
                    }
                    else -> {
                        viewModel.searchTv(p0.toString())

                    }
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun runWorker() {
        val dayWorkBuilder = PeriodicWorkRequest.Builder(
            AlertOpenAppsWorker::class.java, 15, TimeUnit.SECONDS, 5,
            TimeUnit.MINUTES
        )
        val dayWork = dayWorkBuilder.build()
        workManager.enqueue(dayWork)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onError(message: String) {
        toast(message)
        dialog.dismiss()
    }

}
