package com.example.newmeetapp.ui.inviting

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newmeetapp.R
import com.example.newmeetapp.ui.events.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.profile_fragment.*

class InvitingMemberAdapter(private val usersList: ArrayList<user>,
                            private val eventId : String,
                            private val onEventListener: OnInvitingListener) : RecyclerView.Adapter<InvitingMemberAdapter.MyViewHolder>() {

    private var isSelected = false
    private lateinit var membersDb : DatabaseReference
    private lateinit var my_eventDb : DatabaseReference
    private lateinit var notifcDb : DatabaseReference
    private lateinit var auth : FirebaseAuth
    fun getSelected(): Boolean {
        return isSelected
    }

    fun setSelected(selected: Boolean) {
        isSelected = selected
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.inviting_member,
            parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val dName : TextView = itemView.findViewById(R.id.name_member)
        val dImage : ImageView = itemView.findViewById(R.id.invitingPhoto)
        val dInvite : Button = itemView.findViewById(R.id.InviteButton)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = usersList[position]

        holder.dName.text = "${currentItem.firstname}  ${currentItem.lastname}"
//        val imageStorageRef = FirebaseStorage.getInstance().getReference("photo/${currentItem.id}")
//
//
//        imageStorageRef.downloadUrl.addOnCompleteListener { photoUri ->
//            holder.dImage.setImageURI(photoUri.result)
//        }

        if (currentItem.uri != null)
        {
            Glide.with(holder.dImage)
                    .load(currentItem.uri)
                    .into(holder.dImage)
        }

        membersDb = FirebaseDatabase.getInstance().getReference("members")
        my_eventDb = FirebaseDatabase.getInstance().getReference("my_event")
        notifcDb = FirebaseDatabase.getInstance().getReference("notification")
        auth = FirebaseAuth.getInstance()
        holder.dInvite.setOnClickListener {
            if (!getSelected())
            {
                setSelected(true)
                holder.dInvite.setBackgroundResource(android.R.drawable.radiobutton_on_background)
                membersDb.child("$eventId/${currentItem.id}").setValue("true")
                my_eventDb.child("${currentItem.id}/$eventId").setValue(true)
                notifcDb.child("${currentItem.id}/$eventId").setValue(auth.currentUser!!.uid)
            }
            else
            {
                setSelected(false)
                holder.dInvite.setBackgroundResource(android.R.drawable.radiobutton_off_background)
                membersDb.child("$eventId/${currentItem.id}").removeValue()
                my_eventDb.child("${currentItem.id}/$eventId").removeValue()
                notifcDb.child("${currentItem.id}/$eventId").removeValue()
            }

        //TODO В идеале нужно добавлять в бд inviting list, приглашение, которое будет отображаться у конкретного пользователя,
            // которого пригласили, после чего он может либо принять либо отклонить приглашение
        }

    }

}

interface OnInvitingListener{

    fun onInviteClick(position: Int) {

    }

}