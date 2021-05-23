package com.example.newmeetapp.ui.notifications

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newmeetapp.R
import com.example.newmeetapp.ui.events.Events
import com.example.newmeetapp.ui.events.OnEventListener


class NotifAdapter(private val eventList: ArrayList<Events>, private val onNotifListener: onNotifListener) : RecyclerView.Adapter<NotifAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.notification_in_list,
                parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = eventList[position]

        holder.dName.text = currentItem.name
        holder.dDate.text = currentItem.date
        holder.dTime.text = currentItem.time
        holder.dWhoInvite.text = "Вас пригласил/a ${currentItem.adminInfo?.firstname}  ${currentItem.adminInfo?.lastname} "
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

    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val dName : TextView = itemView.findViewById(R.id.eventName_in_list)
        val dDate : TextView = itemView.findViewById(R.id.eventDate_in_list)
        val dTime : TextView = itemView.findViewById(R.id.eventTime_in_list)
        val dCategory : ImageView = itemView.findViewById(R.id.eventCategory_ImageId)
        val dWhoInvite : TextView = itemView.findViewById(R.id.whoInvite_Text)
    }
}

interface onNotifListener{

    fun onNotifClicked(position: Int) {

    }

}