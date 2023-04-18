package com.ebenezer.gana.fcsibbul.ui.admin.addExco

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import coil.load
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.databinding.FragmentAddExcoBinding
import com.ebenezer.gana.fcsibbul.ui.baseFragment.BaseFragment
import com.ebenezer.gana.fcsibbul.utils.Constants
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException

class AddExcoFragment : BaseFragment() {

    override var bottomNavigationViewVisibility = View.GONE

    private var _binding: FragmentAddExcoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddExcoViewModel by viewModel()
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
            if (isValidDetails() && isValidLevelField()) {
                showConfirmDialog()
            }
        }
        binding.excoImage.setOnClickListener {
            showImagePicker()
        }
    }

    private fun showImagePicker() {
        ImagePicker.with(this)
            .compress(1024)
            .maxResultSize(1080, 1080)
            .crop(10f, 10f)    //Crop image with 16:9 aspect ratio
            .galleryMimeTypes(
                mimeTypes = arrayOf(
                    "image/png",
                    "image/jpg",
                    "image/jpeg"
                )
            )
            .createIntent { intent ->
                pickImageFromGalleryForResult.launch(intent)
            }
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
                mSelectedImageFileUri, Constants.EXCOS_IMAGE,
                excoFullName = "${
                    binding.etFirstName.text.toString().trim()
                } - ${binding.etLastName.text.toString().trim()}"
            )

            //Observe and use the excoUrl received from uploadImageToCloudStorage() call
            viewModel.imageUrl.observe(viewLifecycleOwner) { excoUrl ->
                viewModel.addExco(
                    id = "",
                    documentId = "",
                    firstName = binding.etFirstName.text.toString().trim(),
                    lastName = binding.etLastName.text.toString().trim(),
                    emailId = binding.etEmail.text.toString().trim(),
                    imageUrl = excoUrl,
                    phoneNumber = binding.etPhone.text.toString().trim(),
                    office = binding.etOffice.text.toString().trim(),
                    department = binding.etDepartment.text.toString().trim(),
                    level = binding.etLevel.text.toString().toInt()
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

            binding.etOffice.text.toString().trim().isEmpty() -> {
                showSnackBar(
                    resources.getString(R.string.err_msg_enter_office),
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

            binding.etLevel.text.toString().trim().isEmpty() -> {
                showSnackBar(
                    resources.getString(R.string.err_msg_enter_level),
                    isError = true
                )
                false
            }
            else -> true
        }

    }

    private fun isValidLevelField(): Boolean {
        return when (binding.etLevel.text.toString().trim()) {
            "100", "200", "300", "400", "500" -> {
                true
            }
            else -> {
                showSnackBar(
                    resources.getString(R.string.enter_valid_level),
                    isError = true
                )
                false
            }
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