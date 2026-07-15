package com.aenempeh.habittracker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.aenempeh.habittracker.databinding.FragmentLoginBinding
import com.aenempeh.habittracker.viewmodel.LoginViewModel

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        // Check for session
        viewModel.checkSession()

        viewModel.sessionUserLD.observe(viewLifecycleOwner) { username ->
            if (username != null) {
                // Auto login
                val action = LoginFragmentDirections.actionHabitListFragment()
                Navigation.findNavController(requireView()).navigate(action)
            }
        }

        viewModel.loginStatusLD.observe(viewLifecycleOwner) { status ->
            if (status == true) {
                Toast.makeText(requireContext(), "Login Berhasil!", Toast.LENGTH_SHORT).show()
                val action = LoginFragmentDirections.actionHabitListFragment()
                Navigation.findNavController(requireView()).navigate(action)
            } else if (status == false) {
                Toast.makeText(requireContext(), "Gagal masuk. Cek kembali akun Anda.", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loginErrorLD.observe(viewLifecycleOwner) { error ->
            if (error == "Username salah") {
                binding.username.error = "Username salah"
                binding.password.error = null
            } else if (error == "Password salah") {
                binding.password.error = "Password salah"
                binding.username.error = null
            } else {
                binding.username.error = null
                binding.password.error = null
            }
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.txtUsername.text.toString().trim()
            val password = binding.txtPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Username/Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.login(username, password)
        }
    }
}
