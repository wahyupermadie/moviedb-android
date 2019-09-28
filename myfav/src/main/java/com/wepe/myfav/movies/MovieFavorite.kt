package com.wepe.myfav.movies

import android.database.Cursor
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.wepe.myfav.R
import com.wepe.myfav.data.ResultsItemMovies
import com.wepe.myfav.utils.Constant
import com.wepe.myfav.utils.Constant.LIST_DATA_KEY
import com.wepe.myfav.utils.Constant.LIST_STATE_KEY
import kotlinx.android.synthetic.main.fragment_movies.*
import org.jetbrains.anko.support.v4.toast

class MovieFavorite : Fragment(){
    private var mCursor: Cursor? = null
    private var recyclerViewState : Parcelable? = null
    private var listMovies : ArrayList<ResultsItemMovies> = arrayListOf()
    private var manager = LinearLayoutManager(context)
    companion object{
        fun instance() : MovieFavorite{
            return MovieFavorite()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState!=null){
            recyclerViewState = savedInstanceState.getParcelable(LIST_STATE_KEY)
            rv_movies.layoutManager?.onRestoreInstanceState(recyclerViewState)
            listMovies = savedInstanceState.getParcelableArrayList<ResultsItemMovies>(LIST_DATA_KEY) as ArrayList<ResultsItemMovies>
            setAdapter()
        }
        else{
            fetchMovies()
        }
    }

    private fun setAdapter() {
        val adapterMovies  = PopularAdapter(arrayListOf()){
            toast("You clicked "+it.originalTitle)
        }
        listMovies.forEach{
            adapterMovies.addData(it)
        }
        with(rv_movies){
            adapter = adapterMovies
            layoutManager = manager
        }
        adapterMovies.notifyDataSetChanged()
    }

    private fun fetchMovies() {
        listMovies.clear()
        mCursor = context?.contentResolver?.query(Constant.URI_MOVIES, null, null, null, null)
        if (mCursor != null) {
            try {
                if (mCursor?.moveToFirst()!!) {
                    do {
                        mCursor?.let {
                            val items = ResultsItemMovies(
                                id = it.getColumnIndex("id"),
                                originalTitle = it.getString(it.getColumnIndex("originalTitle")),
                                backdropPath = it.getString(it.getColumnIndex("backdropPath")),
                                overview = it.getString(it.getColumnIndex("overview"))
                            )
                            listMovies.add(items)
                        }
                    } while (mCursor?.moveToNext()!!)
                }
            } catch (e: Exception) {
                mCursor?.close()
            } finally {
                mCursor?.close()
            }
        }
        setAdapter()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        recyclerViewState = rv_movies.layoutManager?.onSaveInstanceState()
        outState.putParcelable(LIST_STATE_KEY, recyclerViewState)
        outState.putParcelableArrayList(LIST_DATA_KEY, listMovies as java.util.ArrayList<out Parcelable>)
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        if (recyclerViewState != null) {
            manager.onRestoreInstanceState(recyclerViewState)
        }
    }
}