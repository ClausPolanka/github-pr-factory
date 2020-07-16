package pullrequestfactory.domain.pullrequests

class PullRequestLastNotFinishedMarker : PullRequestMarker {

    override fun mark(pullRequests: List<PullRequest>): List<PullRequest> {
        for (prIdx in 1 until pullRequests.size) {
            pullRequests[prIdx - 1].mark_title_finished(pullRequests[prIdx])
        }
        return pullRequests
    }

}
