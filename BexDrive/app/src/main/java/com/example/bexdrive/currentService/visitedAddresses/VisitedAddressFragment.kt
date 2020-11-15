package com.example.bexdrive.currentService.visitedAddresses

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.bexdrive.R

class VisitedAddressFragment : Fragment() {

    companion object {
        fun newInstance() = VisitedAddressFragment()
    }

    private lateinit var viewModel: VisitedAddressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.visited_address_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(VisitedAddressViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
