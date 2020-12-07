package com.example.bexdrive.changeLocation

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.bexdrive.R
import com.example.bexdrive.databinding.ChangeLocationFragmentBinding
import com.example.bexdrive.entity.Point
import com.example.bexdrive.util.PointAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.change_location_fragment.*

@AndroidEntryPoint
class ChangeLocationFragment : Fragment() {

    private val viewModel: ChangeLocationViewModel by viewModels()
    private lateinit var pointList: List<Point>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ChangeLocationFragmentBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        val sharedPreferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        viewModel.sharedPreferences = sharedPreferences

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.errorMessageLiveData().observe(viewLifecycleOwner){
            val snackBar = Snackbar.make(layout_change_Location, it, Snackbar.LENGTH_LONG)
            snackBar.setAction("OK"){
                snackBar.dismiss()
            }
            snackBar.show()
        }

        viewModel.pointListLiveData().observe(viewLifecycleOwner){
            pointList = it
            val sharedPreferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)

            val viewManager = LinearLayoutManager(requireContext())
            val pointAdapter = PointAdapter(pointList, sharedPreferences, requireActivity(), requireView())
            changeLoc_recyclerview.apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = pointAdapter
            }
        }
    }
}
