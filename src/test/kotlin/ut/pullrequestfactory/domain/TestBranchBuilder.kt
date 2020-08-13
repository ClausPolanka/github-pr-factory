package ut.pullrequestfactory.domain

import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.branches.Branch

class TestBranchBuilder {

    private var _iteration: Int = 0
    private var _pairingPartner: PairingPartner? = null
    private var _pairingPartnerBranchName: String? = null
    private var _candidate: Candidate = Candidate("test-first-name", "test-last-name")

    fun with_iteration(iteration: Int): TestBranchBuilder {
        _iteration = iteration
        return this
    }

    fun with_pairing_partner(pairingPartner: PairingPartner): TestBranchBuilder {
        _pairingPartner = pairingPartner
        return this
    }

    fun with_pairing_partner(pairingPartner: String): TestBranchBuilder {
        _pairingPartnerBranchName = pairingPartner
        return this
    }

    fun with_candidate(candidate: Candidate): TestBranchBuilder {
        _candidate = candidate
        return this
    }

    fun build(): Branch {
        val name = listOf(
                _candidate.firstName.toLowerCase(),
                _candidate.lastName.toLowerCase(),
                "iteration",
                _iteration,
                pairing_partner() ?: throwException()
        ).joinToString("_")
        return Branch(name)
    }

    private fun pairing_partner(): String? {
        if (_pairingPartner == null && _pairingPartnerBranchName == null) {
            throwException()
        }
        return when (_pairingPartner) {
            null -> _pairingPartnerBranchName
            else -> _pairingPartner?.name?.toLowerCase()
        }
    }

    private fun throwException() {
        throw RuntimeException("Branch must contain pairing partner")
    }

}
