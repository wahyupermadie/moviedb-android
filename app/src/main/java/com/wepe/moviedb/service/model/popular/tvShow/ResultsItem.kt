package com.wepe.moviedb.service.model.popular.tvShow

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "results_show")
@Parcelize
data class ResultsItem(

	@field:SerializedName("first_air_date")
	var firstAirDate: String? = null,

	@field:SerializedName("overview")
	var overview: String? = null,

	@field:SerializedName("original_language")
	var originalLanguage: String? = null,

	@Ignore
	@field:SerializedName("genre_ids")
	var genreIds: List<Int>? = null,

	@field:SerializedName("poster_path")
	var posterPath: String? = null,

	@Ignore
	@field:SerializedName("origin_country")
	var originCountry: List<String?>? = null,

	@field:SerializedName("backdrop_path")
	var backdropPath: String? = null,

	@field:SerializedName("original_name")
	var originalName: String? = null,

	@field:SerializedName("popularity")
	var popularity: Double? = null,

	@field:SerializedName("vote_average")
	var voteAverage: Double? = null,

	@field:SerializedName("name")
	var name: String? = null,

	@PrimaryKey
	@field:SerializedName("id")
	var id: Int? = null,

	@field:SerializedName("vote_count")
	var voteCount: Int? = null,

	var isFavorite: Boolean? = false
) : Parcelable