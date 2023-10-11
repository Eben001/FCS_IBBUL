package com.fcsibbul.ui.excos.excosDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.fcsibbul.R
import com.fcsibbul.data.models.Exco
import com.fcsibbul.databinding.FragmentExcosDetailsBinding
import com.fcsibbul.ui.baseFragment.BaseFragment
import com.fcsibbul.ui.excos.excosList.ExcosViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ExcosDetailsFragment : BaseFragment() {

    private var _binding: FragmentExcosDetailsBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: ExcosDetailsFragmentArgs by navArgs()
    private val viewModel: ExcosViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentExcosDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind(navigationArgs.exco)
        observeViewModels()
        setOnClickListeners()


    }

    private fun setOnClickListeners() {
        binding.parent.setOnLongClickListener {
            showConfirmDeleteDialog(navigationArgs.exco)
            Timber.d(navigationArgs.exco.id)
            true
        }
    }

    private fun showConfirmDeleteDialog(excos: Exco) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete_dialog_title))
            .setMessage(resources.getString(R.string.delete_exco_dialog_message))
            .setIcon(R.drawable.ic_vector_delete)
            .setNeutralButton(resources.getString(R.string.cancel_dialog_message)) { dialog, _ ->
                dialog.cancel()
            }
            .setNegativeButton(resources.getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.yes)) { dialog, _ ->
                deleteExcoDetails(excos)
                dialog.dismiss()
            }
            .show()

    }


    private fun observeViewModels() {
        viewModel.isDeleteSuccess.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigateUp()
                showSnackBar("Success", false)
            } else {
                showSnackBar("Success", false)
            }
        }
    }

    private fun bind(excos: Exco) {
        binding.apply {
            excoName.text =
                resources.getString(R.string.first_name_last_name, excos.firstName, excos.lastName)
            excoPhone.text = excos.phone
            excoPost.text = excos.office
            excoDepartment.text = excos.department
            excoLevel.text = context?.resources?.getString(R.string.exco_level, excos.level)
            excoImage.load(excos.image_url) {
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_broken_image)
            }
        }
    }

    private fun deleteExcoDetails(excos: Exco) {
        viewModel.deleteExcoDetails(excos.documentId, excos.image_url)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}