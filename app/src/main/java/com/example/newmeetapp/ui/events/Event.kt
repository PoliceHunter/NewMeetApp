package com.example.newmeetapp.ui.events

import android.app.PendingIntent.getActivity
import androidx.appcompat.app.AppCompatActivity
import com.example.newmeetapp.dummy.DummyContent
import java.sql.Time
import java.util.*
import com.example.newmeetapp.R


object EventContent {
    data class Event(val eventId: String, val eventName: String, val eventDate: String, val eventTime: String, val eventCategory: String) {
    }

    val ITEMS: MutableList<Event> = ArrayList()

    // тут я попробовала перенести функции из активити, но нет доступа к resources
//    private val COUNT = 11
//
//    init {
//        // Add some sample items.
//        for (i in 1..COUNT) {
//            EventContent.addItem(EventContent.createEventItem(i))
//        }
//    }
//    private fun addItem(item: EventContent.Event) {
//        EventContent.ITEMS.add(item)
//    }
//
//    private fun createEventItem(position: Int): EventContent.Event {
//        return EventContent.Event(
//            bigRandom.toString(),
//            getActivity().getRandomName(),
//            getRandomDate(),
//            getRandomDate(),
//            getRandomDate()
//        )
//    }
//
//    private val random: Int
//        get() = Random().nextInt(9)
//
//    private val bigRandom: Int
//        get() = Random().nextInt(10000)
//
//    private fun getRandomName() = resources.getStringArray(R.array.names)[random]
//    private fun getRandomDate() = resources.getStringArray(R.array.dates)[random]
}