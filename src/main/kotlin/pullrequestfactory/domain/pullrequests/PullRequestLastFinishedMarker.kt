package pullrequestfactory.domain.pullrequests

class PullRequestLastFinishedMarker : PullRequestMarker {

    override fun markTitlesOf(pullRequests: List<PullRequest>): List<PullRequest> {
        val prs = PullRequestLastNotFinishedMarker().markTitlesOf(pullRequests)
        if (prs.isEmpty()) {
            return emptyList()
        }
        val newLastPr = prs.last().markTitle()
        return prs.dropLast(1) + newLastPr
    }

}
