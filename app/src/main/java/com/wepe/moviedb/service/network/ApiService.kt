package com.wepe.moviedb.service.network

import com.wepe.moviedb.service.model.popular.ResponseMovies
import com.wepe.moviedb.service.model.popular.tvShow.ResponseTvShow
import com.wepe.moviedb.service.model.popular.video.ResponseVideo
import io.reactivex.Flowable
import io.reactivex.Maybe
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

    @GET("tv/popular")
    fun getTvPopular(@Query("api_key") key : String,
                     @Query("page") page : Int) : Maybe<ResponseTvShow>

    @GET("tv/{tv_id}/videos")
    fun getTvTrailers(@Path("tv_id") tvId : String,
                      @Query("api_key") key : String) : Maybe<ResponseVideo>

    @GET("search/movie")
    fun searchMovie(@Query("language") language : String,
                    @Query("api_key") key: String,
                    @Query("query") query: String) : Maybe<ResponseMovies>

    @GET("search/tv")
    fun searchTv(@Query("language") language : String,
                    @Query("api_key") key: String,
                    @Query("query") query: String) : Maybe<ResponseTvShow>

    @GET("discover/movie")
    fun getNewMovies(@Query("api_key") key: String,
                     @Query("primary_release_date.gte") gte: String,
                     @Query("primary_release_date.lte") lte: String) : Maybe<ResponseMovies>
}