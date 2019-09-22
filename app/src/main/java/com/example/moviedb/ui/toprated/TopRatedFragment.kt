package com.example.moviedb.ui.toprated

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
import com.example.moviedb.adapter.TopRatedAdapter
import com.example.moviedb.service.model.popular.ResultsItem
import com.example.moviedb.ui.detail.DetailMovieActivity
import com.example.moviedb.utils.Constant
import com.example.moviedb.utils.Constant.LIST_DATA_KEY
import com.example.moviedb.utils.Constant.LIST_STATE_KEY
import kotlinx.android.synthetic.main.toprated_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class TopRatedFragment : Fragment(){
    private val topRatedVM : TopRatedViewModel by viewModel()
    private lateinit var topratedAdapter : TopRatedAdapter
    private var topratedList : List<ResultsItem>? = null
    private lateinit var dialog : ProgressDialog
    private var currentPage = 1
    private var isFetchingMovies: Boolean = true
    private lateinit var manager : LinearLayoutManager
    private var recyclerViewState : Parcelable? = null
    companion object{
        fun instance() : TopRatedFragment{
            val args = Bundle()
            val fragment = TopRatedFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onResume() {
        super.onResume()
        if (recyclerViewState != null) {
            manager.onRestoreInstanceState(recyclerViewState)
        }
        dialog.dismiss()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        recyclerViewState = rv_movies.layoutManager?.onSaveInstanceState()
        outState.putParcelable(LIST_STATE_KEY, recyclerViewState)
        outState.putParcelableArrayList(LIST_DATA_KEY, topratedList as ArrayList<out Parcelable>)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.toprated_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        scrollListener()
        if (savedInstanceState!=null){
            // getting recyclerview position
            recyclerViewState = savedInstanceState.getParcelable(LIST_STATE_KEY)
            rv_movies.layoutManager?.onRestoreInstanceState(recyclerViewState)
            topratedList = savedInstanceState.getParcelableArrayList(LIST_DATA_KEY)
            setAdapter()
        }
        else{
            dialog.show()
            fetchData(currentPage)
        }
    }

    private fun fetchData(page : Int) {
        isFetchingMovies = true
        if (Constant.isConnected(context)){
            dialog.show()
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

        if(rv_movies.adapter != topratedAdapter){
            with(rv_movies){
                this.layoutManager = manager
                this.adapter = topratedAdapter
            }
        }
    }

    @SuppressLint("WrongConstant")
    private fun init() {
        dialog = ProgressDialog(context)
        dialog.setMessage("Fetching data...")
        dialog.setCancelable(false)
        manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        topratedAdapter = TopRatedAdapter(mutableListOf()) {
            val intent = Intent(context, DetailMovieActivity::class.java)
            intent.putExtra("details", it)
            startActivity(intent)
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