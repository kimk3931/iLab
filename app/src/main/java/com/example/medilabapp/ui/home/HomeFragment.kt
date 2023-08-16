package com.example.medilabapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.medilabapp.databinding.FragmentHomeBinding
import com.example.medilabapp.helpers.PrefsHelper
import org.json.JSONObject

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //code here
        val userObject = PrefsHelper.getPrefs(requireContext(), "userObject")
        val user = JSONObject(userObject)//convert to JSON Object
        val surname = _binding!!.surname //findview
        surname.text = user.getString("surname")

        val others = _binding!!.others
        others.text = user.getString("others")

        val gender = _binding!!.gender
        gender.text = user.getString("gender")

        val dob = _binding!!.dob
        dob.text = user.getString("dob")

        val email = _binding!!.email
        email.text = user.getString("email")


        //gender, dob ,reg_date, email

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}