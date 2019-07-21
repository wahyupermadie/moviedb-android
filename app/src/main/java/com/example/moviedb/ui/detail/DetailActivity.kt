package com.example.moviedb.ui.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviedb.R
import com.example.moviedb.adapter.TrailerAdapter
import com.example.moviedb.databinding.ActivityDetailBinding
import com.example.moviedb.service.model.popular.ResultsItem
import kotlinx.android.synthetic.main.activity_detail.*
import org.koin.android.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {
    private var moviesItem : ResultsItem? = null
    lateinit var mBinding : ActivityDetailBinding
    private lateinit var trailerAdapter: TrailerAdapter
    private val detailViewModel : DetailActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_detail)
        moviesItem = intent.getParcelableExtra("details")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        init()
        getData()
    }

    private fun init() {
        supportActionBar?.title = moviesItem?.title
        mBinding.movies = moviesItem


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun getData() {
        detailViewModel.getTrailers(moviesItem?.id.toString()).observe(this, Observer{
            trailerAdapter = TrailerAdapter(it.results)
            with(mBinding.rvVideos){
                this.adapter = trailerAdapter
                this.layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
            }
        })
    }
}
