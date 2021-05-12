package com.example.newmeetapp.ui.profile

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newmeetapp.R
import com.example.newmeetapp.ui.events.user
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileOtherUser : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_other_user)

        val bundle = intent.getSerializableExtra("event") as? user

        if (bundle != null)
        {
            nameId.text = "${bundle.firstname}  ${bundle.lastname}"
        }
    }
}