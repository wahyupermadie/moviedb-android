package com.example.moviedb.service.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.moviedb.service.model.popular.favorite.FavoriteItem
import io.reactivex.Completable

@Dao
interface FavoritDao{
    @Query("SELECT * FROM favoriteItem")
    fun getAll(): LiveData<FavoriteItem>

    @Insert(onConflict = REPLACE)
    fun insert(favoriteItem: FavoriteItem) : Completable
}