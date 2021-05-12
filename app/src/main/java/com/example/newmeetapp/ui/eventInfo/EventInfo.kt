package com.example.newmeetapp.ui.eventInfo

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newmeetapp.R
import com.example.newmeetapp.ui.events.Events
import com.example.newmeetapp.ui.events.OnMemberListener
import com.example.newmeetapp.ui.events.user
import com.example.newmeetapp.ui.profile.Profile
import com.example.newmeetapp.ui.profile.ProfileOtherUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.event_info.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class EventInfo : AppCompatActivity(), OnMemberListener {

    lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var usersReference: DatabaseReference
    private lateinit var participantsForAdmin : ArrayList<user>
    private lateinit var participantsForUsers : ArrayList<user>
    lateinit var mRecyclerView : RecyclerView
    lateinit var admin : String
    private lateinit var etUnicalID : UUID



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.event_info)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!.uid
        val bundle = intent.getSerializableExtra("event") as? Events
        etUnicalID = UUID.randomUUID()
        participantsForAdmin = arrayListOf<user>()
        participantsForUsers = arrayListOf<user>()
        if (bundle != null) {
           // getAdminInfo(bundle.admin)
            eventNameInfoId.text = bundle.name
            eventCategoryInfoId.text = bundle.category
            when (bundle.gender) {
                "Женский" -> GenderMaleImageInfoId.visibility = GONE
                "Мужской" -> GenderFemaleImageInfoId.visibility = GONE
            }
            NumberParticipantsInfoId.text = bundle.count_participants
            eventDateTimeInfoId.text = "${bundle.time}  ${bundle.date}"
            eventDescriptionInfoId.text = bundle.details
            OrgNameInfoId.text = "${bundle.adminInfo?.firstname} ${bundle.adminInfo?.lastname}"
            admin = bundle.admin!!
            when (bundle.category) {
                "Прогулка" -> layoutCategoryEventInfoId.setBackgroundColor(Resources.getSystem().getColor(R.color.blue_category_walk))
                "Активный отдых" -> layoutCategoryEventInfoId.setBackgroundColor(resources?.getColor(R.color.green_category_sport)!!)
                "Еда" -> layoutCategoryEventInfoId.setBackgroundColor(resources?.getColor(R.color.red_category_dinner)!!)
                "Культура" -> layoutCategoryEventInfoId.setBackgroundColor(resources?.getColor(R.color.purple_category_culture)!!)
                "Путешествия" -> layoutCategoryEventInfoId.setBackgroundColor(resources?.getColor(R.color.turquoise_category_travel)!!)
                "Образование" -> layoutCategoryEventInfoId.setBackgroundColor(resources?.getColor(R.color.lightBlue_category_study)!!)
                "Посиделки" -> layoutCategoryEventInfoId.setBackgroundColor(resources?.getColor(R.color.yellow_category_company)!!)
                "Прочее" -> layoutCategoryEventInfoId.setBackgroundColor(resources?.getColor(R.color.pink_category_other)!!)
            }
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
                    if (it.value == "false")
                        isWriteBool = false
                    Log.i("firebase", "Got value ${it.value}")

                }.addOnFailureListener{
                    Log.e("firebase", "Error getting data", it)
                }

                bt_go.setOnClickListener {
                    if (isWriteBool == null) {
                        databaseReference = FirebaseDatabase.getInstance()
                            .getReference("members/${bundle.id}/$currentUser")
                        databaseReference.setValue("false")
                        Toast.makeText(this, "You will be added to this event", Toast.LENGTH_SHORT)
                            .show()
//                        val writeUser = FirebaseDatabase.getInstance().getReference("profile/$currentUser")
//                        writeUser.get().addOnSuccessListener {
//                            if (it.value != null) {
//                                val hash : HashMap<String, String> = it.value as HashMap<String, String>
//                                val us = user(firstname = hash["firstname"], lastname = hash["lastname"], id = hash["id"])
//                                participantsForAdmin.add(us)
//                                mRecyclerView.adapter = MembersAdapter(participantsForUsers, true, this@EventInfo)
//                            }
//                        }
                    }
                    else if (isWriteBool == false)
                        Toast.makeText(this, "You already added to this event. Wait to accept", Toast.LENGTH_SHORT)
                            .show()
                    else if (isWriteBool == true)
                        Toast.makeText(this, "You participant this event", Toast.LENGTH_SHORT)
                            .show()
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
//        val bundle = intent.getSerializableExtra("event") as? Events
//        mRecyclerView = findViewById(R.id.membersRV)!!
//
//        mRecyclerView.layoutManager = LinearLayoutManager(this)
//        mRecyclerView.setHasFixedSize(true)
//        participantsForAdmin = arrayListOf<user>()
//        participantsForUsers = arrayListOf<user>()
//        getUsersData(bundle?.id)
//
    }

    override fun onResume() {
        super.onResume()
        val bundle = intent.getSerializableExtra("event") as? Events
        mRecyclerView = findViewById(R.id.membersRV)!!

        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.setHasFixedSize(true)
        getUsersData(bundle?.id)
    }

    private fun getUsersData(id: String?)
    {
        usersReference = FirebaseDatabase.getInstance().getReference("members/$id")

        usersReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (users in snapshot.children)
                {
                    getUser(users.key, users.value)
                }

            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onMemberClick(position: Int) {
        super.onMemberClick(position)
        val intent = Intent(this, ProfileOtherUser::class.java)

        if (auth.currentUser!!.uid == admin) {
            intent.putExtra("event", participantsForAdmin[position])
        } else
            intent.putExtra("event", participantsForUsers[position])
        startActivity(intent)
    }

    private fun getUser(key: String?, value: Any?) {

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
                    if (value == "false")
                        mRecyclerView.adapter = MembersAdapter(participantsForAdmin, false, this@EventInfo)
                    else
                        mRecyclerView.adapter = MembersAdapter(participantsForAdmin, true, this@EventInfo)
                }
                else if (value == "true")
                {
                    us?.let { participantsForUsers.add(it) }
                    mRecyclerView.adapter = MembersAdapter(participantsForUsers, true, this@EventInfo)
                }
            }

        })

    }





}
