package com.ebenezer.gana.fcsibbul.ui.admin.addExco

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import coil.dispose
import coil.load
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.databinding.FragmentAddExcoBinding
import com.ebenezer.gana.fcsibbul.ui.baseFragment.BaseFragment
import com.ebenezer.gana.fcsibbul.utils.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class AddExcoFragment : BaseFragment() {

    override var bottomNavigationViewVisibility = View.GONE

    private var _binding: FragmentAddExcoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddExcoViewModel by viewModels()
    private var mSelectedImageFileUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddExcoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModels()
        setOnClickListeners()

    }

    private fun observeViewModels() {
        viewModel.result.observe(viewLifecycleOwner) {
            if (viewModel.isPostSuccess.value == true) {
                showSnackBar(it.asString(requireContext()), isError = false)
                clearInputFields()
                binding.addExco.isEnabled = true

            } else {
                showSnackBar(it.asString(requireContext()), isError = true)
            }
        }

    }

    private fun setOnClickListeners() {
        binding.addExco.setOnClickListener {
            if (isValidDetails()) {
                showConfirmDialog()
            }
        }
        binding.excoImage.setOnClickListener {
            //Function to check if the permission to use the device storage is granted
            //Request one if not granted
            requestPermission()
        }
    }


    private fun requestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                showImageChooser()
            }

            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                showRequestPermissionRationale()
            }

            else -> {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }

    }

    private fun showImageChooser() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickImageFromGalleryForResult.launch(galleryIntent)
    }

    private var pickImageFromGalleryForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode
            == Activity.RESULT_OK
        ) {
            val data = result.data
            if (data != null
                && data.data != null
            ) {
                val selectedImageUri = data.data
                try {
                    mSelectedImageFileUri = selectedImageUri
                    //Use Coil to Load image
                    binding.excoImage.load(mSelectedImageFileUri) {
                        placeholder(R.drawable.ic_user_placeholder)
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.image_selection_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }

    private fun showRequestPermissionRationale() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(resources.getString(R.string.msg_grant_permission))
            .setNeutralButton(resources.getString(R.string.cancel_dialog_message)) { dialog, _ ->
                dialog.cancel()
            }
            .setNegativeButton(resources.getString(R.string.no_thanks)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    private fun showConfirmDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.add_exco_title))
            .setMessage(resources.getString(R.string.add_exco_message))
            .setNeutralButton(resources.getString(R.string.cancel_dialog_message)) { dialog, _ ->
                dialog.cancel()
            }
            .setNegativeButton(resources.getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.yes)) { dialog, _ ->
                addExco()
            }
            .show()
    }

    private fun addExco() {
        if (mSelectedImageFileUri != null) {

            viewModel.uploadImageToCloudStorage(
                requireActivity(),
                mSelectedImageFileUri, Constants.EXCOS_IMAGE
            )

            //Observe and use the excoUrl received from uploadImageToCloudStorage() call
            viewModel.imageUrl.observe(viewLifecycleOwner) { excoUrl ->
                viewModel.addExco(
                    "",
                    binding.etFirstName.text.toString().trim(),
                    binding.etLastName.text.toString().trim(),
                    binding.etEmail.text.toString().trim(),
                    excoUrl,
                    binding.etPhone.text.toString().trim(),
                    binding.etOffice.text.toString().trim(),
                    binding.etDepartment.text.toString().trim(),
                    binding.etLevel.text.toString().toInt()
                )
            }
            binding.addExco.isEnabled = false

        } else {
            showSnackBar("Please upload an Image", isError = true)
        }
    }

    private fun clearInputFields() {
        binding.excoImage.load(R.drawable.ic_user_placeholder)
        binding.etFirstName.text?.clear()
        binding.etLastName.text?.clear()
        binding.etEmail.text?.clear()
        binding.etPhone.text?.clear()
        binding.etOffice.text?.clear()
        binding.etDepartment.text?.clear()
        binding.etLevel.text?.clear()
    }

    private fun isValidDetails(): Boolean {
        return when {
            binding.etFirstName.text.toString().trim().isEmpty() -> {
                showSnackBar(
                    resources.getString(R.string.err_msg_enter_first_name),
                    isError = true
                )
                false
            }
            binding.etLastName.text.toString().trim().isEmpty() -> {
                showSnackBar(
                    resources.getString(R.string.err_msg_enter_last_name),
                    isError = true
                )
                false
            }

            binding.etPhone.text.toString().trim().isEmpty() -> {
                showSnackBar(
                    resources.getString(R.string.err_msg_enter_phone),
                    isError = true
                )
                false
            }

            binding.etDepartment.text.toString().trim().isEmpty() -> {
                showSnackBar(
                    resources.getString(R.string.err_msg_enter_department),
                    isError = true
                )
                false
            }

            binding.etLastName.text.toString().trim().isEmpty() -> {
                showSnackBar(
                    resources.getString(R.string.err_msg_enter_level),
                    isError = true
                )
                false
            }


            binding.etOffice.text.toString().trim().isEmpty() -> {
                showSnackBar(
                    resources.getString(R.string.err_msg_enter_office),
                    isError = true
                )
                false
            }

            else -> true
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

}