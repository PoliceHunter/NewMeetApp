package com.example.newmeetapp.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newmeetapp.R
import com.example.newmeetapp.ui.events.Events
import com.example.newmeetapp.ui.events.inRelative
import com.example.newmeetapp.ui.events.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class NotificationsFragment : Fragment(), onNotifListener {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var auth : FirebaseAuth
    private lateinit var notifDb : DatabaseReference
    private lateinit var eventDb : DatabaseReference
    private lateinit var profileDb : DatabaseReference
    private lateinit var notifList : ArrayList<Events>
    lateinit var mRecyclerView : RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        notificationsViewModel =
                ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)

        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        notifDb = FirebaseDatabase.getInstance().getReference("notification/${auth.currentUser!!.uid}")
        eventDb = FirebaseDatabase.getInstance().getReference("events")
        profileDb = FirebaseDatabase.getInstance().getReference("profile")
        mRecyclerView = activity?.findViewById(R.id.notifRecycle)!!
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mRecyclerView.setHasFixedSize(true)

        notifDb.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                get_event_with_notif(snapshot.children)
            }

        })
    }

    private fun get_event_with_notif(children: MutableIterable<DataSnapshot>) {

        eventDb.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                notifList = arrayListOf()
//                var whoInvite: user? = null
                for (i in children) {
                    val event = snapshot.child("${i.key}").getValue<Events>(Events::class.java)

                    if (event != null)
                    {
                        if (i.value == event.admin)
                            event.getAdminData(event)
//                        else
//                            whoInvite = event.getUserData(event, i.value as String?) //TODO В будущем если приглашение идет не
//                             //TODO только от администратора события
                        notifList.add(event)
                    }
                }
                mRecyclerView.adapter = NotifAdapter(notifList, this@NotificationsFragment)

                ///TODO add to recycle


            }

        })
    }


}