package com.example.newmeetapp.ui.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newmeetapp.R

class MyAdapter(private val eventList: ArrayList<Events>, private val onEventListener: OnEventListener) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.events_fragment,
        parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = eventList[position]

        holder.dName.text = currentItem.name
        holder.dDate.text = currentItem.date
        holder.dTime.text = currentItem.time
        when (currentItem.category) {
            "Прогулка" -> holder.dCategory.setImageResource(R.drawable.ic_category_walk)
            "Активный отдых" -> holder.dCategory.setImageResource(R.drawable.ic_category_sport)
            "Еда" -> holder.dCategory.setImageResource(R.drawable.ic_category_dinner)
            "Культура" -> holder.dCategory.setImageResource(R.drawable.ic_category_culture)
            "Путешествия" -> holder.dCategory.setImageResource(R.drawable.ic_category_travel)
            "Образование" -> holder.dCategory.setImageResource(R.drawable.ic_category_study )
            "Посиделки" -> holder.dCategory.setImageResource(R.drawable.ic_category_company)
            "Прочее" -> holder.dCategory.setImageResource(R.drawable.ic_category_other)
        }

        holder.itemView.setOnClickListener{
            onEventListener.onEventClick(position)
        }

    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val dName : TextView = itemView.findViewById(R.id.eventName_in_list)
        val dDate : TextView = itemView.findViewById(R.id.eventDate_in_list)
        val dTime : TextView = itemView.findViewById(R.id.eventTime_in_list)
        val dCategory : ImageView = itemView.findViewById(R.id.eventCategory_ImageId)
    }
}