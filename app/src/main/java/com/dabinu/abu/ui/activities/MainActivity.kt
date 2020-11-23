package com.dabinu.abu.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.findNavController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dabinu.abu.R
import com.dabinu.abu.models.DrawerItem
import com.dabinu.abu.ui.adapters.DrawerAdapter
import com.dabinu.abu.viewmodels.AuthViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_content.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {


    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navController: NavController
    private val authViewModel by viewModel<AuthViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
        observe()
        authViewModel.checkAuthentication()

    }


    private fun setupViews() {

        val drawerItems = mutableListOf<DrawerItem>().apply {
            add(DrawerItem(R.drawable.ic_home, getString(R.string.menu_home), true))
            add(DrawerItem(R.drawable.ic_countries, getString(R.string.menu_currencies), false))
            add(DrawerItem(R.drawable.ic_rate_us, getString(R.string.menu_rate_us), false))
            add(DrawerItem(R.drawable.ic_share_app, getString(R.string.menu_share_app), false))
        }

        val drawerAdapter by lazy { DrawerAdapter(this, drawerItems) {item ->
            drawer_layout.closeDrawers()

            when(item.icon) {
                R.drawable.ic_home -> navController.navigate(R.id.currencyConversionFragment)
                R.drawable.ic_countries -> navController.navigate(R.id.currenciesFragment)
                R.drawable.ic_rate_us -> { }
                R.drawable.ic_share_app -> { }
            }
        } }


        navController = findNavController( R.id.nav_host_fragment)

        toggle =
            ActionBarDrawerToggle(this, drawer_layout, R.string.app_name, R.string.app_name)
        toggle.syncState()

        rvDrawer.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvDrawer.adapter = drawerAdapter


        drawerHeader.setOnClickListener { navController.navigate(R.id.profileFragment); drawer_layout.closeDrawers() }
    }


    private fun observe() {

        authViewModel.getAuthStatusLiveData().observe(this, {

            when (it) {

                true -> {
                    btn_log_in.text = getString(R.string.logout)
                    btn_log_in.setOnClickListener { authViewModel.logout(); drawer_layout.closeDrawers(); Toast.makeText(this, "Logged out", Toast.LENGTH_LONG).show() }
                }
                false -> {
                    btn_log_in.text = getString(R.string.login)
                    btn_log_in.setOnClickListener { navController.navigate(R.id.signInFragment); drawer_layout.closeDrawers() }
                }
            }
        })

        authViewModel.getProfileFromRoom.observe(this, {
            if(it.isNotEmpty()) tvUsernameHomePage.text = it[0].name
            else { tvUsernameHomePage.text = "" }
        })
    }


    fun openDrawer() {
        drawer_layout.openDrawer(GravityCompat.START)
    }


}