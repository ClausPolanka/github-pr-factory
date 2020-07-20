package pullrequestfactory.domain.pullrequests

class PullRequestLastNotFinishedMarker : PullRequestMarker {

    override fun mark(pullRequests: List<PullRequest>): List<PullRequest> {
        for (prIdx in 1 until pullRequests.size) {
            val prevPr = pullRequests[prIdx - 1]
            val nextPr = pullRequests[prIdx]
            prevPr.mark_title_finished_when_next_pull_request_has_new_iteration(nextPr)
        }
        return pullRequests
    }

}
