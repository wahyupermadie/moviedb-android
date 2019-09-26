package com.example.moviedb.utils

import android.content.ContentProvider
import android.content.ContentValues
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.room.RoomMasterTable.TABLE_NAME
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import com.example.moviedb.service.model.popular.ResultsItem
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.content.UriMatcher
import android.database.Cursor
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.room.RoomMasterTable.TABLE_NAME
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.room.RoomMasterTable.TABLE_NAME
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.content.ContentUris
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.room.Room
import com.example.moviedb.service.local.MoviesDatabase


class FavoriteProvider : ContentProvider(){

    /** The authority of this content provider.  */
    val AUTHORITY = "com.example.moviedb"

    /** The URI for the Movies table.  */
    val URI_MOVIES = Uri.parse(
        "content://$AUTHORITY/result_item"
    )

    /** The URI for the tvShow table.  */
    val URI_TV = Uri.parse(
        "content://$AUTHORITY/results_show"
    )

    /** The match code for some items in the MOVIES table.  */
    private val CODE_MOVIES_DIR = 1

    /** The match code for an item in the MOVIES table.  */
    private val CODE_MOVIES_ITEM = 2

    /** The match code for some items in the TV table.  */
    private val CODE_TV_DIR = 1

    /** The match code for an item in the TV table.  */
    private val CODE_TV_ITEM = 2

    /** The URI matcher.  */
    private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

    private fun initializeUriMatching() {
        MATCHER.addURI(FavoriteProvider().AUTHORITY, "result_item", CODE_MOVIES_DIR)
        MATCHER.addURI(FavoriteProvider().AUTHORITY, "result_item"+ "/*", CODE_MOVIES_ITEM)
        MATCHER.addURI(FavoriteProvider().AUTHORITY, "results_show", CODE_TV_DIR)
        MATCHER.addURI(FavoriteProvider().AUTHORITY, "results_show"+ "/*", CODE_TV_ITEM)
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        return ContentUris.withAppendedId(p0, 1)
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val code = MATCHER.match(uri)
        if (code == CODE_MOVIES_DIR || code == CODE_MOVIES_ITEM) {
            val context = context ?: return null
            val database = Room.databaseBuilder(context, MoviesDatabase::class.java, "movies_database").fallbackToDestructiveMigration().build()
            val moviesDao = database.movieDao()
            val cursor: Cursor
            if (code == CODE_MOVIES_DIR) {
                cursor = moviesDao.getFavoriteCursor(true)
            }else{
                cursor = moviesDao.getFavoriteCursor(true)
            }
            cursor.setNotificationUri(context.contentResolver, uri)
            return cursor
        } else {
            throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun onCreate(): Boolean {
        initializeUriMatching()
        return true
    }

    override fun update(uri: Uri, p1: ContentValues?, p2: String?, p3: Array<String>?): Int {
        return 1
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<String>?): Int {
        return 1
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}