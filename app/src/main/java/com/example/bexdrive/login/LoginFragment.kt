package com.example.bexdrive.login

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.example.bexdrive.DaggerClass
import com.example.bexdrive.activity.MainActivity
import com.example.bexdrive.databinding.LoginFragmentBinding
import com.example.bexdrive.listener.RegisterListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.login_fragment.*


@AndroidEntryPoint
class LoginFragment : Fragment(), RegisterListener {

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var myProgress: ProgressBar
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = LoginFragmentBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        if(arguments != null){
            viewModel.basicProxyToken = requireArguments().getString("basicProxyToken")!!
        }

        myProgress = ProgressBar(requireContext())
        progressBar = binding.loginProgressBar

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        header_login.text = DaggerClass.vehiclePlate

        viewModel.successLiveData().observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }

        viewModel.loginMyProgressLiveData().observe(viewLifecycleOwner){
            if(it == 1)
                login_pBar1.visibility = VISIBLE
            if(it == 0)
                if (progressBar.isVisible){
                    progressBar.visibility = GONE
                }
                login_pBar1.visibility = GONE
        }

        view.let { _ ->
            viewModel.navigateMainPageLiveData().observe(viewLifecycleOwner) {
                if(it) {
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onStarted() {

    }

    override fun onSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onFailure(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onStart() {
        val sharedPreferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        viewModel.sharedPreferences = sharedPreferences
        if (!sharedPreferences.getString("Username", "").isNullOrEmpty() && !sharedPreferences.getString("Password", "").isNullOrEmpty()){
            viewModel.username = sharedPreferences.getString("Username", "").toString()
            viewModel.password = sharedPreferences.getString("Password", "").toString()
            viewModel.onLoginButtonClicked()
        }else{
            progressBar.visibility = GONE
        }
        super.onStart()
    }

}
