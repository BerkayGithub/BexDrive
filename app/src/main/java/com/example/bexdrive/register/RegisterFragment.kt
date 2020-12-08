package com.example.bexdrive.register


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import com.example.bexdrive.R
import com.example.bexdrive.databinding.RegisterFragmentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.register_fragment.*

@AndroidEntryPoint
class RegisterFragment : Fragment(){

    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var linearLayout : LinearLayout
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: RegisterFragmentBinding = RegisterFragmentBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        linearLayout = binding.linLayoutRegister
        progressBar = binding.registerProgressBar

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.successLiveData().observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }

        viewModel.snackbarLiveData().observe(viewLifecycleOwner) {
            val mySnackBar = Snackbar.make(layout_register, it, Snackbar.LENGTH_INDEFINITE)
            mySnackBar.setAction("Close") { mySnackBar.dismiss() }
            mySnackBar.show()
        }

        viewModel.messageLiveData().observe(viewLifecycleOwner) {
            txt_register_message.text = it
            if(progressBar.visibility == VISIBLE){
                progressBar.visibility = GONE
                linearLayout.visibility = VISIBLE
            }
        }

        viewModel.registerProgressLiveData().observe(viewLifecycleOwner){
            if (it == 1)
                reg_pBar1.visibility = VISIBLE
            if (it == 0)
                reg_pBar1.visibility = GONE
        }

        view?.let {view ->
            viewModel.navigateLoginPageLiveData().observe(viewLifecycleOwner) {
                if (it) {
                    val bundle = bundleOf("basicProxyToken" to viewModel.basicProxyToken)
                    Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment, bundle)
                }
            }
        }
    }

    override fun onStart() {
        val sharedPreferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        viewModel.sharedPreferences = sharedPreferences
        if(!sharedPreferences.getString("DeviceName", "").isNullOrEmpty() && !sharedPreferences.getString("DevicePassword", "").isNullOrEmpty()){
            viewModel.inputName = sharedPreferences.getString("DeviceName", "").toString()
            viewModel.inputPassword = sharedPreferences.getString("DevicePassword", "").toString()
            viewModel.checkDeviceRegister()
        }else{
            progressBar.visibility = GONE
            linearLayout.visibility = VISIBLE
        }

        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity() , arrayOf(Manifest.permission.READ_PHONE_STATE), 101)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==101 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }
    }

}
