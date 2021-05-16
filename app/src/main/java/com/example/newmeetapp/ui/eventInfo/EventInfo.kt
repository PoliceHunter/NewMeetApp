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
import com.example.newmeetapp.ui.events.*
import com.example.newmeetapp.ui.profile.ProfileOtherUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.event_info.*
import java.util.*
import kotlin.collections.ArrayList

class EventInfo : AppCompatActivity(), OnMemberListener {

    lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var usersReference: DatabaseReference

    private lateinit var participantsForUsersToRelative : ArrayList<inRelative>
    private lateinit var participantsForAdminToRelative : ArrayList<inRelative>
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
                "Образование" -> layoutCategoryEventInfoId.setBackgroundColor(resources?.getColor(R.color.orange_category_study)!!)
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
                    when (isWriteBool) {
                        null -> {
                            databaseReference = FirebaseDatabase.getInstance()
                                    .getReference("members/${bundle.id}/$currentUser")
                            databaseReference.setValue("false")
                            Toast.makeText(this, "You will be added to this event", Toast.LENGTH_SHORT)
                                    .show()
                        }
                        false -> Toast.makeText(this, "You already added to this event. Wait to accept", Toast.LENGTH_SHORT)
                                .show()
                        true -> Toast.makeText(this, "You participant this event", Toast.LENGTH_SHORT)
                                .show()
                    }
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()

        val bundle = intent.getSerializableExtra("event") as? Events
        mRecyclerView = findViewById(R.id.membersRV)

        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.setHasFixedSize(true)
        getUsersData(bundle!!.id)

    }


    private fun getUsersData(id: String?)
    {
        usersReference = FirebaseDatabase.getInstance().getReference("members/$id")


        usersReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot)
            {
                getUser(snapshot.children, id)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onDeleteMember(position: Int)
    {
        super.onDeleteMember(position)
        val db = FirebaseDatabase.getInstance()
                .getReference("members/${participantsForAdminToRelative[position].idEvent}/" +
                        "${participantsForAdminToRelative[position].User.id}")
        db.removeValue()
        //participantsForAdminToRelative.removeAt(position)

        //updateMembers(participantsForAdminToRelative)
    }

    override fun onAcceptMember(position: Int){
        super.onAcceptMember(position)
        val db = FirebaseDatabase.getInstance()
                .getReference("members/${participantsForAdminToRelative[position].idEvent}/" +
                        "${participantsForAdminToRelative[position].User.id}")
        db.setValue("true")
      //  participantsForAdminToRelative[position].value = true
        //updateMembers(participantsForAdminToRelative)
    }

    override fun onMemberClick(position: Int) {
        super.onMemberClick(position)
        val intent = Intent(this, ProfileOtherUser::class.java)

        if (auth.currentUser!!.uid == admin){
            intent.putExtra("event", participantsForAdminToRelative[position].User)
        } else
            intent.putExtra("event", participantsForUsersToRelative[position].User)
        startActivity(intent)
    }


    @SuppressLint("LongLogTag")
    private fun getUser(
        data: MutableIterable<DataSnapshot>,
        id: String?
    ) {
        val userReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("profile")

        userReference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                participantsForAdminToRelative = arrayListOf()
                participantsForUsersToRelative = arrayListOf()
                for (i in data) {
                    var result: inRelative? = null
                    val us = snapshot.child("${i.key}").getValue<user>(user::class.java)
                    val visible: Boolean = i.value == "true"
                    result = inRelative(User = us!!, value = visible, idEvent = id!!)
                    if (admin == auth.currentUser!!.uid)
                        participantsForAdminToRelative.add(result)
                    else if (i.value == "true" && admin != auth.currentUser!!.uid)
                        participantsForUsersToRelative.add(result)
                }
                if (auth.currentUser!!.uid == admin)
                    mRecyclerView.adapter = MembersAdapter(participantsForAdminToRelative, admin,this@EventInfo)
                else
                    mRecyclerView.adapter = MembersAdapter(participantsForUsersToRelative, admin, this@EventInfo)
                (mRecyclerView.adapter as MembersAdapter).notifyDataSetChanged()
            }

        })


        ///TODO Переписать без слушателя
    }

}
