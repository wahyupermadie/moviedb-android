package com.example.moviedb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedb.R
import com.example.moviedb.service.model.popular.video.ResultsItem
import kotlinx.android.synthetic.main.trialers_item.view.*


class TrailerAdapter(private var trailes : List<ResultsItem>?) : RecyclerView.Adapter<TrailerAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.trialers_item, parent, false))
    }

    override fun getItemCount(): Int {
        return if (trailes?.size == null )0 else trailes!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(trailes?.get(position))
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(
            resultsItem: ResultsItem?
        ) {
            Glide.with(itemView.context)
                .asBitmap()
                .thumbnail(0.1f)
                .load("http://img.youtube.com/vi/" + resultsItem?.key + "/default.jpg")
                .into(itemView.iv_trailes)

        }
    }

}
