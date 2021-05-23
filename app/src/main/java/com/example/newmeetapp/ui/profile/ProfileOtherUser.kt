package com.example.newmeetapp.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.newmeetapp.R
import com.example.newmeetapp.ui.events.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_profile_other_user.*
import kotlinx.android.synthetic.main.profile_fragment.AboutId
import kotlinx.android.synthetic.main.profile_fragment.BirthDayId
import kotlinx.android.synthetic.main.profile_fragment.CityId
import kotlinx.android.synthetic.main.profile_fragment.InstagramId
import kotlinx.android.synthetic.main.profile_fragment.PhoneId
import kotlinx.android.synthetic.main.profile_fragment.TelegramId
import kotlinx.android.synthetic.main.profile_fragment.VkId
import kotlinx.android.synthetic.main.profile_fragment.nameId

class ProfileOtherUser : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_other_user)


        val bundle = intent.getSerializableExtra("event") as? user
        auth = FirebaseAuth.getInstance()

        if (bundle != null)
        {
            nameId.text = "${bundle.firstname}  ${bundle.lastname}"
            CityId.text = bundle.city
            AboutId.text = bundle.about
            BirthDayId.text = bundle.birthday
            PhoneId.text = bundle.phone
            TelegramId.text =bundle.telegram
            InstagramId.text = bundle.instagram
            VkId.text = bundle.vk
        }
        val imageStorageRef = FirebaseStorage.getInstance().getReference("photo/${bundle?.id}")


        Glide.with(this@ProfileOtherUser)
                .load(bundle?.uri)
                .into(AvatarId)

//        imageStorageRef.downloadUrl.addOnCompleteListener { photoUri ->
//            if (photoUri.isSuccessful)
//                Glide.with(this@ProfileOtherUser)
//                        .load(photoUri.result)
//                        .into(OrgAvatarInfoId)
//        }
    }
}