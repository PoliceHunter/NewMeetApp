package com.example.newmeetapp.ui.profile

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.newmeetapp.LoginActivity
import com.example.newmeetapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.profile_fragment.*
import java.io.ByteArrayOutputStream

class Profile : Fragment() {

    companion object {
        fun newInstance() = Profile()
    }

    lateinit var auth: FirebaseAuth
    private lateinit var viewModel: ProfileViewModel
    private var firstname: String? = null
    private var lastname: String? = null
    private var city: String? = null
    private var about: String? = null
    private var birthday: String? = null
    private var phone: String? = null
    private var telegram: String? = null
    private var instagram: String? = null
    private var vk: String? = null
    var database: FirebaseDatabase? = null

    private lateinit var imageUri: Uri
    private val REQUEST_IMAGE_CAPTURE = 100


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        auth = FirebaseAuth.getInstance()

        fragmentManager?.beginTransaction()
            ?.replace(R.id.frame_fragment_id, calendar_event())?.commit()
        bt_UpcomingEvents.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_profile_calendar_filled)!!)


//        val baos = ByteArrayOutputStream()
//        val image = baos.toByteArray()
        val imageStorageRef = FirebaseStorage.getInstance().getReference("photo/${auth.currentUser!!.uid}")

        imageStorageRef.downloadUrl.addOnCompleteListener { photoUri ->
            if (photoUri.isSuccessful)
                Glide.with(this@Profile)
                    .load(photoUri.result)
                    .into(OrgAvatarInfoId)
        }

        OrgAvatarInfoId.setOnClickListener {
            takePictureIntent()
        }

        bt_UpcomingEvents.setOnClickListener {

            fragmentManager?.beginTransaction()
                ?.replace(R.id.frame_fragment_id, calendar_event())?.commit()
            bt_UpcomingEvents.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_profile_calendar_filled)!!)
            bt_PassedEvents.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_profile_archive)!!)
            bt_FavouritesEvents.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_favourite_event)!!)

        }
        bt_PassedEvents.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.frame_fragment_id, last_event())
                ?.commit()
            bt_UpcomingEvents.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_profile_calendar)!!)
            bt_FavouritesEvents.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_favourite_event)!!)
            bt_PassedEvents.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_profile_archive_filled)!!)
        }

        bt_FavouritesEvents.setOnClickListener() {
            fragmentManager?.beginTransaction()
                ?.replace(R.id.frame_fragment_id, LikeEvent())?.commit()
            bt_UpcomingEvents.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_profile_calendar)!!)
            bt_PassedEvents.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_profile_archive)!!)
            bt_FavouritesEvents.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_favourite_event_filled)!!)
        }

        buttonLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        getUserData()
        //TODO Разбить на части

    }

    private fun takePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->
            pictureIntent.resolveActivity(activity?.packageManager!!)?.also {
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap

            uploadImageAndSaveUri(imageBitmap)
        }

    }

    private fun uploadImageAndSaveUri(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val storage = FirebaseStorage.getInstance()
            .getReference("photo/${auth.currentUser!!.uid}")
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()

        val upload = storage.putBytes(image)


        progress_bar_profileImage.visibility = View.VISIBLE
        upload.addOnCompleteListener { uploadTask ->
            progress_bar_profileImage.visibility = View.INVISIBLE

            if (uploadTask.isSuccessful)
            {
                storage.downloadUrl.addOnCompleteListener { urlTask ->
                    urlTask.result?.let{
                        imageUri = it
                        Toast.makeText(requireContext(), imageUri.toString(), Toast.LENGTH_SHORT).show()
                        OrgAvatarInfoId.setImageBitmap(bitmap)
                        FirebaseDatabase.getInstance().getReference("profile/${auth.currentUser!!.uid}/uri").setValue(imageUri.toString())
                    }
                }
            }
            else
            {
                uploadTask.exception?.let {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


    @SuppressLint("SetTextI18n")
    private fun getUserData() {

        val currentUser = auth.currentUser
        val currentUserDb =
            FirebaseDatabase.getInstance().getReference("profile/${currentUser?.uid!!}")

        currentUserDb.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                firstname = snapshot.child("firstname").value.toString()
                lastname = snapshot.child("lastname").value.toString()
                nameId.text = "$firstname $lastname"

                CityId.text = snapshot.child("city").value.toString()
                AboutId.text = snapshot.child("about").value.toString()
                BirthDayId.text = snapshot.child("birthday").value.toString()
                PhoneId.text = snapshot.child("phone").value.toString()
                TelegramId.text = snapshot.child("telegram").value.toString()
                InstagramId.text = snapshot.child("instagram").value?.toString()
                VkId.text = snapshot.child("vk").value.toString()

                //TODO Add to class user new params and read with one param
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error access to db", "All bad")
            }
        })

    }

//    private fun likeClick() {
//        bt_FavouritesEvents.setOnClickListener {
//
//            val fr = getFragmentManager()?.beginTransaction()
//            fr?.replace(R.id.nav_event_fragment, last_event())
//            fr?.commit()
//        }
//    }

}

