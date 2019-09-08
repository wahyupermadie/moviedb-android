package com.example.moviedb.service.model.popular.tvShow

import com.google.gson.annotations.SerializedName

data class ResponseTvShow(

	@field:SerializedName("page")
	var page: Int? = null,

	@field:SerializedName("total_pages")
	var totalPages: Int? = null,

	@field:SerializedName("results")
	var results: List<ResultsItem>? = null,

	@field:SerializedName("total_results")
	var totalResults: Int? = null
)