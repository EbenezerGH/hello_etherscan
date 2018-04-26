package jfyg.data.transaction

import io.reactivex.Single
import jfyg.data.TxExecutionStatus
import jfyg.data.TxReceiptStatus
import jfyg.network.queries.ApiQuery
import jfyg.utils.Const

/**
 * https://etherscan.io/apis#transactions
 */
class TxStatus : TxStatusContract {

    private val query = ApiQuery()
    private val genericNetworkQuery = query.txReceiptStatus("transaction",
            "getstatus",
            Const.TRANSACTION_PUBLIC_ADDRESS)

    /**
     * [BETA] Check Contract Execution Status (if there was an error during contract execution)
     * Note: isError":"0" = Pass , isError":"1" = Error during Contract Execution
     */
    override fun getTxExecutionStatus(txHash: String): Single<TxExecutionStatus> =
            query.txExecutionStatus("transaction",
                    "getstatus",
                    txHash).map { it.result }

    /**
     * [BETA] Check Transaction Receipt Status (Only applicable for Post Byzantium fork transactions)
     * Note: status: 0 = Fail, 1 = Pass. Will return null/empty value for pre-byzantium fork
     */
    override fun getTxReceiptStatus(txHash: String): Single<TxReceiptStatus> =
            query.txReceiptStatus("transaction",
                    "gettxreceiptstatus",
                    txHash).map { it.result }

    /**
     * Return network status
     */
    override fun getNetworkStatus(): Single<String> = genericNetworkQuery.map { it.status }

    /**
     * Return network message
     */
    override fun getNetworkMessage(): Single<String> = genericNetworkQuery.map { it.message }

}