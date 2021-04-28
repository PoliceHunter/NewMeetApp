package com.example.newmeetapp.ui.inviting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.newmeetapp.R
import com.example.newmeetapp.ui.creating.Creating
import com.example.newmeetapp.ui.creating.CreatingViewModel

class InvitingFragment : Fragment() {

    companion object {
        fun newInstance() = InvitingFragment()
    }

    // Отображает inviting_fragment.xml
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.invite_fragment, container, false)

    }
}

