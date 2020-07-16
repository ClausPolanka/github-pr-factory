package pullrequestfactory.domain.pullrequests

class PullRequestLastFinishedMarker : PullRequestMarker {

    override fun mark(pullRequests: List<PullRequest>): List<PullRequest> {
        val prs = PullRequestLastNotFinishedMarker().mark(pullRequests)
        prs.last().mark_title_finished()
        return prs
    }

}
