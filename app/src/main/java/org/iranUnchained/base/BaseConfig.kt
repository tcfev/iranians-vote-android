package org.iranUnchained.base


//Main
object BaseConfig {
    const val CREATE_IDENTITY_LINK =
        "https:///kyc.iran.freedomtool.org/integrations/identity-provider-service/v1/create-identity"

    const val GIST_DATA_LINK =
        "https:///kyc.iran.freedomtool.org/integrations/identity-provider-service/v1/gist-data"

    const val CLAIM_OFFER_LINK_V2 = "https://issuer.iran.freedomtool.org/v1/offer/{claim_id}"

    const val SEND_REGISTRATION_LINK =
        "https://proofverification.iran.freedomtool.org/integrations/proof-verification-relayer/v1/register"

    const val REGISTRATION_ADDRESS = "0x90D6905362a9CBaF3A401a629a19057D23055Baf"
    const val PROPOSAL_ADDRESS = "0xB1e1650A95e2baC47084D3E324766d3B16e5d0ef"
    const val CORE_LINK = "https://rpc-api.mainnet.rarimo.com"

    const val BLOCK_CHAIN_RPC_LINK = "https://rpcproxy.iran.freedomtool.org"

    const val REGISTRATION_TYPE = "Simple Registration"

    const val PRIVACY_POLICY_URL = "https://www.iranians.vote/privacy-policy.html"
}

//TestNet
object TestNet {

    const val CREATE_IDENTITY_LINK =
        "https://api.stage.freedomtool.org/integrations/identity-provider-service/v1/create-identity"
    const val GIST_DATA_LINK =
        "https://api.stage.freedomtool.org/integrations/identity-provider-service/v1/gist-data"
    const val SEND_REGISTRATION_LINK =
        "https://api.stage.freedomtool.org/integrations/proof-verification-relayer/v1/register"

    const val CLAIM_OFFER_LINK_V2 =
        "https://issuer.polygon.robotornot.mainnet-beta.rarimo.com/v1/offer/{claim_id}"

    const val CORE_LINK = "https://rpc-api.node1.mainnet-beta.rarimo.com"
    const val REGISTRATION_ADDRESS = "0xC97c08F18F03bF14c7013533A53fbCe934E5Cb1e"
    const val PROPOSAL_ADDRESS = "0xb6407f0bb10fDC61863253e0ca36531Fc6D4aedE"

    const val BLOCK_CHAIN_RPC_LINK = "https://rpc.qtestnet.org"


    const val REGISTRATION_TYPE = "Simple Registration"

    const val PRIVACY_POLICY_URL = "https://www.iranians.vote/privacy-policy.html"

}