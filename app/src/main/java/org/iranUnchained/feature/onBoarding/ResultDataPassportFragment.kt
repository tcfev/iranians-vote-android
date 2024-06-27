package org.iranUnchained.feature.onBoarding

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import org.iranUnchained.R
import org.iranUnchained.base.view.BaseActivity
import org.iranUnchained.base.view.BaseFragment
import org.iranUnchained.databinding.FragmentResultDataPassportBinding
import org.iranUnchained.logic.persistance.SecureSharedPrefs
import org.iranUnchained.utils.Navigator
import org.iranUnchained.utils.nfc.ImageUtil
import org.iranUnchained.utils.nfc.model.EDocument
import org.iranUnchained.utils.withPersianDigits

class ResultDataPassportFragment : BaseFragment() {

    private lateinit var binding: FragmentResultDataPassportBinding
    var eDocumentData: EDocument? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_result_data_passport, container, false
        )

        val image: Bitmap = ImageUtil.scaleImage(eDocumentData!!.personDetails!!.faceImage!!)!!

        binding.viewPhoto.setImageBitmap(image)

        binding.name.text = eDocumentData!!.personDetails!!.name

        if (eDocumentData!!.personDetails!!.gender == "MALE") {
            binding.gender.text = resources.getString(R.string.male)
        } else {
            binding.gender.text = resources.getString(R.string.female)
        }

        binding.documentNumber.text = eDocumentData!!.personDetails!!.serialNumber
        binding.dateOfExpiry.text =
            eDocumentData!!.personDetails!!.expiryDate?.withPersianDigits((requireActivity() as BaseActivity).getLocale())
        binding.dateOfBirth.text =
            eDocumentData!!.personDetails!!.birthDate?.withPersianDigits((requireActivity() as BaseActivity).getLocale())
        binding.nationality.text = eDocumentData!!.personDetails!!.nationality


        initButtons()
        return binding.root
    }

    private fun initButtons() {
        clickHelper.addViews(binding.confirmButton)
        clickHelper.setOnClickListener {
            when (it.id) {
                binding.confirmButton.id -> {

                    SecureSharedPrefs.clearAllData(requireContext())

                    SecureSharedPrefs.saveDateOfBirth(
                        requireContext(), eDocumentData!!.personDetails!!.birthDate!!
                    )
                    SecureSharedPrefs.saveIssuerAuthority(
                        requireContext(), eDocumentData!!.personDetails!!.issuerAuthority!!
                    )
                    Navigator.from(this).openConfirmation(eDocumentData!!)
                    requireActivity().finish()

                }
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = ResultDataPassportFragment()
    }
}