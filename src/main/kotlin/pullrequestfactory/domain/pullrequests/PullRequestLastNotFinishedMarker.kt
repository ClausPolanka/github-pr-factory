package pullrequestfactory.domain.pullrequests

class PullRequestLastNotFinishedMarker : PullRequestMarker {

    override fun mark(pullRequests: List<PullRequest>): List<PullRequest> {
        val prs = pullRequests.toMutableList()
        for (prIdx in 1 until prs.size) {
            val prevPr = prs[prIdx - 1]
            val nextPr = prs[prIdx]
            prs[prIdx - 1] = prevPr.mark_title_finished_when_next_pull_request_has_new_iteration(nextPr)
        }
        return prs
    }

}
