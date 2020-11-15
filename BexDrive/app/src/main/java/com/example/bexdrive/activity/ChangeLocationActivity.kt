package com.example.bexdrive.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.bexdrive.R
import com.example.bexdrive.changeLocation.ChangeLocationFragment
import com.example.bexdrive.currentService.tobevisitedAddress.ToBeVisitedAddress
import kotlinx.android.synthetic.main.activity_change_location.*
import kotlinx.android.synthetic.main.activity_main.*

class ChangeLocationActivity : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_location)

        toolbar_2.setTitleTextColor(resources.getColor(R.color.color_white))
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        main_drawer_menu.bringToFront()

        toggle = ActionBarDrawerToggle(this, drawer_Layout_2, toolbar , R.string.open, R.string.close)
        drawer_Layout.addDrawerListener(toggle)
        toggle.syncState()

        supportFragmentManager.beginTransaction().run {
            replace(R.id.changeLocation_fragment, ChangeLocationFragment())
            commit()
        }
    }
}
