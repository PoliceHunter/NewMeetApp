package com.example.newmeetapp.ui.eventInfo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    private lateinit var participantsForAdmin : ArrayList<user>
    private lateinit var participantsForUsers : ArrayList<user>
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
                val isWrite = FirebaseDatabase.getInstance().getReference("members/${bundle.id}/$currentUser")
                var isWriteBool : Boolean? = null
                isWrite.get().addOnSuccessListener {
                    if (it.value == "true")
                        isWriteBool = true
                    Log.i("firebase", "Got value ${it.value}")
                }.addOnFailureListener{
                    isWriteBool = false
                    Log.e("firebase", "Error getting data", it)
                }

                if (isWriteBool == null) {
                    bt_go.setOnClickListener {
                        databaseReference = FirebaseDatabase.getInstance().getReference("members/${bundle.id}/$currentUser")
                        databaseReference.setValue("false")
                        Toast.makeText(this, "You will be added to this event", Toast.LENGTH_SHORT).show()
                    }
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
        participantsForAdmin = arrayListOf<user>()
        participantsForUsers = arrayListOf<user>()
        getUsersData(bundle?.id)
        //mRecyclerView.adapter = MembersAdapter(participants, this)




    }

    private fun getUsersData(id: String?)
    {
        usersReference = FirebaseDatabase.getInstance().getReference("members/$id")
        val userReference = FirebaseDatabase.getInstance().getReference("profile")
        val bundle = intent.getSerializableExtra("event") as? Events

        usersReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (users in snapshot.children)
                {
                    val user = getUser(users.key, users.value)

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getUser(key: String?, value: Any?): user? {

        val bundle = intent.getSerializableExtra("event") as? Events
        val userReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("profile/$key")
        var us : user? = null


        userReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                us = snapshot.getValue(user::class.java)
                if (bundle!!.admin == auth.currentUser!!.uid)
                {
                    us?.let { participantsForAdmin.add(it) }
                    mRecyclerView.adapter = MembersAdapter(participantsForAdmin,
                        this)
                }
                else if (value == "true")
                {
                    us?.let { participantsForUsers.add(it) }
                    mRecyclerView.adapter = MembersAdapter(participantsForUsers,
                            this)
                }
            }

        })

        return us
    }



}
