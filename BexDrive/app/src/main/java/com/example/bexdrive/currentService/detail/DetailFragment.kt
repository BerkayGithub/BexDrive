package com.example.bexdrive.currentService.detail

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe

import com.example.bexdrive.R
import com.example.bexdrive.databinding.DetailFragmentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.detail_fragment.*

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DetailFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val sharedPreferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        viewModel.sharedPreferences = sharedPreferences

        viewModel.getService()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.errorMessageLiveData().observe(viewLifecycleOwner){
            detail_progress.visibility = View.GONE
            val mySnackBar = Snackbar.make(layout_service_detail, it, Snackbar.LENGTH_LONG)
            mySnackBar.setAction("Retry") {
                viewModel.getService()
                detail_progress.visibility = View.VISIBLE
            }
            mySnackBar.show()
        }

        viewModel.progressBarLiveData().observe(viewLifecycleOwner){
            detail_progress.visibility = View.GONE
            const_service_detail.visibility = View.VISIBLE
        }
    }

}
