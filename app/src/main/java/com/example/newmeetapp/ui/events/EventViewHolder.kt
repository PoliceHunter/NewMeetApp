package com.example.newmeetapp.ui.events

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
import com.example.newmeetapp.R
import kotlinx.android.synthetic.main.events_fragment.view.*

class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(event: EventContent.Event) {
        with(itemView) {
            event.run {
                eventName_in_list.text = eventName
                eventDate_in_list.text = eventDate
                eventTime_in_list.text = eventTime

                //Glide.with(context).load(eventCategory).into(eventCategory_ImageId)

            }
        }
    }
}