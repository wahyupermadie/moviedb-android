package com.example.moviedb.service.model.popular

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "response_popular")
@Parcelize
data class ResponseMovies(

	@PrimaryKey
	@field:SerializedName("page")
	var page: Long? = 0,

	@field:SerializedName("total_pages")
	var totalPages: Int? = null,

	@Ignore
	@field:SerializedName("results")
	var results: List<ResultsItem>? = null,

	@field:SerializedName("total_results")
	var totalResults: Int? = null
) : Parcelable