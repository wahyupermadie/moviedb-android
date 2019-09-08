package com.example.moviedb.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.databinding.MovieItemsBinding
import com.example.moviedb.service.model.popular.ResultsItem

class PopularAdapter(private var resultsItem: MutableList<ResultsItem>?, private val listener : (ResultsItem) -> Unit) : RecyclerView.Adapter<PopularAdapter.ViewHolder>() {
    fun addData(list : ResultsItem){
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
        fun bindItem(resultsItem: ResultsItem?, listener: (ResultsItem) -> Unit) {
            itemView.rootView.setOnClickListener {
                resultsItem?.let { it1 -> listener(it1) }
            }
        }
    }
}