package com.example.newmeetapp.ui.eventInfo

import android.annotation.SuppressLint
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newmeetapp.R
import com.example.newmeetapp.ui.events.OnEventListener
import com.example.newmeetapp.ui.events.OnMemberListener
import com.example.newmeetapp.ui.events.user
import com.google.firebase.database.ValueEventListener

class MembersAdapter(private val userList: ArrayList<user>, private val visibility : Boolean,
                     private val onMemberListener: OnMemberListener) : RecyclerView.Adapter<MembersAdapter.MyViewHolder>() {

    var vis = visibility
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {


        val itemView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        return MyViewHolder(itemView)
    }

    override fun getItemViewType(position: Int): Int {
        return if (visibility)
            R.layout.fragment_member
        else
            R.layout.fragment_member_accept


    }

    override fun getItemCount(): Int {
        return userList.size
    }

    @SuppressLint("SetTextI18n", "WrongConstant")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]

        holder.dName.text = currentItem.firstname + currentItem.lastname

//        if (!visibility)
//        {
//            holder.dBtAccept.visibility = 1
//            holder.dBtDeni.visibility = 1
//        }
//        else
//        {
//            holder.dBtAccept.visibility = 0
//            holder.dBtDeni.visibility = 0
//        }

        holder.itemView.setOnClickListener{
            onMemberListener.onMemberClick(position)
       }

    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val dName : TextView = itemView.findViewById(R.id.members_TextRV)
//        val dBtAccept : Button = itemView.findViewById(R.id.buttonAccept)
//        val dBtDeni : Button = itemView.findViewById(R.id.buttonDeni)

    }
}

