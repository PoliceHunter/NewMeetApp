package com.example.newmeetapp.ui.eventInfo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newmeetapp.R
import com.example.newmeetapp.ui.events.user
import com.google.firebase.database.ValueEventListener

class MembersAdapter(private val userList: ArrayList<user>, private val onEventListener: ValueEventListener) : RecyclerView.Adapter<MembersAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.fragment_member,
            parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]

        holder.dName.text = currentItem.firstname + currentItem.lastname


//        holder.itemView.setOnClickListener{
//            onEventListener.onEventClick(position)
//        }

    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val dName : TextView = itemView.findViewById(R.id.members_TextRV)

    }
}

