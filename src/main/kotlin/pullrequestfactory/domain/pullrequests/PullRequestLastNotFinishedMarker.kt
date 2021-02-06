package pullrequestfactory.domain.pullrequests

class PullRequestLastNotFinishedMarker : PullRequestMarker {

    override fun markTitlesOf(pullRequests: List<PullRequest>): List<PullRequest> {
        val prs = pullRequests.toMutableList()
        for (prIdx in 1 until prs.size) {
            val prevPr = prs[prIdx - 1]
            val nextPr = prs[prIdx]
            prs[prIdx - 1] = prevPr.markTitleWhenNextHasNewIteration(nextPr)
        }
        return prs
    }

}
