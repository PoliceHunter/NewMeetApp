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
import com.example.newmeetapp.ui.events.inRelative
import com.example.newmeetapp.ui.events.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener

class MembersAdapter(private val userList: ArrayList<inRelative>,
                     private val adminId : String,
                     private val onMemberListener: OnMemberListener) : RecyclerView.Adapter<MembersAdapter.MyViewHolder>()
{

    private var auth : FirebaseAuth? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        auth = FirebaseAuth.getInstance()
        val itemView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        return MyViewHolder(itemView)
    }

    override fun getItemViewType(position: Int): Int {
       return R.layout.fragment_member_accept

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    @SuppressLint("SetTextI18n", "WrongConstant")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]

        holder.dName.text = currentItem.User.firstname + " " + currentItem.User.lastname

//        holder.remove.setOnClickListener {
//            val db = DataBaseHandler(this)
//            if(db.deleteData(credential.id)){
//                notifyItemRemoved(holder.getAdapterPosition())
//            }
//        }

        holder.dBtDeni.setOnClickListener {
            onMemberListener.onDeleteMember(position)
            notifyItemRemoved(position)
        }
        holder.dBtAccept.setOnClickListener {
            onMemberListener.onAcceptMember(position)
            notifyItemChanged(position)
        }
        if (!currentItem.value)
        {
            holder.dBtAccept.visibility = View.VISIBLE
            holder.dBtDeni.visibility = View.VISIBLE

        }
        else
        {
            if (adminId != auth?.currentUser!!.uid)
                holder.dBtDeni.visibility = View.GONE
            holder.dBtAccept.visibility = View.GONE
        }

        holder.itemView.setOnClickListener{
            onMemberListener.onMemberClick(position)
       }

    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val dName : TextView = itemView.findViewById(R.id.members_TextRV)
        val dBtAccept : Button = itemView.findViewById(R.id.buttonAccept)
        val dBtDeni : Button = itemView.findViewById(R.id.buttonDeni)
    }
}

