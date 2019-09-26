package com.example.moviedb.ui.widget

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.graphics.Bitmap
import androidx.room.Database
import androidx.room.Room
import com.example.moviedb.service.local.MoviesDao
import com.example.moviedb.service.local.MoviesDatabase
import com.example.moviedb.service.model.popular.ResultsItem
import android.content.Intent
import android.os.Bundle
import com.example.moviedb.R
import com.bumptech.glide.request.target.SimpleTarget
import android.R.attr.path
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.moviedb.utils.Constant
import android.database.Cursor
import android.net.Uri
import android.os.Binder


class StackRemoteViewsFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory{
    private var mWidgetItems : ArrayList<ResultsItem> = arrayListOf()
    private var mCursor: Cursor? = null
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

    override fun onCreate() {
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)
        Glide.with(context)
            .asBitmap()
            .load(Constant.BACKDROP_URL+mWidgetItems[position].backdropPath)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    rv.setImageViewBitmap(R.id.imageView, resource)
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    // this is called when imageView is cleared on lifecycle call or for
                    // some other reason.
                    // if you are referencing the bitmap somewhere else too other than this imageView
                    // clear it here as you can no longer have the bitmap
                }
            })


        val extras = Bundle()
        extras.putInt(FavoriteWidget.EXTRA_ITEM, position)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getCount(): Int {
        return mWidgetItems.size
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun onDestroy() {

    }

    private fun setFavoriteFromCursor(uri: Uri) {
        val identityToken = Binder.clearCallingIdentity()
        mWidgetItems.clear()
        mCursor = context.contentResolver.query(uri, null, null, null, null)
        if (mCursor != null) {
            try {
                if (mCursor?.moveToFirst()!!) {
                    do {
                        val items = ResultsItem(
                            id = mCursor?.getColumnIndex("id")!!,
                            originalTitle = mCursor?.getString(mCursor?.getColumnIndex("originalTitle")!!),
                            backdropPath = mCursor?.getString(mCursor?.getColumnIndex("backdropPath")!!)
                        )
                        mWidgetItems.add(items)
                    } while (mCursor?.moveToNext()!!)
                }
            } catch (e: Exception) {
                mCursor?.close()
            } finally {
                mCursor?.close()
            }
        }
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDataSetChanged() {
        val thread = object : Thread() {
            override fun run() {
                super.run()
                setFavoriteFromCursor(URI_MOVIES)
            }
        }

        thread.start()

        try {
            thread.join()
        } catch (e: InterruptedException) {

        }

    }

}