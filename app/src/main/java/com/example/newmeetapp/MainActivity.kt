package com.example.newmeetapp

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newmeetapp.ui.events.EventContent
import com.example.newmeetapp.ui.events.MyEventRecyclerViewAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.event_list_fragment.*
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var  MyEventRecyclerViewAdapter : MyEventRecyclerViewAdapter

    private val random: Int
        get() = Random().nextInt(9)

    private val bigRandom: Int
        get() = Random().nextInt(10000)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_search,
                R.id.navigation_mapsFragment,
                R.id.navigation_creating,
                R.id.navigation_notifications,
                R.id.navigation_profile))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        initRecyclerView()

        val event = EventContent.Event(
            eventId = bigRandom.toString(),
            eventName = getRandomName(),
            eventDate = getRandomDate(),
            eventTime = getRandomDate(),
            eventCategory = getRandomName()
        )
        MyEventRecyclerViewAdapter.setEvent(listOf(event))


    }



    private fun initRecyclerView() {
        MyEventRecyclerViewAdapter = MyEventRecyclerViewAdapter()

        with(eventListId) {
            this?.layoutManager = LinearLayoutManager(context)
            this?.adapter = MyEventRecyclerViewAdapter
            this?.setHasFixedSize(true)
        }
    }

    private fun listParticipants() {
        val names = arrayOf(
            "Иван", "Марья", "Петр", "Антон", "Даша", "Борис",
            "Костя", "Игорь", "Анна", "Денис", "Андрей"
        )
        var lvInviting: ListView = findViewById<View>(R.id.listViewInviting) as ListView

        val myParticipantsAdapter = ArrayAdapter(this, R.layout.list_participant_item, names)
        lvInviting.adapter = myParticipantsAdapter;
    }


    private fun getRandomName() = resources.getStringArray(R.array.names)[random]
    private fun getRandomDate() = resources.getStringArray(R.array.dates)[random]
 //   private fun getRandomAvatarUrl() = "https://i.pravatar.cc/150?img=$random"

//    fun goToInvitations (view: View) {
//        val btNext = view.findViewById<Button>(R.id.bt_next)
//        btNext.setOnClickListener {
//            supportFragmentManager.beginTransaction().replace(R.id.frame_fragment_id, InvitingFragment()).addToBackStack(null).commit()
//        }
//    }
}