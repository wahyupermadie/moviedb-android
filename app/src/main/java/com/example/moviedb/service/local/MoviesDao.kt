package com.example.moviedb.service.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.moviedb.service.model.popular.ResponsePopular
import io.reactivex.Completable

@Dao
interface MoviesDao{
    @Query("SELECT * FROM response_popular WHERE page = :page")
    fun getAll(page : Int): LiveData<ResponsePopular>

    @Insert(onConflict = REPLACE)
    fun insert(responsePopular: ResponsePopular) : Completable
}