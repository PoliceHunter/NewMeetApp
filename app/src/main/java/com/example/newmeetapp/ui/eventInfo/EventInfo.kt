package com.example.newmeetapp.ui.eventInfo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newmeetapp.R
import com.example.newmeetapp.ui.events.Events
import kotlinx.android.synthetic.main.event_info.*

class EventInfo : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.event_info)

        val bundle = intent.getSerializableExtra("event") as? Events

        if (bundle != null) {
            eventNameInfoId.text = bundle.name
            eventCategoryInfoId.text = bundle.category
            eventDateTimeInfoId.text = "${bundle.time}  ${bundle.date}"
            eventDescriptionInfoId.text = bundle.details
        }
    }
}