package com.example.newmeetapp.ui.events

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newmeetapp.R
import com.example.newmeetapp.ui.eventInfo.EventInfo
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment : Fragment(), OnEventListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var mRecyclerView : RecyclerView
     lateinit var databaseReference: DatabaseReference
     lateinit var profileReference: DatabaseReference
    lateinit var userReference : DatabaseReference
     lateinit var eventArrayList : ArrayList<Events>
    private lateinit var usersArrayList: ArrayList<user>
    var database: FirebaseDatabase? = null
    lateinit var etValueEventListener : ValueEventListener

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


        return inflater.inflate(R.layout.event_list_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mRecyclerView = activity?.findViewById(R.id.eventListId)!!

        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mRecyclerView.setHasFixedSize(true)
        eventArrayList = arrayListOf<Events>()
        getEventsData()

    }

     fun getEventsData()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("events")
        etValueEventListener = databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    var event : Events? = null
                    for (eventSnapshot in snapshot.children)
                    {
                        event = eventSnapshot.getValue(Events::class.java)!!

                        getAdminData(event)
                        eventArrayList.add(event)
                    }

                    mRecyclerView.adapter = MyAdapter(eventArrayList, this@BlankFragment)
                    databaseReference.removeEventListener(this)
                }
            }

        })
    }


    fun getAdminData(event: Events)
     {
         database = FirebaseDatabase.getInstance()
         profileReference = database?.reference!!.child("profile/${event.admin}")

         profileReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val info = snapshot.getValue(user::class.java)
                event.adminInfo = info
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    override fun onEventClick(position: Int) {
        super.onEventClick(position)

        val intent = Intent(requireContext(), EventInfo::class.java)
        intent.putExtra("event", eventArrayList[position])
        startActivity(intent)
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BlankFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}