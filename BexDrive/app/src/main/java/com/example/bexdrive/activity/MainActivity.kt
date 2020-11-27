package com.example.bexdrive.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
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
}
