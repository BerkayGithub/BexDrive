package com.example.bexdrive.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.bexdrive.R
import com.example.bexdrive.currentService.detail.DetailFragment
import com.example.bexdrive.currentService.map.MapFragment
import com.example.bexdrive.currentService.tobevisitedAddress.ToBeVisitedAddress
import com.example.bexdrive.currentService.visitedAddresses.VisitedAddressFragment
import com.example.bexdrive.util.ViewPageAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var toggle : ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val configuration = resources.configuration
        val orientation = configuration.orientation
        val smallestScreenWidth = configuration.smallestScreenWidthDp

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        main_drawer_menu.setNavigationItemSelectedListener {
            when (it.itemId){
                R.id.item_current_service -> {}
                R.id.item_update_location -> {
                    val intent = Intent(this, ChangeLocationActivity::class.java)
                    startActivity(intent)
                }
                R.id.item_settings -> Toast.makeText(this, "You clicked on settings!", Toast.LENGTH_LONG).show()
                R.id.item_logout -> logout()
                else -> {}
            }
            true
        }

        if((smallestScreenWidth >= 600) && (orientation == Configuration.ORIENTATION_LANDSCAPE)){

        } else {
            toolbar.setTitleTextColor(resources.getColor(R.color.color_white))
            setSupportActionBar(toolbar)

            main_drawer_menu.bringToFront()
            toggle = ActionBarDrawerToggle(this, drawer_Layout, toolbar, R.string.open, R.string.close)
            drawer_Layout.addDrawerListener(toggle)
            toggle.syncState()
        }

        val adapter = ViewPageAdapter(supportFragmentManager)
        adapter.addFragment(ToBeVisitedAddress(), "Ziyaret Edilecek Adresler")
        adapter.addFragment(MapFragment(), "Harita")
        adapter.addFragment(VisitedAddressFragment(), "Uğranan Adresler")
        adapter.addFragment(DetailFragment(), "Detay")
        main_viewpager.adapter = adapter
        main_tabLayout.setupWithViewPager(main_viewpager)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout(){
        val intent = Intent(applicationContext, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
