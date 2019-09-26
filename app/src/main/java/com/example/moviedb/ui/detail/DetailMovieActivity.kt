package com.example.moviedb.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviedb.R
import com.example.moviedb.adapter.TrailerAdapter
import com.example.moviedb.databinding.ActivityDetailBinding
import com.example.moviedb.service.model.popular.ResultsItem
import com.example.moviedb.utils.Constant
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import android.content.Intent
import com.example.moviedb.ui.widget.FavoriteWidget


class DetailMovieActivity : AppCompatActivity() {
    private var moviesItem : ResultsItem? = null
    private var isFavorite : Boolean = false
    lateinit var mBinding : ActivityDetailBinding
    private lateinit var trailerAdapter: TrailerAdapter
    private var menuItem : Menu? = null
    private val detailMovieViewModel : DetailMovieActivityViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_detail)
        moviesItem = intent.getParcelableExtra("details")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        init()
        getData()
        checkFavorite()
    }

    @SuppressLint("CheckResult")
    private fun checkFavorite() {
        detailMovieViewModel.getSingleData(moviesItem?.id!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(object : SingleObserver<ResultsItem> {
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
        if (isFavorite) {
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_added_favorite)
        }else{
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_add_favorite)
        }
    }

    private fun init() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = moviesItem?.title
        mBinding.movies = moviesItem

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    private fun updateWidget(){
        val intent = Intent(this, FavoriteWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(application).getAppWidgetIds(ComponentName(
            application, FavoriteWidget::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(intent)

    }
    @SuppressLint("CheckResult")
    private fun updateFavorite(id : Int, value : Boolean){
        detailMovieViewModel.updateFavorite(id, value)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                updateWidget()
            },{
                toast("Error "+it.localizedMessage)
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
            R.id.add_to_favorite -> {
                isFavorite = if(!isFavorite){
                    updateFavorite(moviesItem!!.id, true)
                    !isFavorite
                }else{
                    updateFavorite(moviesItem!!.id, false)
                    !isFavorite
                }
                setFavorite()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    @SuppressLint("CheckResult")
    private fun getData() {
        if (Constant.isConnected(this)){
            detailMovieViewModel.getTrailers(moviesItem?.id.toString()).observe(this, Observer{
                trailerAdapter = TrailerAdapter(it.results)
                with(mBinding.rvTrailers){
                    this.adapter = trailerAdapter
                    this.layoutManager = LinearLayoutManager(this@DetailMovieActivity, LinearLayoutManager.HORIZONTAL, false)
                }
            })
        }
    }
}
