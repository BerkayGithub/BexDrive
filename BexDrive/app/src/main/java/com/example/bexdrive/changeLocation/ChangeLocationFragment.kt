package com.example.bexdrive.changeLocation

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.bexdrive.R

class ChangeLocationFragment : Fragment() {

    companion object {
        fun newInstance() = ChangeLocationFragment()
    }

    private lateinit var viewModel: ChangeLocationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.change_location_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ChangeLocationViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
