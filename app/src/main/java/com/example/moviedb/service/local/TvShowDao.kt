package com.example.moviedb.service.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import com.example.moviedb.service.model.popular.tvShow.ResultsItem
import io.reactivex.Completable
import io.reactivex.Single


@Dao
interface TvShowDao {
    @Query("SELECT * FROM results_show")
    fun getData() : LiveData<List<ResultsItem>>

    @Query("UPDATE results_show SET isFavorite = :favorite WHERE id = :id")
    fun setFavorite(favorite: Boolean, id : Int) : Completable

    @Query("SELECT * FROM results_show WHERE isFavorite = :favorite")
    fun getFavorite(favorite: Boolean) : LiveData<List<ResultsItem>>

    @Query("SELECT * FROM results_show WHERE id = :id")
    fun getItem(id : Int): Single<ResultsItem>

    @Insert(onConflict = IGNORE)
    fun insert(resultsItem: ResultsItem) : Completable
}
