package com.ebenezer.gana.fcsibbul.ui.signup

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.databinding.SignUpFragmentBinding

class SignUpFragment : Fragment() {

    private var _binding: SignUpFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignUpViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SignUpFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isSignupSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                val action = SignUpFragmentDirections.actionNavigationSignupToLoginFragment()
                findNavController().navigate(action)
            }
        }

        viewModel.result.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it.asString(requireContext()), Toast.LENGTH_SHORT).show()

        }

        binding.tvLogin.setOnClickListener {
            findNavController().navigateUp()
        }


        binding.btnRegister.setOnClickListener {
            val email: String = binding.etEmail.text.toString().trim() { it <= ' ' }
            val password: String = binding.etPassword.text.toString().trim() { it <= ' ' }
            val firstName: String = binding.etFirstName.text.toString().trim { it <= ' ' }
            val lastName: String = binding.etLastName.text.toString().trim { it <= ' ' }

            if (validateRegistrationDetails()) {
                viewModel.registerNewUser(
                    firstName, lastName, email, password
                )
            }
        }
    }












    private fun validateRegistrationDetails(): Boolean {
        return when {
            TextUtils.isEmpty(
                binding.etFirstName.text.toString()
                    .trim { it <= ' ' }) -> {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.err_msg_enter_first_name),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            TextUtils.isEmpty(
                binding.etLastName.text.toString()
                    .trim { it <= ' ' }) -> {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.err_msg_enter_last_name),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }

            TextUtils.isEmpty(
                binding.etEmail.text.toString()
                    .trim { it <= ' ' }) -> {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.err_msg_enter_email),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }

            TextUtils.isEmpty(
                binding.etPassword.text.toString()
                    .trim { it <= ' ' }) -> {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.err_msg_enter_password),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }

            TextUtils.isEmpty(
                binding.etConfirmPassword.text.toString()
                    .trim { it <= ' ' }) -> {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.err_msg_enter_confirm_password),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            binding.etPassword.text.toString()
                .trim { it <= ' ' } != binding.etConfirmPassword.text.toString()
                .trim { it <= ' ' } -> {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.err_msg_password_and_confirm_password_mismatch),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            !binding.cbTermsAndCondition.isChecked -> {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.err_msg_agree_terms_and_condition),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            else -> {
                true
            }


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}