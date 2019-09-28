package com.wepe.myfav.movies
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.wepe.myfav.R
import com.wepe.myfav.data.ResultsItemMovies
import com.wepe.myfav.databinding.MovieItemsBinding

class PopularAdapter(private var resultsItem: MutableList<ResultsItemMovies>?, private val listener : (ResultsItemMovies) -> Unit) : RecyclerView.Adapter<PopularAdapter.ViewHolder>() {
    fun addData(list : ResultsItemMovies){
        resultsItem?.add(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<MovieItemsBinding>(
            LayoutInflater.from(parent.context),
            R.layout.movie_items, parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.populars = resultsItem?.get(position)
        holder.bindItem(resultsItem?.get(position), listener)
    }

    override fun getItemCount(): Int {
        return if(resultsItem?.size == null) 0 else resultsItem!!.size
    }

    class ViewHolder(val binding: MovieItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(resultsItem: ResultsItemMovies?, listener: (ResultsItemMovies) -> Unit) {
            itemView.rootView.setOnClickListener {
                resultsItem?.let { it1 -> listener(it1) }
            }
        }
    }
}