package com.example.moviedb.ui.setting

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.moviedb.R
import com.example.moviedb.service.worker.AlertOpenAppsWorker
import com.example.moviedb.service.worker.NewMoviesWorker
import com.example.moviedb.utils.Constant
import kotlinx.android.synthetic.main.activity_setting.*
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class SettingActivity : AppCompatActivity() {
    private val sf : SharedPreferences by inject()
    private val workManager = WorkManager.getInstance(application)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.setting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initEvent()
        initData()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initData() {
        val isRelease = sf.getBoolean(Constant.SETTING_REMINDER_NEW, false)
        val isReminder = sf.getBoolean(Constant.SETTING_REMINDER_OPEN, false)
        if (isRelease) swich_release.isChecked = true
        if (isReminder) swich_daily.isChecked = true
    }

    private fun initEvent() {
        swich_release.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked){
                sf.edit().putBoolean(Constant.SETTING_REMINDER_NEW, true).apply()
                runWorkerNewMovies()
            }else{
                sf.edit().putBoolean(Constant.SETTING_REMINDER_NEW, false).apply()
                stopWorker(Constant.SETTING_REMINDER_NEW)
            }
        }

        swich_daily.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked){
                sf.edit().putBoolean(Constant.SETTING_REMINDER_OPEN, true).apply()
                runWorkerDailyReminder()
            }else{
                sf.edit().putBoolean(Constant.SETTING_REMINDER_OPEN, false).apply()
                stopWorker(Constant.SETTING_REMINDER_OPEN)
            }
        }
    }

    private fun stopWorker(tag: String) {
        workManager.cancelAllWorkByTag(tag)
    }

    private fun runWorkerDailyReminder() {
        val dayWorkBuilder = PeriodicWorkRequest.Builder(
            AlertOpenAppsWorker::class.java, 15, TimeUnit.MINUTES, 5,
            TimeUnit.MINUTES
        ).addTag(Constant.SETTING_REMINDER_OPEN)
        val dayWork = dayWorkBuilder.build()
        workManager.enqueue(dayWork)
    }

    private fun runWorkerNewMovies() {
        val dayWorkBuilder = PeriodicWorkRequest.Builder(
            NewMoviesWorker::class.java, 15, TimeUnit.MINUTES, 5,
            TimeUnit.MINUTES
        ).addTag(Constant.SETTING_REMINDER_NEW)
        val dayWork = dayWorkBuilder.build()
        workManager.enqueue(dayWork)
    }
}
