package com.example.moviedb.utils
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

class BindingExtension{
    companion object{
        @JvmStatic
        @BindingAdapter("srcImage")
        fun setImage(view : AppCompatImageView, url : String){
            Glide.with(view.context)
                .asBitmap()
                .thumbnail(0.1f)
                .load(Constant.IMAGE_URL+url)
                .into(view)
        }

        @JvmStatic
        @BindingAdapter("textDate")
        fun setDate(view: AppCompatTextView, date: String){
            val newDate = Constant.dateFormat(date)
            view.text = newDate
        }
    }
}