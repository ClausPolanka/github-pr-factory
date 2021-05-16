package ut.pullrequestfactory.domain

import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.branches.Branch

class TestBranchBuilder {

    private var _iteration: Int = 0
    private var _pairingPartner: PairingPartner? = null
    private var _branchName: String? = null
    private var _pairingPartnerBranchName: String? = null
    private var _candidate: Candidate = Candidate("test-first-name", "test-last-name")

    fun withIteration(iteration: Int): TestBranchBuilder {
        _iteration = iteration
        return this
    }

    fun withPairingPartner(pairingPartner: PairingPartner): TestBranchBuilder {
        _pairingPartner = pairingPartner
        return this
    }

    fun withPairingPartner(pairingPartner: String): TestBranchBuilder {
        _pairingPartnerBranchName = pairingPartner
        return this
    }

    fun withCandidate(candidate: Candidate): TestBranchBuilder {
        _candidate = candidate
        return this
    }

    fun withBranchName(branchName: String): TestBranchBuilder {
        _branchName = branchName
        return this
    }

    fun build(): Branch {
        val name = _branchName ?: listOf(
            _candidate.firstName.lowercase(),
            _candidate.lastName.lowercase(),
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
            else -> _pairingPartner?.name?.lowercase()
        }
    }

    private fun throwException() {
        throw RuntimeException("Branch must contain pairing partner")
    }

}
