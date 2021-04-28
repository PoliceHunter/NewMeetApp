package com.example.newmeetapp.ui.creating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.newmeetapp.R
import com.google.android.material.textfield.TextInputLayout


class Creating : Fragment() {

    companion object {
        fun newInstance() = Creating()
    }

    private lateinit var viewModel: CreatingViewModel

    // Отображает creating_fragment.xml
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.creating_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CreatingViewModel::class.java)
        // TODO: Use the ViewModel

        // возможно, нужно очищать поле или при мапинге в бд смотреть на состояние свитч
        view?.let { switchParticipantsCountVisibility(it) }

    }

//    fun goToInvitations (view: View) {
//        val btNext = view.findViewById<Button>(R.id.bt_next)
//        btNext.setOnClickListener {
//                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, InvitingFragment()).addToBackStack(null).commit()
//            }
//    }


    private fun switchParticipantsCountVisibility (view: View) {
        view.findViewById<Switch>(R.id.switchEventParticipantsCount).setOnCheckedChangeListener { _, isChecked ->
            view.findViewById<TextInputLayout>(R.id.textInputLayoutEventParticipantsCount).visibility = if (isChecked) View.VISIBLE
            else View.GONE
        }
    }


}