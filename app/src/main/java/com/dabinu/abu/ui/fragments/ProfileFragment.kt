package com.dabinu.abu.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.dabinu.abu.R
import com.dabinu.abu.databinding.FragmentProfileBinding
import com.dabinu.abu.models.Account
import com.dabinu.abu.viewmodels.AuthViewModel


class ProfileFragment : Fragment() {


    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewmodel: AuthViewModel
    private lateinit var navController: NavController

    private var myProfile = Account()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        viewmodel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        navController = Navigation.findNavController(container!!)

        observe()
        setOnClickListeners()

        return binding.root

    }

    private fun observe() {
        viewmodel.getProfileFromRoom.observe(viewLifecycleOwner, {
            it?.let { list -> myProfile = list[0]; binding.profile = myProfile }
        })
    }

    private fun setOnClickListeners() {

        with(binding) {

            btnContinue.setOnClickListener {
                viewmodel.logout()
                Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_LONG).show()
                navController.navigate(R.id.action_profileFragment_to_currencyConversionFragment)
            }

            cbSubscribed.setOnCheckedChangeListener { _, isChecked ->
                viewmodel.updateSubscriptionStatusOnRoom(isChecked, myProfile.email)
                viewmodel.updateSubscriptionStatusToFirebase(isChecked)
                myProfile.hasSubscribed = isChecked
                Toast.makeText(requireContext(), "Subscription status updated", Toast.LENGTH_LONG).show()
            }

            ibBack.setOnClickListener { navController.navigateUp() }
        }
    }

}