package com.ebenezer.gana.fcsibbul.ui.excos.excosDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.data.models.Exco
import com.ebenezer.gana.fcsibbul.databinding.FragmentExcosDetailsBinding

class ExcosDetailsFragment : Fragment() {

    private var _binding: FragmentExcosDetailsBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs:ExcosDetailsFragmentArgs by navArgs()

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
    }

    fun bind(excos:Exco){
        binding.apply {
            excoName.text = resources.getString(R.string.first_name_last_name, excos.firstName,excos.lastName)
            excoPhone.text = excos.phone
            excoPost.text = excos.office
            excoDepartment.text = excos.department
            excoLevel.text = excos.level
            excoImage.setImageResource(R.drawable.img_eben)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}