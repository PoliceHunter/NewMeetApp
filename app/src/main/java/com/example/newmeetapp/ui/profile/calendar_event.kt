package com.example.newmeetapp.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newmeetapp.R
import com.example.newmeetapp.ui.eventInfo.EventInfo
import com.example.newmeetapp.ui.events.Events
import com.example.newmeetapp.ui.events.MyAdapter
import com.example.newmeetapp.ui.events.OnEventListener
import com.example.newmeetapp.ui.events.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [calendar_event.newInstance] factory method to
 * create an instance of this fragment.
 */
class calendar_event : Fragment(), OnEventListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var mRecyclerView : RecyclerView
    lateinit var listEvents : ArrayList<Events>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar_event, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val current_user = FirebaseAuth.getInstance().currentUser!!.uid
        val db = FirebaseDatabase.getInstance().getReference("my_event/$current_user")

        mRecyclerView = activity?.findViewById(R.id.profileEventListId)!!
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mRecyclerView.setHasFixedSize(true)


        db.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                writeToRecycle(snapshot.children)
            }

        })
    }

    private fun writeToRecycle(id_snap: MutableIterable<DataSnapshot>) {
        val eventDb = FirebaseDatabase.getInstance().getReference("events")

        eventDb.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(eventSnap: DataSnapshot) {
                listEvents = arrayListOf<Events>()
                for (event_id in id_snap)
                {
                    val event = eventSnap.child("${event_id.key}").getValue(Events::class.java)
                    if (event != null) {

                        event.getAdminData(event)
                        listEvents.add(event)
                    }
                }
                mRecyclerView.adapter = MyAdapter(listEvents, this@calendar_event)
                eventDb.removeEventListener(this)
            }

        })
    }

    override fun onEventClick(position: Int) {
        super.onEventClick(position)

        val intent = Intent(requireContext(), EventInfo::class.java)
        intent.putExtra("event", listEvents[position])

        startActivity(intent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment calendar_event.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            calendar_event().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

    }
}