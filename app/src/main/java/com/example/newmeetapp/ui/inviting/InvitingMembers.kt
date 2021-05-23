package com.example.newmeetapp.ui.inviting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newmeetapp.R
import com.example.newmeetapp.ui.events.OnMemberListener
import com.example.newmeetapp.ui.events.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InvitingMembers : AppCompatActivity(), OnInvitingListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var usersList : ArrayList<user>
    lateinit var mRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inviting_members)

        auth = FirebaseAuth.getInstance()
        usersList = arrayListOf()

        mRecyclerView = findViewById(R.id.invitingRecycle)

        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.setHasFixedSize(true)

        val eventId = intent.getSerializableExtra("event_id") as? String

        get_list_invite_members(eventId!!)


    }

    override fun onInviteClick(position: Int) {
        super.onInviteClick(position)

    }


    private fun get_list_invite_members(eventId: String) {

        val usersDb = FirebaseDatabase.getInstance().getReference("profile")

        val listCurrentMembers: ArrayList<user> = arrayListOf()
        usersDb.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (curUser in snapshot.children)
                {
                    val us = curUser.getValue(user::class.java)
                    if (us != null)
                    {
                        if (us.id == auth.currentUser!!.uid)
                            continue
                        usersList.add(us)

                    }
                }
                mRecyclerView.adapter = InvitingMemberAdapter(usersList, eventId, this@InvitingMembers)

            }

        })

    }

}