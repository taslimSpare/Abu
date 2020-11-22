package com.dabinu.abu.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dabinu.abu.R
import com.dabinu.abu.databinding.FragmentAllCurrenciesBinding
import com.dabinu.abu.models.State
import com.dabinu.abu.ui.adapters.AllCurrenciesAdapter
import com.dabinu.abu.viewmodels.FixerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class AllCurrenciesFragment : Fragment() {


    private lateinit var binding: FragmentAllCurrenciesBinding
    private val viewmodel by viewModel<FixerViewModel>()
    private lateinit var navController: NavController
    private val currencyList = arrayListOf<String>()
    private val adapter by lazy { AllCurrenciesAdapter(currencyList) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_currencies, container, false)
        navController = Navigation.findNavController(container!!)

        setupViews()
        setOnClickListeners()
        observe()

        return binding.root

    }


    private fun setupViews() {

        binding.rvAllCurrencies.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvAllCurrencies.adapter = adapter

        viewmodel.fetchAllSymbols()

    }



    private fun setOnClickListeners() {

        with(binding) {

            ibBack.setOnClickListener { navController.navigateUp() }

            tvError.setOnClickListener { viewmodel.fetchAllSymbols() }
        }

    }


    private fun observe() {

        viewmodel.getSymbolsLiveData().observe(viewLifecycleOwner, {

            when (it.state) {

                State.LOADING -> {
                    binding.pbProgressBar.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                }

                State.ERROR -> {
                    binding.pbProgressBar.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                }

                State.SUCCESS -> {
                    binding.pbProgressBar.visibility = View.GONE
                    binding.tvError.visibility = View.GONE

                    val keys = it?.data?.symbols?.keys?.toList()
                    val values = it?.data?.symbols?.values?.toList()

                    currencyList.clear()
                    keys?.forEach { d -> currencyList.add("${d}___${values?.get(keys.indexOf(d))}") }
                    adapter.notifyDataSetChanged()

                }
            }
        })
    }

}