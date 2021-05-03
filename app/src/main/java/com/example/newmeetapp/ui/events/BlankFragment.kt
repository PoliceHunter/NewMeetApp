package com.example.newmeetapp.ui.events

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newmeetapp.R
import com.example.newmeetapp.ui.creating.CreatingViewModel
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.event_list_fragment.*
import kotlinx.android.synthetic.main.event_list_fragment.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var mRecyclerView : RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var eventArrayList : ArrayList<Events>
    var database: FirebaseDatabase? = null

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

//        val rootView = inflater.inflate(R.layout.event_list_fragment, container, false)
//
//        rootView.eventListId.layoutManager = LinearLayoutManager(requireContext()) //activity
//        rootView.eventListId.adapter = Event(fillList())
//        return rootView
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.event_list_fragment, container, false)
    }

    private fun getEventsData()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("events")

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (eventSnapshot in snapshot.children)
                    {
                        val event = eventSnapshot.getValue(Events::class.java)
                        eventArrayList.add(event!!)
                    }
                    mRecyclerView.adapter = MyAdapter(eventArrayList)
                }
            }

        })
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mRecyclerView = activity?.findViewById(R.id.eventListId)!!

        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mRecyclerView.setHasFixedSize(true)
        eventArrayList = arrayListOf<Events>()
        getEventsData()

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