package com.example.moviedb.service.network

import com.example.moviedb.service.model.popular.ResponseMovies
import com.example.moviedb.service.model.popular.video.ResponseVideo
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") key : String,
                         @Query("page") page : Int) : Flowable<ResponseMovies>

    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("api_key") key : String,
                          @Query("page") page : Int) : Flowable<ResponseMovies>

    @GET("movie/{movie_id}/videos")
    fun getMoviesTrailers(@Path("movie_id") movieId : String,
                          @Query("api_key") key : String) : Flowable<ResponseVideo>
}