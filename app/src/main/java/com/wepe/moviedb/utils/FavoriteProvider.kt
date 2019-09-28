package com.wepe.moviedb.utils

import android.content.ContentProvider
import android.content.ContentValues
import android.net.Uri
import android.content.UriMatcher
import android.database.Cursor
import android.content.ContentUris
import androidx.room.Room
import com.wepe.moviedb.service.local.MoviesDatabase


class FavoriteProvider : ContentProvider(){

    /** The authority of this content provider.  */
    val AUTHORITY = "com.wepe.moviedb"

    /** The match code for some items in the MOVIES table.  */
    private val CODE_MOVIES_DIR = 1

    /** The match code for an item in the MOVIES table.  */
    private val CODE_MOVIES_ITEM = 2

    /** The match code for some items in the TV table.  */
    private val CODE_TV_DIR = 3

    /** The match code for an item in the TV table.  */
    private val CODE_TV_ITEM = 4

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
        val context = context ?: return null
        val database = Room.databaseBuilder(context, MoviesDatabase::class.java, "movies_database").fallbackToDestructiveMigration().build()
        val code = MATCHER.match(uri)
        if (code == CODE_MOVIES_DIR || code == CODE_MOVIES_ITEM) {
            val moviesDao = database.movieDao()
            val cursor: Cursor
            if (code == CODE_MOVIES_DIR) {
                cursor = moviesDao.getFavoriteCursor(true)
            }else{
                cursor = moviesDao.getFavoriteCursor(true)
            }
            cursor.setNotificationUri(context.contentResolver, uri)
            return cursor
        } else if (code == CODE_TV_DIR || code == CODE_TV_ITEM) {
            val tvDao = database.tvShowDao()
            val cursor: Cursor
            if (code == CODE_TV_DIR){
                cursor = tvDao.getFavoriteCursor(true)
            }else{
                cursor = tvDao.getFavoriteCursor(true)
            }
            cursor.setNotificationUri(context.contentResolver, uri)
            return cursor
        }else{
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