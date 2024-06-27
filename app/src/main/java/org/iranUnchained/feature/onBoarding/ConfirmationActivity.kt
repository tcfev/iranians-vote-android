package org.iranUnchained.feature.onBoarding

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import io.reactivex.rxkotlin.addTo
import org.iranUnchained.R
import org.iranUnchained.base.view.BaseActivity
import org.iranUnchained.databinding.ActivityConfirmingBinding
import org.iranUnchained.feature.onBoarding.logic.GenerateVerifiableCredential
import org.iranUnchained.logic.persistance.SecureSharedPrefs
import org.iranUnchained.utils.ObservableTransformers
import org.iranUnchained.utils.TimerManager
import org.iranUnchained.utils.nfc.model.EDocument
import retrofit2.HttpException
import java.net.UnknownHostException

class ConfirmationActivity : BaseActivity() {

    lateinit var binding: ActivityConfirmingBinding
    private lateinit var eDocument: EDocument
    private lateinit var nextTimer: TimerManager
    override fun onCreateAllowed(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirming)
        binding.lifecycleOwner = this

        eDocument = intent?.getParcelableExtra(E_DOCUMENT)!!

        GenerateVerifiableCredential().generateIdentity(this, this.apiProvider, eDocument)
            .compose(ObservableTransformers.defaultSchedulersSingle())
            .subscribe({
                SecureSharedPrefs.saveIsPassportScanned(this)
                setReady()
            }, {
                it.printStackTrace()
                errorHandler(it)
                finish()
            })
            .addTo(compositeDisposable)
    }

    private fun errorHandler(throwable: Throwable) {
        throwable.printStackTrace()
        when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    404 -> {
                        toastManager.long("You already have an Identity")
                    }

                    400 -> {
                        toastManager.long("Please try again")
                    }
                }
            }

            is java.lang.RuntimeException -> {
                if (throwable.cause is UnknownHostException) {
                    toastManager.long("Check your Internet connection")
                }
            }

            else -> {
                Log.i("ERROR", throwable.toString())
            }
        }
    }


    private fun setReady() {
        binding.animation.setAnimation(R.raw.checkbox_succes)
        binding.animation.repeatCount = 0
        binding.animation.playAnimation()

        binding.titleText.text = getString(R.string.all_done)

        binding.tipText.text = getString(R.string.all_done_description)

        nextTimer = TimerManager {
            finish()
        }

        nextTimer.startTimer(5000)

    }


    companion object {
        const val E_DOCUMENT = "E_DOCUMENT"
    }

}


