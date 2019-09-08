package com.example.moviedb.service.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import com.example.moviedb.service.model.popular.ResultsItem
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface MoviesDao{
    @Query("SELECT * FROM result_item")
    fun getAll(): LiveData<List<ResultsItem>>

    @Insert(onConflict = IGNORE)
    fun insert(resultsItem: ResultsItem) : Completable

    @Query("SELECT * FROM result_item WHERE id = :id")
    fun getItem(id : Int): Single<ResultsItem>

    @Query("UPDATE result_item SET isFavorite = :favorite WHERE id = :id")
    fun setFavorite(favorite: Boolean, id : Int) : Completable

    @Query("SELECT * FROM result_item ORDER BY voteAverage DESC")
    fun getTopRatedLocalData(): LiveData<List<ResultsItem>>

    @Query("SELECT * FROM result_item WHERE isFavorite = :favorite")
    fun getFavorite(favorite: Boolean) : LiveData<List<ResultsItem>>

}