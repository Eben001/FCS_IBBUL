package com.ebenezer.gana.fcsibbul.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.databinding.LoginFragmentBinding
import com.ebenezer.gana.fcsibbul.ui.baseFragment.BaseFragment
import com.ebenezer.gana.fcsibbul.ui.host.HostActivityLoggedIn
import com.ebenezer.gana.fcsibbul.ui.login.FieldValidators.isValidEmail
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : BaseFragment() {

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel:LoginViewModel by viewModel()

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
                    isEmailValid()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        observeViewModes()
    }

    private fun setupListeners() {
        binding.etEmail.addTextChangedListener(TextFieldValidation(binding.etEmail))
        binding.etPassword.addTextChangedListener(TextFieldValidation(binding.etPassword))

        binding.tvRegister.setOnClickListener {
            /*val action = LoginFragmentDirections.actionLoginFragmentToNavigationSignup()
            this.findNavController().navigate(action)
*/
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim { it <= ' ' }
            val password = binding.etPassword.text.toString().trim { it <= ' ' }

            if (isFieldNotEmpty() && isEmailValid()) {
                viewModel.loginUser(email, password)
                dismissKeyboard()
            }

        }
    }

    private fun isFieldNotEmpty(): Boolean {
        return when {
            binding.etEmail.text.toString().trim().isEmpty() -> {
                showSnackBar("Please enter your email", isError = true)
                false
            }
            binding.etPassword.text.toString().trim().isEmpty() -> {
                showSnackBar("Please enter your password", isError = true)
                false
            }

            else -> true

        }
    }

    private fun isEmailValid(): Boolean {
        return if (!isValidEmail(binding.etEmail.text.toString())) {
            binding.tilEmail.error = resources.getString(R.string.invalid_email)
            binding.etEmail.requestFocus()
            if (binding.etEmail.text.toString().isEmpty()) {
                binding.tilEmail.isErrorEnabled = false
            }
            false
        } else {
            binding.tilEmail.isErrorEnabled = false
            true

        }
    }
    private fun dismissKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }


    private fun observeViewModes() {

        viewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) { // user is logged in
                val intent = Intent(requireActivity(), HostActivityLoggedIn::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                resetEditTextField()
            }
        }
        viewModel.result.observe(viewLifecycleOwner) {
            if (viewModel.isPostSuccess.value == true) {
                showSnackBar(it.asString(requireContext()), isError = false)
            } else {
                showSnackBar(it.asString(requireContext()), isError = true)
            }
        }
    }

    private fun resetEditTextField() {
        binding.etEmail.text?.clear()
        binding.etPassword.text?.clear()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}