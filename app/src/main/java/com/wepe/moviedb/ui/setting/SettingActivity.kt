package com.wepe.moviedb.ui.setting

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.work.WorkManager
import com.wepe.moviedb.R
import com.wepe.moviedb.utils.Constant
import kotlinx.android.synthetic.main.activity_setting.*
import org.koin.android.ext.android.inject
import com.wepe.moviedb.utils.AlarmReceiver

class SettingActivity : AppCompatActivity() {
    private val sf : SharedPreferences by inject()
    private val workManager = WorkManager.getInstance(application)
    private val alarmReceiver= AlarmReceiver()
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
        swich_release.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                sf.edit().putBoolean(Constant.SETTING_REMINDER_NEW, true).apply()
                alarmReceiver.setRepeatingAlarm(this, Constant.MOVIES_NEW,"07:00")
            }else{
                sf.edit().putBoolean(Constant.SETTING_REMINDER_NEW, false).apply()
                alarmReceiver.cancelAlarm(this, Constant.MOVIES_NEW)
            }
        }

        swich_daily.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked){
                sf.edit().putBoolean(Constant.SETTING_REMINDER_OPEN, true).apply()
                alarmReceiver.setRepeatingAlarm(this, Constant.REMINDER,"08:00")
            }else{
                sf.edit().putBoolean(Constant.SETTING_REMINDER_OPEN, false).apply()
                alarmReceiver.cancelAlarm(this, Constant.REMINDER)
            }
        }
    }
}
