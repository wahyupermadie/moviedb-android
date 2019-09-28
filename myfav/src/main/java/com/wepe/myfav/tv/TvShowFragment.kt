package com.wepe.myfav.tv

import android.database.Cursor
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.wepe.myfav.R
import com.wepe.myfav.data.ResultsItemMovies
import com.wepe.myfav.data.ResultsItemTv
import com.wepe.myfav.utils.Constant
import com.wepe.myfav.utils.Constant.LIST_DATA_KEY
import com.wepe.myfav.utils.Constant.LIST_STATE_KEY
import kotlinx.android.synthetic.main.fragment_tv.*
import org.jetbrains.anko.support.v4.toast

class TvShowFragment : Fragment(){
    private var mCursor: Cursor? = null
    private var listTv : ArrayList<ResultsItemTv> = arrayListOf()
    private var recyclerViewState : Parcelable? = null
    private var manager = LinearLayoutManager(context)
    companion object{
        fun instance() : TvShowFragment{
            return TvShowFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tv, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState!=null){
            recyclerViewState = savedInstanceState.getParcelable(LIST_STATE_KEY)
            rv_tv.layoutManager?.onRestoreInstanceState(recyclerViewState)
            listTv = savedInstanceState.getParcelableArrayList<ResultsItemTv>(LIST_DATA_KEY) as ArrayList<ResultsItemTv>
            setAdapter()
        }
        else{
            fetchTv()
        }
    }

    private fun setAdapter() {
        val adapterMovies  = TvShowAdapter(arrayListOf()){
            toast("You clicked "+it.originalName)
        }
        listTv.forEach{
            adapterMovies.addData(it)
        }
        with(rv_tv){
            adapter = adapterMovies
            layoutManager = manager
        }
        adapterMovies.notifyDataSetChanged()
    }

    private fun fetchTv() {
        listTv.clear()
        mCursor = context?.contentResolver?.query(Constant.URI_TV, null, null, null, null)
        if (mCursor != null) {
            try {
                if (mCursor?.moveToFirst()!!) {
                    do {
                        val items = ResultsItemTv(
                            id = mCursor?.getColumnIndex("id")!!,
                            originalName = mCursor?.getString(mCursor?.getColumnIndex("originalName")!!),
                            backdropPath = mCursor?.getString(mCursor?.getColumnIndex("backdropPath")!!),
                            overview = mCursor?.getString(mCursor?.getColumnIndex("overview")!!)
                        )
                        listTv.add(items)
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
        recyclerViewState = rv_tv.layoutManager?.onSaveInstanceState()
        outState.putParcelable(LIST_STATE_KEY, recyclerViewState)
        outState.putParcelableArrayList(LIST_DATA_KEY, listTv as java.util.ArrayList<out Parcelable>)
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        if (recyclerViewState != null) {
            manager.onRestoreInstanceState(recyclerViewState)
        }
    }
}