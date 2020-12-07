package com.example.bexdrive.activity

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.bexdrive.R
import com.example.bexdrive.changeLocation.ChangeLocationFragment
import com.example.bexdrive.currentService.tobevisitedAddress.ToBeVisitedAddress
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_change_location.*
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class ChangeLocationActivity : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_location)

        val configuration = resources.configuration
        val orientation = configuration.orientation
        val smallestScreenWidth = configuration.smallestScreenWidthDp

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        change_Location_drawer_menu.setNavigationItemSelectedListener {
            when (it.itemId){
                R.id.item_current_service -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)}
                R.id.item_update_location -> { }
                R.id.item_settings -> Toast.makeText(this, "You clicked on settings!", Toast.LENGTH_LONG).show()
                R.id.item_logout -> logout()
                else -> {}
            }
            true
        }

        if ((smallestScreenWidth >= 600) && (orientation == Configuration.ORIENTATION_LANDSCAPE)){

        }else{
            toolbar_2.setTitleTextColor(resources.getColor(R.color.color_white))
            toolbar_2.title = "Konumu Güncelle"
            setSupportActionBar(toolbar_2)
            change_Location_drawer_menu.bringToFront()
            toggle = ActionBarDrawerToggle(this, drawer_Layout_2, toolbar_2 , R.string.open, R.string.close)
            drawer_Layout_2.addDrawerListener(toggle)
            toggle.syncState()
        }

        supportFragmentManager.beginTransaction().run {
            replace(R.id.changeLocation_fragment, ChangeLocationFragment())
            commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
