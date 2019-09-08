package com.example.moviedb.ui.tvshow

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.adapter.TvShowAdapter
import com.example.moviedb.service.model.popular.tvShow.ResultsItem
import com.example.moviedb.ui.detailTvShow.DetailTvShowActivity
import com.example.moviedb.utils.Constant
import kotlinx.android.synthetic.main.fragment_tv_show.*
import kotlinx.android.synthetic.main.fragment_tv_show.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TvShowFragment : Fragment(){
    private val tvShowVM : TvShowViewModel by viewModel()
    private var currentPage : Int = 1
    private lateinit var dialog : ProgressDialog
    private lateinit var manager : LinearLayoutManager
    private lateinit var tvSHowAdapter : TvShowAdapter
    private var isFetchingTvShow: Boolean = true
    private var tvShowList : List<ResultsItem>? = null
    private var recyclerViewState : Parcelable? = null
    val LIST_STATE_KEY = "RECYCLER_VIEW_STATE"
    companion object{
        fun instance() : TvShowFragment{
            val args = Bundle()
            val fragment = TvShowFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onResume() {
        super.onResume()
        if (recyclerViewState != null) {
            manager.onRestoreInstanceState(recyclerViewState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        recyclerViewState = rv_tv_show.layoutManager?.onSaveInstanceState()
        outState.putParcelable(LIST_STATE_KEY, recyclerViewState)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tv_show, container, false)
        if (savedInstanceState!=null){
            // getting recyclerview position
            recyclerViewState = savedInstanceState.getParcelable(LIST_STATE_KEY)
            view.rv_tv_show.layoutManager?.onRestoreInstanceState(recyclerViewState)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
        scrollListener()
        if (savedInstanceState!=null){
            // getting recyclerview position
            recyclerViewState = savedInstanceState.getParcelable(LIST_STATE_KEY)
            rv_tv_show.layoutManager?.onRestoreInstanceState(recyclerViewState)
        }
        fetchData(currentPage)
    }

    private fun fetchData(currentPage: Int) {
        isFetchingTvShow = true
        if (Constant.isConnected(context)) {
            tvShowVM.getTvShowPopular(currentPage)?.observe(this, Observer {
                tvShowList = it?.results
                dialog.dismiss()
                setAdapter()
            })
        }
    }

    private fun setAdapter() {
        tvShowList?.forEach {
            tvSHowAdapter.addData(it)
        }
        tvSHowAdapter.notifyDataSetChanged()
        isFetchingTvShow = false

        if(rv_tv_show.adapter != tvSHowAdapter){
            with(rv_tv_show){
                this.layoutManager = manager
                this.adapter = tvSHowAdapter
            }
        }
    }

    @SuppressLint("WrongConstant")
    private fun init() {
        dialog = ProgressDialog(context)
        dialog.setMessage("Fetching data...")
        dialog.setCancelable(false)
        dialog.show()
        manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        tvSHowAdapter = TvShowAdapter(mutableListOf()) {
            val intent = Intent(context, DetailTvShowActivity::class.java)
            intent.putExtra("details", it)
            startActivity(intent)
        }
    }

    private fun scrollListener() {
        rv_tv_show.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = manager.itemCount
                val visibleItemCount = manager.childCount
                val firstVisibleItem = manager.findFirstVisibleItemPosition()
                if(firstVisibleItem + visibleItemCount >= totalItemCount / 2){
                    if (!isFetchingTvShow){
                        currentPage += 1
                        fetchData(currentPage)
                    }
                }
            }
        })
    }
}