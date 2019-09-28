package com.wepe.moviedb.service.model.popular.video

import com.google.gson.annotations.SerializedName
data class ResponseVideo(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("results")
	val results: List<ResultsItem>? = null
)