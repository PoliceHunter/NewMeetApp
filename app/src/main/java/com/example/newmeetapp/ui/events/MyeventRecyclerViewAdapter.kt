package com.example.newmeetapp.ui.events

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import com.example.newmeetapp.R
//import kotlinx.android.synthetic.main.events_fragment.view.*

import com.example.newmeetapp.dummy.DummyContent.DummyItem

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyEventRecyclerViewAdapter() : RecyclerView.Adapter<EventViewHolder>() {
 //   private val eventList: List<Event>)

    private val eventList = mutableListOf<EventContent.Event>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.events_fragment, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(eventList[position])
//        val item = eventList[position]
//        holder.name.text = item.eventName
//        holder.date.text = item.eventDate
//        holder.time.text = item.eventTime

    }

    override fun getItemCount(): Int = eventList.size

    fun setEvent(events: List<EventContent.Event>) {
        eventList.clear()
        eventList.addAll(events)
        notifyDataSetChanged()
    }

//    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//
////        fun bind (event: Event) {
////            with(itemView) {
////                event.run {
////                    eventName_in_list
////                }
////            }
////        }
//
//        val name: TextView = view.findViewById(R.id.eventName_in_list)
//        val date: TextView = view.findViewById(R.id.eventDate_in_list)
//        val time: TextView = view.findViewById(R.id.eventTime_in_list)
//        val category: ImageView = view.findViewById(R.id.eventDate_in_list)
//
//        override fun toString(): String {
//            return super.toString() + " '" + date.text + "'"
//        }
//    }

    fun refreshEvents () {
        notifyDataSetChanged()
    }
}