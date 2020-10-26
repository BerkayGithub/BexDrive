package com.example.bexdrive.register

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController


import com.example.bexdrive.R
import com.example.bexdrive.databinding.RegisterFragmentBinding
import com.example.bexdrive.listener.RegisterListener
import com.example.bexdrive.login.LoginFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.register_fragment.*
import kotlinx.android.synthetic.main.register_fragment.view.*

@AndroidEntryPoint
class RegisterFragment : Fragment() , RegisterListener{

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private val viewModel: RegisterViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.register_fragment, container, false)
        viewModel.registerListener = this
        viewModel.activity = requireActivity()

        viewModel.varNav1.observe(viewLifecycleOwner, Observer<Boolean> {
            if(!it) {
                requireActivity().findNavController(R.id.login_frame).navigate(R.id.action_registerFragment_to_loginFragment)
                //requireActivity().supportFragmentManager.beginTransaction().replace(R.id.login_frame, LoginFragment()).commit()
            }
        })

        val binding: RegisterFragmentBinding = DataBindingUtil.setContentView(requireActivity(), R.layout.register_fragment)
        binding.viewmodel = viewModel

        displayPhoneInfo()



        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStarted() {
    }

    override fun onSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onFailure(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun displayPhoneInfo(){

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity() , arrayOf(Manifest.permission.READ_PHONE_STATE), 101);
            return
        } else{
            viewModel.manufacturer = Build.MANUFACTURER
            viewModel.serial = Build.getSerial()
            viewModel.UUID = Build.ID
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
            viewModel.manufacturer = Build.MANUFACTURER
            viewModel.serial = Build.getSerial()
            viewModel.UUID = Build.ID
        }
    }

}
