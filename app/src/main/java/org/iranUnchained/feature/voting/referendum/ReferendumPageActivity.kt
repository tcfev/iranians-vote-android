//package org.iranUnchained.feature.voting.referendum
//
//import android.Manifest
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.net.Uri
//import android.os.Bundle
//import android.provider.Settings
//import android.util.Log
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.databinding.DataBindingUtil
//import com.google.android.material.dialog.MaterialAlertDialogBuilder
//import org.iranUnchained.R
//import org.iranUnchained.base.view.BaseActivity
//import org.iranUnchained.data.models.VotingData
//import org.iranUnchained.databinding.ActivityReferendumPageBinding
//import org.iranUnchained.feature.voting.MustVerifyActivity
//import org.iranUnchained.utils.Navigator
//import org.iranUnchained.utils.nfc.PermissionUtil
//import org.iranUnchained.utils.resolveDays
//
//class ReferendumPageActivity : BaseActivity() {
//
//    private lateinit var binding: ActivityReferendumPageBinding
//    private lateinit var votingData: VotingData
//    private var selectedOption = ""
//
//
//    override fun onCreateAllowed(savedInstanceState: Bundle?) {
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_referendum_page)
//        binding.lifecycleOwner = this
//
//        votingData = intent?.getParcelableExtra(VOTING_DATA)!!
//        binding.data = votingData
//        binding.date.text = resolveDays(this, votingData.dueDate!!)
//
//        Log.i("Addresses", votingData.contractNo + " " + " " + votingData.contractYes)
//        binding.signBtn.isEnabled = false
//        binding.signBtn.backgroundTintList =
//            ContextCompat.getColorStateList(this, R.color.unselected_button_color)
//
//
//        if (votingData.metadata?.question == null) {
//            binding.markdownText.text = votingData.description
//        }else {
//            binding.markdownText.text = votingData.metadata!!.question
//        }
//
//        if (votingData.metadataYes?.option == null) {
//            binding.option1.text = getString(R.string.button_yes)
//        }else {
//            binding.option1.text = votingData.metadataYes!!.option
//        }
//
//        if (votingData.metadataNo?.option == null) {
//            binding.option2.text = getString(R.string.button_no)
//        }else {
//            binding.option2.text = votingData.metadataNo?.option
//        }
//        initButton()
//    }
//
//    private fun clearButtons() {
//        binding.option1.backgroundTintList =
//            ContextCompat.getColorStateList(this, R.color.unselected_button_color)
//        binding.option2.backgroundTintList =
//            ContextCompat.getColorStateList(this, R.color.unselected_button_color)
//        binding.signBtn.isEnabled = false
//        selectedOption = ""
//    }
//
//    private fun initButton() {
//        clickHelper.addViews(
//            binding.signBtn, binding.cancelButton, binding.option1, binding.option2
//        )
//
//        clickHelper.setOnClickListener {
//            when (it.id) {
//
//                binding.signBtn.id -> {
//                    Navigator.from(this).openVoteProcessing(voteData = votingData, selectedOption)
//
//                }
//
//                binding.cancelButton.id -> {
//                    finish()
//                }
//
//                binding.option1.id -> {
//                    Log.i("YES", votingData.contractYes!! + votingData.metadataYes?.option)
//                    clearButtons()
//                    binding.option1.backgroundTintList =
//                        ContextCompat.getColorStateList(this, R.color.primary_button_color)
//                    selectedOption = votingData.contractYes!!
//                    binding.signBtn.backgroundTintList =
//                        ContextCompat.getColorStateList(this, R.color.primary_button_color)
//                    binding.signBtn.isEnabled = true
//
//                }
//
//                binding.option2.id -> {
//
//                    Log.i("No", votingData.contractNo + votingData.metadataNo?.option)
//                    clearButtons()
//                    binding.option2.backgroundTintList =
//                        ContextCompat.getColorStateList(this, R.color.primary_button_color)
//                    selectedOption = votingData.contractNo!!
//                    binding.signBtn.backgroundTintList =
//                        ContextCompat.getColorStateList(this, R.color.primary_button_color)
//                    binding.signBtn.isEnabled = true
//                }
//
//            }
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == PermissionUtil.REQUEST_CODE_MULTIPLE_PERMISSIONS) {
//            val result = grantResults[0]
//            if (result == PackageManager.PERMISSION_DENIED) {
//                if (!PermissionUtil.showRationale(this, permissions[0])) {
//                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                    val uri = Uri.fromParts("package", packageName, null)
//                    intent.setData(uri)
//                    startActivityForResult(
//                        intent, MustVerifyActivity.APP_SETTINGS_ACTIVITY_REQUEST_CODE
//                    )
//                } else {
//                    requestPermissionForCamera()
//                }
//            } else if (result == PackageManager.PERMISSION_GRANTED) {
//                Navigator.from(this).openScan()
//            }
//        }
//    }
//
//    private fun requestPermissionForCamera() {
//        val permissions = arrayOf(Manifest.permission.CAMERA)
//        val isPermissionGranted = PermissionUtil.hasPermissions(this, *permissions)
//        if (!isPermissionGranted) {
//
//            MaterialAlertDialogBuilder(this).setTitle(getString(R.string.permission_title))
//                .setMessage(resources.getString(R.string.permission_description))
//                .setPositiveButton(resources.getString(R.string.button_ok)) { dialog, which ->
//                    ActivityCompat.requestPermissions(
//                        this, permissions, PermissionUtil.REQUEST_CODE_MULTIPLE_PERMISSIONS
//                    )
//                }.show()
//
//
//        } else {
//            Navigator.from(this).openScan()
//        }
//    }
//
//    companion object {
//        const val VOTING_DATA = "VOTING_DATA"
//    }
//}