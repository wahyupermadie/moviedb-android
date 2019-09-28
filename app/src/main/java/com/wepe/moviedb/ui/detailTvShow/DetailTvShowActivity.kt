package com.wepe.moviedb.ui.detailTvShow

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wepe.moviedb.R
import com.wepe.moviedb.adapter.TrailerAdapter
import com.wepe.moviedb.databinding.ActivityDetailTvShowBinding
import com.wepe.moviedb.service.model.popular.tvShow.ResultsItem
import com.wepe.moviedb.utils.Constant
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_tv_show.*
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailTvShowActivity : AppCompatActivity() {
    private lateinit var mBinding : ActivityDetailTvShowBinding
    private lateinit var tvShowItem : ResultsItem
    private lateinit var trailerAdapter: TrailerAdapter
    private var isFavorite : Boolean? = false
    private val detailTvShowVM : DetailTvShowVM by viewModel()
    private var menuItem : Menu? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_detail_tv_show)
        tvShowItem = intent.getParcelableExtra("details")
        mBinding.items = tvShowItem
        setSupportActionBar(toolbar)
        supportActionBar?.title = tvShowItem.originalName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getData()
        checkFavorite()
    }

    @SuppressLint("CheckResult")
    private fun checkFavorite() {
    detailTvShowVM.getSingleData(tvShowItem.id!!)
        ?.observeOn(AndroidSchedulers.mainThread())
        ?.subscribeOn(Schedulers.io())
        ?.subscribeWith(object :
            SingleObserver<ResultsItem> {
            override fun onSuccess(t: ResultsItem) {
                isFavorite = t.isFavorite
                setFavorite()
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                toast("Error "+e.localizedMessage)
            }

        })
    }

    private fun setFavorite() {
        if (isFavorite!!) {
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_added_favorite)
        }else{
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_add_favorite)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
            R.id.add_to_favorite -> {
                isFavorite = if(!isFavorite!!){
                    updateFavorite(tvShowItem.id, true)
                    !isFavorite!!
                }else{
                    updateFavorite(tvShowItem.id, false)
                    !isFavorite!!
                }
                setFavorite()
                return true
            }
        }
        return true
    }

    @SuppressLint("CheckResult")
    private fun updateFavorite(id : Int?, value : Boolean){
        id?.let {
            detailTvShowVM.updateFavorite(it, value)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribe({

                },{
                    toast("Error "+it.localizedMessage)
                })
        }
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
