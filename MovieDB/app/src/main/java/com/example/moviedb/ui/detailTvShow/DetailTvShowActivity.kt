package com.example.moviedb.ui.detailTvShow

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.moviedb.R
import com.example.moviedb.adapter.TrailerAdapter
import com.example.moviedb.databinding.ActivityDetailTvShowBinding
import com.example.moviedb.service.model.popular.tvShow.ResultsItem
import com.example.moviedb.utils.Constant
import kotlinx.android.synthetic.main.activity_detail_tv_show.*
import org.koin.android.viewmodel.ext.android.viewModel

class DetailTvShowActivity : AppCompatActivity() {
    private lateinit var mBinding : ActivityDetailTvShowBinding
    private lateinit var tvShowItem : ResultsItem
    private lateinit var trailerAdapter: TrailerAdapter
    private val detailTvShowVM : DetailTvShowVM by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_detail_tv_show)
        tvShowItem = intent.getParcelableExtra("details")
        mBinding.items = tvShowItem
        setSupportActionBar(toolbar)
        supportActionBar?.title = tvShowItem.originalName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }

    private fun getData() {
        if (Constant.isConnected(this)){
            detailTvShowVM.getTrailers(tvShowItem.id.toString())?.observe(this, Observer{
                trailerAdapter = TrailerAdapter(it.results)
                with(rv_trailers){
                    this.adapter = trailerAdapter
                    this.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                        this@DetailTvShowActivity,
                        androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
                        false
                    )
                }
            })
        }
    }
}
