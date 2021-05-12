package com.example.newmeetapp.ui.eventInfo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newmeetapp.R
import com.example.newmeetapp.ui.events.Events
import com.example.newmeetapp.ui.events.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.event_info.*
import java.util.*
import kotlin.collections.ArrayList

class EventInfo : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var usersReference: DatabaseReference
    private lateinit var participants : ArrayList<user>
    lateinit var mRecyclerView : RecyclerView
    lateinit var admin : user
    private lateinit var etUnicalID : UUID



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.event_info)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!.uid
        val bundle = intent.getSerializableExtra("event") as? Events
        etUnicalID = UUID.randomUUID()
        if (bundle != null) {
           // getAdminInfo(bundle.admin)

            eventNameInfoId.text = bundle.name
            eventCategoryInfoId.text = bundle.category
            eventDateTimeInfoId.text = "${bundle.time}  ${bundle.date}"
            eventDescriptionInfoId.text = bundle.details
            OrgNameInfoId.text = "${bundle.adminInfo?.firstname} ${bundle.adminInfo?.lastname}"
            if (currentUser == bundle.admin)
            {
                bt_go.setText("Настройки")
                bt_go.setOnClickListener{
                    Toast.makeText(this, "You are admin this event", Toast.LENGTH_SHORT).show()
                }

            }
            else
            {
                bt_go.setOnClickListener {
                    databaseReference = FirebaseDatabase.getInstance().getReference("members/${bundle.id}/$currentUser")
                    databaseReference.setValue("true")
                    Toast.makeText(this, "You will be added to this event", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
        val bundle = intent.getSerializableExtra("event") as? Events
        mRecyclerView = findViewById(R.id.membersRV)!!

        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.setHasFixedSize(true)
        participants = arrayListOf<user>()
        getUsersData(bundle?.id)
        //mRecyclerView.adapter = MembersAdapter(participants, this)




    }

    private fun getUsersData(id: String?)
    {
        usersReference = FirebaseDatabase.getInstance().getReference("members/$id")
        val userReference = FirebaseDatabase.getInstance().getReference("profile")

        usersReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (users in snapshot.children)
                {
                    var us : user? = null
                    userReference.child("${users.key}").addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            us = snapshot.getValue(user::class.java)
                        }

                    })
                    val user = getUser(users.key)
                    if (user != null) {
                        participants.add(user)
                    }
                    if (us != null)
                    {
                        participants.add(us!!)
                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getUser(key: String?): user? {

        val userReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("profile/$key")
        var us : user? = null


        userReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
               us = snapshot.getValue(user::class.java)
                us?.let { participants.add(it) }
                mRecyclerView.adapter = MembersAdapter(participants,
                    this)
            }

        })

        return us
    }



}
