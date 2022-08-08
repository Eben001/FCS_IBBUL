package com.ebenezer.gana.fcsibbul.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.databinding.LoginFragmentBinding
import com.ebenezer.gana.fcsibbul.ui.host.HostActivityLoggedIn
import com.ebenezer.gana.fcsibbul.ui.login.FieldValidators.isStringContainNumber
import com.ebenezer.gana.fcsibbul.ui.login.FieldValidators.isStringContainSpecialCharacter
import com.ebenezer.gana.fcsibbul.ui.login.FieldValidators.isStringLowerAndUpperCase
import com.ebenezer.gana.fcsibbul.ui.login.FieldValidators.isValidEmail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    /**
     * applying text watcher on each text field
     */
    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // checking ids of each text field and applying functions accordingly.
            when (view.id) {
                R.id.et_email -> {
                    validateEmail()
                }
                R.id.et_password -> {
                    validatePassword()
                }
            }

        }

    }

    private fun resetEditTextField() {
        binding.etEmail.setText("")
        binding.etPassword.setText("")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()

        viewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) { // user is logged in
                val intent = Intent(requireActivity(), HostActivityLoggedIn::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                resetEditTextField()

            }
        }

        binding.tvRegister.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToNavigationSignup()
            this.findNavController().navigate(action)

        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim { it <= ' ' }
            val password = binding.etPassword.text.toString().trim { it <= ' ' }
            viewModel.loginUser(email, password)

            /*  if (isLoginDetailsValid()) {
                  viewModel.loginUser(email, password)
              }*/
        }
    }


    private fun setupListeners() {
        // binding.etEmail.addTextChangedListener(TextFieldValidation(binding.etEmail))
        //binding.etPassword.addTextChangedListener(TextFieldValidation(binding.etPassword))
    }


    private fun isLoginDetailsValid(): Boolean =
        validateEmail() && validatePassword()


    private fun validateEmail(): Boolean {
        if (binding.etEmail.toString().trim().isEmpty()) {
            binding.tilEmail.error = "Required Field"
            binding.etEmail.requestFocus()
            return false
        } else if (!isValidEmail(binding.etEmail.text.toString())) {
            binding.tilEmail.error = "Invalid Email"
            binding.etEmail.requestFocus()
            return false
        } else {
            binding.tilEmail.isErrorEnabled = false
        }
        return true
    }

    private fun validatePassword(): Boolean {
        if (binding.etPassword.text.toString().trim().isEmpty()) {
            binding.tilPassword.error = "Required Field"
            binding.etPassword.requestFocus()

            return false
        } else if (binding.etPassword.text.toString().length < 6) {
            binding.tilPassword.error = "password can't be less than 6"
            binding.etPassword.requestFocus()

            return false
        } else if (!isStringContainNumber(binding.etPassword.text.toString())) {
            binding.tilPassword.error = "Required at least 1 digit"
            binding.etPassword.requestFocus()
            return false
        } else if (!isStringLowerAndUpperCase(binding.etPassword.text.toString())) {
            binding.tilPassword.error =
                "Password must contain upper and lower case letters"
            binding.etPassword.requestFocus()
            return false
        } else if (!isStringContainSpecialCharacter(binding.etPassword.text.toString())) {
            binding.tilPassword.error = "1 special character required"
            binding.etPassword.requestFocus()
            return false
        } else {
            binding.tilPassword.isErrorEnabled = false
        }
        return true
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}