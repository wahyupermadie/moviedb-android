package com.wepe.moviedb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.wepe.moviedb.R
import com.wepe.moviedb.databinding.TvShowItemsBinding
import com.wepe.moviedb.service.model.popular.tvShow.ResultsItem

class TvShowAdapter(private var resultsItem: MutableList<ResultsItem>?, private val listener : (ResultsItem) -> Unit) : RecyclerView.Adapter<TvShowAdapter.ViewHolder>() {
    fun addData(list: ResultsItem){
        resultsItem?.add(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<TvShowItemsBinding>(
            LayoutInflater.from(parent.context),
            R.layout.tv_show_items, parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.items = resultsItem?.get(position)
        holder.bindItem(resultsItem?.get(position), listener)
    }

    override fun getItemCount(): Int {
        return if(resultsItem?.size == null) 0 else resultsItem!!.size
    }

    class ViewHolder(val binding: TvShowItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(resultsItem: ResultsItem?, listener: (ResultsItem) -> Unit) {
            itemView.rootView.setOnClickListener {
                resultsItem?.let { it1 -> listener(it1) }
            }
        }
    }
}