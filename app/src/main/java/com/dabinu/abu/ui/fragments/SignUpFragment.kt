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
import com.dabinu.abu.databinding.FragmentSignUpBinding
import com.dabinu.abu.models.STATE_FAILED
import com.dabinu.abu.models.STATE_LOADING
import com.dabinu.abu.models.STATE_SUCCESSFUL
import com.dabinu.abu.viewmodels.AuthViewModel


class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewmodel: AuthViewModel
    private lateinit var navController: NavController


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        viewmodel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        navController = Navigation.findNavController(container!!)

        setOnClickListeners()
        observe()

        return binding.root

    }


    private fun setOnClickListeners() {

        with(binding) {

            btnContinue.setOnClickListener {
                when {
                    name.text.toString().trim().isEmpty() -> name.error = "This field is required"
                    emailAddress.text.toString().trim().isEmpty() -> emailAddress.error = "This field is required"
                    password.text.toString().trim().isEmpty() -> password.error = "This field is required"
                    password.text.toString().trim() != confirmPassword.text.toString().trim() -> confirmPassword.error = "Passwords do not match"
                    else -> viewmodel.signInWithEmailPassword(emailAddress.text.toString().trim(), password.text.toString().trim())
                }
            }

            btnSignIn.setOnClickListener { navController.navigate(R.id.action_signUpFragment_to_signInFragment) }
        }
    }


    private fun observe() {

        viewmodel.getCreateAccountWithEmailLiveData().observe(viewLifecycleOwner, {

            when (it.state) {

                STATE_LOADING -> binding.layoutLoading.root.visibility = View.VISIBLE

                STATE_FAILED -> {
                    binding.layoutLoading.root.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }

                STATE_SUCCESSFUL -> {
                    binding.layoutLoading.root.visibility = View.GONE
                    navController.navigate(R.id.action_signInFragment_to_currencyConversionFragment)
                }
            }
        })
    }


}