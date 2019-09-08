package com.example.moviedb.ui.popular

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
import com.example.moviedb.adapter.PopularAdapter
import com.example.moviedb.service.model.popular.ResultsItem
import com.example.moviedb.ui.detail.DetailMovieActivity
import com.example.moviedb.utils.Constant
import kotlinx.android.synthetic.main.fragment_popular.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PopularFragment : Fragment(){
    private val popularViewModel : PopularViewModel by viewModel()
    private lateinit var popularAdapter : PopularAdapter
    private var popularList : List<ResultsItem>? = null
    private lateinit var dialog : ProgressDialog
    private var currentPage = 1
    private var isFetchingMovies: Boolean = true
    private lateinit var manager : LinearLayoutManager
    private var recyclerViewState : Parcelable? = null
    val LIST_STATE_KEY = "RECYCLER_VIEW_STATE"
    companion object{
        fun instance() : PopularFragment{
            val args = Bundle()
            val fragment = PopularFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_popular, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated (savedInstanceState)
        init()
        scrollListener()
        if (savedInstanceState!=null){
            // getting recyclerview position
            recyclerViewState = savedInstanceState.getParcelable(LIST_STATE_KEY)
            rv_movies.layoutManager?.onRestoreInstanceState(recyclerViewState)
        }
        fetchData(currentPage)
    }

    private fun fetchData(page : Int) {
        isFetchingMovies = true
        dialog.show()
        if (Constant.isConnected(context)){
            popularViewModel.getPopularMovies(page)?.observe(viewLifecycleOwner, Observer {
                it?.results?.let {results ->
                    popularList = results
                    dialog.dismiss()
                    setAdapter()
                }
            })
        }else{
            popularViewModel.getLocalMovies().observe(viewLifecycleOwner, Observer {
                it?.let {result ->
                    popularList = result
                    dialog.dismiss()
                    setAdapter()
                }
            })
        }
    }

    private fun setAdapter() {
        popularList?.forEach {
            popularAdapter.addData(it)
        }
        popularAdapter.notifyDataSetChanged()
        isFetchingMovies = false

        if(rv_movies.adapter != popularAdapter){
            with(rv_movies){
                this.layoutManager = manager
                this.adapter = popularAdapter
            }
        }
    }

    @SuppressLint("WrongConstant")
    private fun init() {
        dialog = ProgressDialog(context)
        dialog.setMessage("Fetching data...")
        dialog.setCancelable(false)
        manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        popularAdapter = PopularAdapter(mutableListOf()) {
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

    override fun onSaveInstanceState(outState: Bundle) {
        recyclerViewState = rv_movies.layoutManager?.onSaveInstanceState()
        outState.putParcelable(LIST_STATE_KEY, recyclerViewState)
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        if (recyclerViewState != null) {
            manager.onRestoreInstanceState(recyclerViewState)
        }
    }
}