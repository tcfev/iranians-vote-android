package org.iranUnchained.feature.onBoarding.logic

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import identity.Identity
import identity.Identity_
import io.reactivex.Observable
import io.reactivex.Single
import org.iranUnchained.R
import org.iranUnchained.base.BaseConfig
import org.iranUnchained.contracts.SRegistration
import org.iranUnchained.data.models.Data
import org.iranUnchained.data.models.IdCardSod
import org.iranUnchained.data.models.IdentityData
import org.iranUnchained.data.models.IdentityDataStored
import org.iranUnchained.data.models.InputsPassport
import org.iranUnchained.data.models.Payload
import org.iranUnchained.data.models.SendCalldataRequest
import org.iranUnchained.data.models.SendCalldataRequestData
import org.iranUnchained.data.models.StateInfo
import org.iranUnchained.data.models.ZkProof
import org.iranUnchained.di.providers.ApiProvider
import org.iranUnchained.logic.persistance.SecureSharedPrefs
import org.iranUnchained.logic.stateProvider.StateProviderImpl
import org.iranUnchained.utils.ZKPTools
import org.iranUnchained.utils.ZKPUseCase
import org.iranUnchained.utils.addCharAtIndex
import org.iranUnchained.utils.decodeHexString
import org.iranUnchained.utils.getIssuingAuthorityCode
import org.iranUnchained.utils.nfc.SecurityUtil
import org.iranUnchained.utils.nfc.model.EDocument
import org.iranUnchained.utils.toBitArray
import org.web3j.crypto.Credentials
import org.web3j.crypto.Keys
import org.web3j.tx.gas.DefaultGasProvider
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class GenerateVerifiableCredential {
    @OptIn(ExperimentalStdlibApi::class)
    fun generateIdentity(
        context: Context, apiProvider: ApiProvider, eDocument: EDocument
    ): Single<Boolean> {

        return Single.create { it ->
            val hexString = eDocument.sod
            if (hexString.isNullOrEmpty()) throw IllegalStateException("No Sod File found")

            val byteArray = hexString.decodeHexString().inputStream()

            val zkpTools = ZKPTools(context)

            val sodFile = SODFileOwn(byteArray)

            val dg1 = eDocument.dg1

            val storedPassportsKeys =
                eDocument.dg2Hash?.let { it1 -> SecureSharedPrefs.getCachedIdentity(context, it1) }

            if (storedPassportsKeys != null) {
                val identity = IdentityData(
                    storedPassportsKeys.secretHex,
                    storedPassportsKeys.secretKeyHex,
                    storedPassportsKeys.nullifierHex,
                    storedPassportsKeys.timeStamp,
                )
                SecureSharedPrefs.saveIdentityData(context, identity.toJson())
                SecureSharedPrefs.saveIssuerDid(context, storedPassportsKeys.issuer_did)
                SecureSharedPrefs.saveClaimId(context, storedPassportsKeys.claim_id)
                Log.i("StoredPassports", "RESTORED FROM SecureSharedPrefs")
                it.onSuccess(true)
            }

            val currentDateTimeInGMT = ZonedDateTime.now(ZoneOffset.UTC)
            val current: LocalDate = currentDateTimeInGMT.toLocalDate()

            val nextDate = processDateForProof(eDocument.personDetails!!.expiryDate!!)
            val inputs = InputsPassport(
                `in` = (dg1!!).toBitArray().toCharArray().map { it1 -> it1.digitToInt() },
                currDateDay = current.dayOfMonth,
                credValidDay = nextDate.dayOfMonth,
                credValidMonth = nextDate.month.value,
                credValidYear = nextDate.year.toString().takeLast(2).toInt(),
                currDateMonth = current.month.value,
                currDateYear = current.year.toString().takeLast(2).toInt(),
                ageLowerbound = 16
            )


            val gson = GsonBuilder().setPrettyPrinting().create()
            val jsonInputs = gson.toJson(inputs).toByteArray()


            val zkp: ZkProof = when (sodFile.digestAlgorithm) {
                "SHA-256" -> {
                    ZKPUseCase(context).generateZKP(
                        R.raw.passport_verification_sha_256_zkey,
                        R.raw.passport_verification_sha_256,
                        jsonInputs,
                        zkpTools::passportVerification256
                    )
                }

                "SHA-1" -> {
                    ZKPUseCase(context).generateZKP(
                        R.raw.passport_verification_sha_1_zkey,
                        R.raw.passport_verification_sha_1,
                        jsonInputs,
                        zkpTools::passportVerification1
                    )
                }

                else -> {
                    throw Exception("Invalid digest algorithm: ${sodFile.digestAlgorithm}")
                }
            }

            val identity = Identity.newIdentity(
                StateProviderImpl(context, apiProvider)
            )

            val signedAttributes = sodFile.eContent
            val algorithm = sodFile.digestEncryptionAlgorithm
            val certificate = sodFile.docSigningCertificate
            var pemFile = SecurityUtil.convertToPem(certificate)

            val index = pemFile.indexOf("-----END CERTIFICATE-----")
            pemFile = pemFile.addCharAtIndex('\n', index)
            val encapsulatedContent = sodFile.readASN1Data()!!.toHexString()

            val target = "30"
            val startIndex = encapsulatedContent.indexOf(target)
            val encapsulatedContentCut = encapsulatedContent.substring(startIndex)


            val payload = Payload(
                Data(
                    id = identity.did, zkproof = zkp, document_sod = IdCardSod(
                        signed_attributes = signedAttributes.toHexString(),
                        algorithm = algorithm,
                        signature = sodFile.encryptedDigest.toHexString(),
                        pem_file = pemFile,
                        encapsulated_content = encapsulatedContentCut
                    )
                )
            )


            val passportVerificationProof: String = gson.toJson(payload)

            Log.i("Payload", passportVerificationProof)

            val response = apiProvider.circuitBackend.createIdentity(payload).blockingGet()

            val timestamp = System.currentTimeMillis() / 1000

            Log.i("Payload", gson.toJson(response))

            SecureSharedPrefs.saveIssuerDid(context, response.data.attributes.issuer_did)
            SecureSharedPrefs.saveClaimId(context, response.data.attributes.claim_id)
            val identityToSave = IdentityData(
                identity.secretHex,
                identity.secretKeyHex,
                identity.nullifierHex,
                (timestamp).toString()
            )

            SecureSharedPrefs.saveIdentityData(context, identityToSave.toJson())

            //save to Cache
            val storedIdentityData = IdentityDataStored(
                identity.secretHex,
                identity.secretKeyHex,
                identity.nullifierHex,
                response.data.attributes.issuer_did,
                response.data.attributes.claim_id,
                (timestamp).toString(),
            )
            SecureSharedPrefs.addCachedIdentity(context, eDocument.dg2Hash!!, storedIdentityData)


            Log.i("StoredPassports", "RESTORED FROM RAW DATA")
            it.onSuccess(true)
        }
    }

    fun createIdentity(context: Context, apiProvider: ApiProvider): Identity_? {
        val identityRaw = SecureSharedPrefs.getIdentityData(context) ?: return null
        val identityData = IdentityData.fromJson(identityRaw)
        val identity = Identity.newIdentityWithData(
            identityData.secretKeyHex,
            identityData.secretHex,
            identityData.nullifierHex,
            StateProviderImpl(context, apiProvider)
        )
        return identity
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun register(
        context: Context, apiProvider: ApiProvider, votingAddress: String, contractToSave: String?
    ): Observable<Int> {

        val identity = createIdentity(context, apiProvider)!!
        val gson = Gson()

        val savableContract: String
        if (contractToSave != null) {
            savableContract = contractToSave
        } else {
            savableContract = votingAddress
        }

        return Observable.create {

            val ecKeyPair = Keys.createEcKeyPair()
            val credentials = Credentials.create(ecKeyPair)
            val gasProvider = DefaultGasProvider()

            val contract = SRegistration.load(
                votingAddress, apiProvider.web3, credentials, gasProvider
            )

            val zkpTools = ZKPTools(context)
            val issuerDid = SecureSharedPrefs.getIssuerDid(context)!!
            val schemaJson = zkpTools.openRawResourceAsByteArray(R.raw.registration)

            val identityRaw = SecureSharedPrefs.getIdentityData(context)!!
            val identityData = IdentityData.fromJson(identityRaw)

            it.onNext(0)
            var stateInfo: StateInfo? = null
            if (SecureSharedPrefs.getVC(context) == null) {
                while (true) {
                    val finalizedResponse =
                        isFinalized(identity, identityData, issuerDid, stateInfo)

                    if (finalizedResponse!!.stateInfo.hash.isNotEmpty()) {
                        stateInfo = finalizedResponse.stateInfo
                    }

                    if (finalizedResponse.isFinalized) {
                        break
                    }
                    Thread.sleep(10 * 1000)
                }

                val claim_id = SecureSharedPrefs.geiClaimId(context)!!

                Log.i("Claim id", claim_id)

                Log.i("IDENTITY DID", identity.did)
                val claimOfferResponse =
                    apiProvider.circuitBackend.claimOffer(claim_id).blockingGet()

                val rawClaimOfferResponse = gson.toJson(claimOfferResponse)

                SecureSharedPrefs.saveVC(context, rawClaimOfferResponse)

                identity.initVerifiableCredentials(rawClaimOfferResponse.toByteArray())

            } else {
                val vC = SecureSharedPrefs.getVC(context)!!.toByteArray()
                identity.initVerifiableCredentials(vC)
            }

            it.onNext(1)


            val issuerAuthority = SecureSharedPrefs.getIssuerAuthority(context)

            if (!SecureSharedPrefs.checkFinalizes(context, votingAddress)) {

                val callData: ByteArray = identity.register(
                    BaseConfig.CORE_LINK,
                    issuerDid,
                    votingAddress,
                    schemaJson,
                    getIssuingAuthorityCode(issuerAuthority!!),
                    Gson().toJson(stateInfo).toByteArray()
                )

                Log.i("STATE INFO", Gson().toJson(stateInfo))
                val calldataRequest =
                    SendCalldataRequest(SendCalldataRequestData("0x" + callData.toHexString()))


                val resp =
                    apiProvider.circuitBackend.sendRegistration(calldataRequest).blockingGet()

                SecureSharedPrefs.addFinalizationVote(context, votingAddress)
                it.onNext(2)
            }
            it.onNext(2)



            it.onNext(3)

            SecureSharedPrefs.saveVotedAddress(context, identity.nullifierHex, savableContract)
            it.onComplete()

        }
    }

    private fun processDateForProof(expiryDate: String): LocalDateTime {
        val currentDateTimeInGMT = ZonedDateTime.now(ZoneOffset.UTC)
        val currentDateTime: LocalDateTime = currentDateTimeInGMT.toLocalDateTime()
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val parsedExpiryDate = LocalDate.parse(expiryDate, formatter).atStartOfDay()

        if (currentDateTime.isAfter(parsedExpiryDate)) throw IllegalStateException("Expiry date is out")

        val maxDuration = Duration.ofDays(365L) // one year

        var duration = maxDuration
        var permissionEndDateTime = currentDateTime.plus(duration)

        while (permissionEndDateTime.isAfter(parsedExpiryDate)) {
            duration = duration.dividedBy(2)
            permissionEndDateTime = currentDateTime.plus(duration)
        }

        return permissionEndDateTime
    }


    private fun isFinalized(
        identity: Identity_, identityData: IdentityData, issuerDid: String, stateInfo: StateInfo?
    ): org.iranUnchained.data.models.FinalizedResponse? {
        try {

            var stateData: ByteArray = "".toByteArray()
            if (stateInfo != null) {
                stateData = Gson().toJson(stateInfo).toByteArray()
            }

            val byteArrayResponse = identity.isFinalized(
                BaseConfig.CORE_LINK, issuerDid, identityData.timeStamp.toLong(), stateData
            )


            val response = Gson().fromJson(
                byteArrayResponse.decodeToString(),
                org.iranUnchained.data.models.FinalizedResponse::class.java
            )



            return response
        } catch (e: Exception) {
            throw e
        }

    }
}
