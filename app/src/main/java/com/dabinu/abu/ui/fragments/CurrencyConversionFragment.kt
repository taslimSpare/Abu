package com.dabinu.abu.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.dabinu.abu.R
import com.dabinu.abu.databinding.FragmentCurrencyConversionBinding
import com.dabinu.abu.models.State
import com.dabinu.abu.ui.activities.MainActivity
import com.dabinu.abu.viewmodels.FixerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


@Suppress("DEPRECATION")
class CurrencyConversionFragment : Fragment() {


    private lateinit var binding: FragmentCurrencyConversionBinding
    private val viewmodel by viewModel<FixerViewModel>()
    private lateinit var navController: NavController
    private var allSymbols: MutableList<String> = mutableListOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_currency_conversion, container, false)
        navController = Navigation.findNavController(container!!)

        setupViews()
        setOnClickListeners()
        observe()

        return binding.root

    }


    private fun setupViews() {

        val myString = SpannableString(getString(R.string.currencyCalculator))
        myString.setSpan(ForegroundColorSpan(resources.getColor(R.color.app_green)), myString.indexOf("."), myString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvHeader.text = myString
        binding.tvHeader.movementMethod = LinkMovementMethod.getInstance()

        viewmodel.fetchAllSymbols()
    }


    private fun setOnClickListeners() {

        with(binding) {

            ibMenu.setOnClickListener {
                (requireActivity() as MainActivity).openDrawer()
            }

            btnContinue.setOnClickListener {
                viewmodel.convert(
                    binding.tvFromCurrency.text.toString(),
                    binding.tvToCurrency.text.toString(),
                    binding.etFromCurrency.text.toString().trim().toDouble()
                    )
            }

        }

    }


    @SuppressLint("SetTextI18n")
    private fun observe() {

        viewmodel.getProfileFromRoom.observe(viewLifecycleOwner, {

            if(it.isNotEmpty()) {
                binding.tvName.text = "Hi ${it[0].name}"
                binding.tvName.setOnClickListener {  }
            }

            else {
                binding.tvName.text = getString(R.string.signup)
                binding.tvName.setOnClickListener { navController.navigate(R.id.action_currencyConversionFragment_to_signInFragment) }
            }
        })


        viewmodel.getSymbolsLiveData().observe(viewLifecycleOwner, {

            when(it.state) {

                State.LOADING -> binding.layoutLoading.root.visibility = View.VISIBLE

                State.ERROR -> viewmodel.fetchAllSymbols()

                State.SUCCESS -> {

                    binding.layoutLoading.root.visibility = View.GONE

                    allSymbols.clear()
                    it?.data?.symbols?.keys?.forEach { k -> allSymbols.add(k) }

                    loadSpinners()

                }
            }

        })


        viewmodel.getConvertLiveData().observe(viewLifecycleOwner, {

            when (it.state) {

                State.LOADING -> binding.layoutLoading.root.visibility = View.VISIBLE

                State.ERROR -> {
                    binding.layoutLoading.root.visibility = View.GONE
                    Toast.makeText(requireContext(), "Something went wrong, check your internet connection", Toast.LENGTH_LONG).show()
                }

                State.SUCCESS -> {
                    binding.layoutLoading.root.visibility = View.GONE
                    binding.etToCurrency.text = it?.data?.result.toString()
                }
            }
        })
    }


    private fun loadSpinners() {

        val fromAdapter = ArrayAdapter(requireContext(), R.layout.layout_spinner, allSymbols)
        fromAdapter.setDropDownViewResource(R.layout.layout_spinner_drop_down)
        binding.spFromCurrency.adapter = fromAdapter

        val toAdapter = ArrayAdapter(requireContext(), R.layout.layout_spinner, allSymbols)
        toAdapter.setDropDownViewResource(R.layout.layout_spinner_drop_down)
        binding.spToCurrency.adapter = toAdapter


        if(allSymbols.size >= 2) {
            binding.spFromCurrency.setSelection(0)
            binding.tvFromCurrency.text = binding.spFromCurrency.selectedItem as String
            binding.spToCurrency.setSelection(1)
            binding.tvToCurrency.text = binding.spToCurrency.selectedItem as String
        }


        binding.spFromCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.tvFromCurrency.text = allSymbols[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }

        binding.spToCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.tvToCurrency.text = allSymbols[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }

    }


}
