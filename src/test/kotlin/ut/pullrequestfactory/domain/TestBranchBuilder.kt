package ut.pullrequestfactory.domain

import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.branches.Branch

class TestBranchBuilder {

    private var _iteration: Int = 0
    private var _pairingPartner: PairingPartner? = null
    private var _candidate: Candidate = Candidate("test-first-name", "test-last-name")

    fun with_iteration(iteration: Int): TestBranchBuilder {
        _iteration = iteration
        return this
    }

    fun with_pairing_partner(pairingPartner: PairingPartner): TestBranchBuilder {
        _pairingPartner = pairingPartner
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
                _pairingPartner?.name?.toLowerCase()
        ).joinToString("_")
        return Branch(name)
    }

}
