package com.example.newmeetapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.creating_fragment.*
import org.w3c.dom.Text


class RegistrationActivity : AppCompatActivity(){
    lateinit var auth: FirebaseAuth
    val MIN_PASSWORD_LENGTH = 6
    private lateinit var databaseReference: DatabaseReference
    var database: FirebaseDatabase? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        register()
    }


    @SuppressLint("LongLogTag")
    private fun register()
    {
        registerButton.setOnClickListener {

            if (validation()) {
                Log.d("Переменные в базу - name", firstnameInput.text.toString())
                Log.d("Переменные в базу - lastname", lastnameInput.text.toString())
                auth.createUserWithEmailAndPassword(emailInput.text.toString(), passwordInput.text.toString()).addOnCompleteListener {
                            if (it.isSuccessful) {

                                val currentUser = auth.currentUser
                                val currentUserDb = databaseReference.child(currentUser?.uid!!)
                                currentUserDb.child("firstname").setValue(firstnameInput.text.toString())
                                currentUserDb.child("lastname").setValue(lastnameInput.text.toString())
                                currentUserDb.child("city").setValue(cityInput.text.toString())
                                currentUserDb.child("birthday").setValue(birthdayInput.text.toString())
                                currentUserDb.child("phone").setValue(phoneInput.text.toString())
                                currentUserDb.child("telegram").setValue(telegramInput.text.toString())
                                currentUserDb.child("instagram").setValue(instagramInput.text.toString())
                                currentUserDb.child("vk").setValue(VKInput.text.toString())
                                currentUserDb.child("about").setValue(editText_about.text.toString())
                                currentUserDb.child("id").setValue(currentUser.uid)
                                Toast.makeText(this, "Вы успешно зарегистрировались!", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
                                finish()
                            } else {
                                Log.w("createUserWithEmail:failure", it.exception)
                                Toast.makeText(this@RegistrationActivity, "Регистрация не удалась, пожалуйста, попробуйте ещё раз!", Toast.LENGTH_SHORT).show()
                            }
                        }
            }
        }
    }


    private fun validation() : Boolean
    {

        if (TextUtils.isEmpty(firstnameInput.text.toString()))
        {
            firstnameInput.error = "Введите Ваше имя"
            return false
        }

        else if (TextUtils.isEmpty(lastnameInput.text.toString()))
        {
            lastnameInput.error = "Введите Вашу фамилию"
            return false
        }
        else if (TextUtils.isEmpty(emailInput.text.toString()))
        {
            emailInput.error = "Введите Ваш email"
            return false
        }
        else if (TextUtils.isEmpty(passwordInput.text.toString()))
        {
            passwordInput.error = "Введите пароль"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput.text.toString()).matches()) {
            emailInput.error = "Введите корректный email"
            return false
        }
        if (emailInput.text.toString().split('@')[1] != "gmail.com")
        {
            emailInput.error = "Введите Вашу корпоративную почту"
            return false
        }
        if (passwordInput.text.toString().length < MIN_PASSWORD_LENGTH) {
            passwordInput.error = "Пароль должен содержать не менее " + MIN_PASSWORD_LENGTH + " символов"
            return false
        }
        if (passwordInput.text.toString() != repeatpasswordInput.text.toString())
        {
            repeatpasswordInput.error = "Пароли не совпадают"
            return false
        }
        return true

    }
}