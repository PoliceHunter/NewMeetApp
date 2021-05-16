package com.example.newmeetapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        loginIn()
    }

    private fun loginIn()
    {
        loginButton.setOnClickListener {
            if (validation()) {
                auth.signInWithEmailAndPassword(emailLoginInput.text.toString(), passwordLoginInput.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Указан неверный email или пароль",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            }
        }
    }

    private fun validation() : Boolean
    {
        if (TextUtils.isEmpty(emailLoginInput.text.toString()))
        {
            emailLoginInput.error = "Введите Ваш email"
            return false
        }
        else if (TextUtils.isEmpty(passwordLoginInput.text.toString()))
        {
            passwordLoginInput.error = "Введите пароль"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailLoginInput.text.toString()).matches()) {
            emailLoginInput.error = "Введите корректный email"
            return false
        }
        if (emailLoginInput.text.toString().split('@')[1] != "gmail.com")
        {
            emailLoginInput.error = "Введите Вашу корпоративную почту"
            return false
        }
        return true
    }

    fun goToRegisterPage(view: View)
    {
        val pageRegister = Intent(this, RegistrationActivity::class.java)
        startActivity(pageRegister)
    }
}