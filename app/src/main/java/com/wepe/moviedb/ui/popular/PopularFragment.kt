package com.wepe.moviedb.ui.popular

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wepe.moviedb.R
import com.wepe.moviedb.adapter.PopularAdapter
import com.wepe.moviedb.service.model.popular.ResultsItem
import com.wepe.moviedb.ui.detail.DetailMovieActivity
import com.wepe.moviedb.ui.searchActivity.SearchActivity
import com.wepe.moviedb.utils.Constant
import com.wepe.moviedb.utils.Constant.LIST_DATA_KEY
import com.wepe.moviedb.utils.Constant.LIST_STATE_KEY
import kotlinx.android.synthetic.main.fragment_popular.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class PopularFragment : Fragment(){
    private val popularViewModel : PopularViewModel by viewModel()
    private lateinit var popularAdapter : PopularAdapter
    private var popularList : List<ResultsItem>? = null
    private lateinit var dialog : ProgressDialog
    private var currentPage = 1
    private var isFetchingMovies: Boolean = true
    private lateinit var manager : LinearLayoutManager
    private var recyclerViewState : Parcelable? = null

    companion object{
        fun instance() : PopularFragment{
            val args = Bundle()
            val fragment = PopularFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_popular, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated (savedInstanceState)
        init()
        scrollListener()
        setHasOptionsMenu(true)
        if (savedInstanceState!=null){
            recyclerViewState = savedInstanceState.getParcelable(LIST_STATE_KEY)
            rv_movies.layoutManager?.onRestoreInstanceState(recyclerViewState)
            popularList = savedInstanceState.getParcelableArrayList(LIST_DATA_KEY)
            setAdapter()
        }
        else{
            fetchData(currentPage)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_icon, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.icon_search -> {
                val intent = Intent(context, SearchActivity::class.java)
                intent.putExtra("value", "movies")
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
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
        outState.putParcelableArrayList(LIST_DATA_KEY, popularList as ArrayList<out Parcelable> )
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        if (recyclerViewState != null) {
            manager.onRestoreInstanceState(recyclerViewState)
        }
    }
}