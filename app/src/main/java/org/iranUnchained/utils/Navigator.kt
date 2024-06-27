package org.iranUnchained.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import org.iranUnchained.R
import org.iranUnchained.data.models.VotingData
import org.iranUnchained.feature.browser.BrowserActivity
import org.iranUnchained.feature.intro.IntroActivity
import org.iranUnchained.feature.intro.StartActivity
import org.iranUnchained.feature.onBoarding.ConfirmationActivity
import org.iranUnchained.feature.onBoarding.InfoActivity
import org.iranUnchained.feature.onBoarding.ScanActivity
import org.iranUnchained.feature.security.CheckPinCodeActivity
import org.iranUnchained.feature.security.CreateCredentialActivity
import org.iranUnchained.feature.security.CreatePinCodeActivity
import org.iranUnchained.feature.voting.ManifestSigning
import org.iranUnchained.feature.voting.MustVerifyActivity
import org.iranUnchained.feature.voting.SignedManifest
import org.iranUnchained.feature.voting.VoteListActivity
import org.iranUnchained.feature.voting.VoteOptionsActivity
import org.iranUnchained.feature.voting.VotePageActivity
import org.iranUnchained.feature.voting.VotePassportlessActivity
import org.iranUnchained.feature.voting.VoteProcessingActivity
import org.iranUnchained.utils.nfc.model.EDocument


/**
 * Performs transitions between screens.
 * 'open-' will open related screen as a child.<p>
 * 'to-' will open related screen and finish current.
 */
class Navigator private constructor() {
    private var activity: Activity? = null
    private var fragment: Fragment? = null
    private var context: Context? = null

    companion object {
        fun from(activity: Activity): Navigator {
            val navigator = Navigator()
            navigator.activity = activity
            navigator.context = activity
            return navigator
        }

        fun from(fragment: Fragment): Navigator {
            val navigator = Navigator()
            navigator.fragment = fragment
            navigator.context = fragment.requireContext()
            return navigator
        }

        fun from(context: Context): Navigator {
            val navigator = Navigator()
            navigator.context = context
            return navigator
        }
    }

    private fun performIntent(intent: Intent?, requestCode: Int? = null, bundle: Bundle? = null) {
        if (intent != null) {
            if (!IntentLock.checkIntent(intent, context)) return
            activity?.let {
                if (requestCode != null) {
                    it.startActivityForResult(intent, requestCode, bundle ?: Bundle.EMPTY)
                } else {
                    it.startActivity(intent, bundle ?: Bundle.EMPTY)
                }
                return
            }

            fragment?.let {
                if (requestCode != null) {
                    it.startActivityForResult(intent, requestCode, bundle ?: Bundle.EMPTY)
                } else {
                    it.startActivity(intent, bundle ?: Bundle.EMPTY)
                }
                return
            }

            val newIntent = intent.apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context?.startActivity(newIntent, bundle ?: Bundle.EMPTY)

        }
    }

    fun openVotePage(votingData: VotingData) {
        val intent = Intent(context, VotePageActivity::class.java)
        intent.putExtra(VotePageActivity.VOTING_DATA, votingData)
        performIntent(intent)
    }

    fun openCreateCredential() {
        val intent = Intent(context, CreateCredentialActivity::class.java)
        performIntent(intent)
    }

    fun openCreatePinCode() {
        val intent = Intent(context, CreatePinCodeActivity::class.java)
        performIntent(intent)
    }

    fun openVotePasspoetless(votingData: VotingData) {
        val intent = Intent(context, VotePassportlessActivity::class.java)
        intent.putExtra(VotePassportlessActivity.VOTING_DATA, votingData)
        performIntent(intent)
    }

    fun openOptionVoting(votingData: VotingData) {
        val intent = Intent(context, VoteOptionsActivity::class.java)
        intent.putExtra(VoteOptionsActivity.VOTING_DATA, votingData)
        performIntent(intent)
    }

    fun openInfo() {
        val intent = Intent(context, InfoActivity::class.java)
        performIntent(intent)
    }

    fun openScan() {
        val intent = Intent(context, ScanActivity::class.java)
        performIntent(intent)
    }

    fun openIntro() {
        val intent = Intent(context, IntroActivity::class.java)
        performIntent(intent)
    }

    fun openVote() {
        val intent = Intent(context, VoteListActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK);
        performIntent(intent)
    }

    fun openConfirmation(eDocument: EDocument) {
        val intent = Intent(context, ConfirmationActivity::class.java)
        eDocument.personDetails?.faceImageBase64 = null
        eDocument.personDetails?.faceImage = null
        eDocument.personDetails?.portraitImage = null
        eDocument.personDetails?.portraitImageBase64 = null
        eDocument.personDetails?.signature = null
        eDocument.personDetails?.signatureBase64 = null
        intent.putExtra(ConfirmationActivity.E_DOCUMENT, eDocument)
        performIntent(intent)
    }

    fun openVoteProcessing(voteData: VotingData, selectedContract: String? = null) {
        val intent = Intent(context, VoteProcessingActivity::class.java)
        intent.putExtra(VoteProcessingActivity.VOTE_DATA, voteData)
        if (selectedContract != null)
            intent.putExtra(VoteProcessingActivity.VOTE_REFERENDUM_CONTRACT, selectedContract)
        performIntent(intent)
    }

    fun openSignedManifest(voteData: VotingData) {
        val intent = Intent(context, SignedManifest::class.java)
        intent.putExtra(SignedManifest.VOTE_DATA, voteData)
        performIntent(intent)
    }

    fun openStart() {
        val intent = Intent(context, StartActivity::class.java)
        performIntent(intent)
    }

    fun openBrowser(url: String) {
        val intent = Intent(context, BrowserActivity::class.java)
        intent.putExtra(BrowserActivity.URL_TRANSFER_KEY, url)
        performIntent(intent)
    }

    private fun finishAffinity(activity: Activity) {
        activity.setResult(Activity.RESULT_CANCELED, null)
        ActivityCompat.finishAffinity(activity)
    }

    private fun fadeOut(activity: Activity) {
        ActivityCompat.finishAfterTransition(activity)
        activity.overridePendingTransition(0, R.anim.activity_fade_out)
        activity.finish()
    }

    private fun createTransitionBundle(
        activity: Activity, vararg pairs: Pair<View?, String>
    ): Bundle {
        val sharedViews = arrayListOf<androidx.core.util.Pair<View, String>>()

        pairs.forEach {
            val view = it.first
            if (view != null) {
                sharedViews.add(androidx.core.util.Pair(view, it.second))
            }
        }

        return if (sharedViews.isEmpty()) {
            Bundle.EMPTY
        } else {
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity, *sharedViews.toTypedArray()
            ).toBundle() ?: Bundle.EMPTY
        }
    }

    fun openManifestPage(data: VotingData) {
        val intent = Intent(context, ManifestSigning::class.java)
        intent.putExtra(ManifestSigning.MANIFEST_DATA, data)
        performIntent(intent)
    }

    fun openVerificationPage(data: VotingData) {
        val intent = Intent(context, MustVerifyActivity::class.java)
        intent.putExtra(MustVerifyActivity.VOTING_DATA, data)
        performIntent(intent)
    }

    fun openCheckPinCode() {
        val intent = Intent(context, CheckPinCodeActivity::class.java)
        performIntent(intent)
    }

//    fun openReferendumCheckReq(data: VotingData) {
//        val intent = Intent(context, ReferendumCheckReqActivity::class.java)
//        intent.putExtra(ReferendumCheckReqActivity.VOTING_DATA, data)
//        performIntent(intent)
//    }



}