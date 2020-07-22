package pullrequestfactory.domain.pullrequests

class PullRequestLastFinishedMarker : PullRequestMarker {

    override fun mark(pullRequests: List<PullRequest>): List<PullRequest> {
        val prs = PullRequestLastNotFinishedMarker().mark(pullRequests)
        val newLastPr = prs.last().mark_title_finished()
        return prs.dropLast(1) + newLastPr
    }

}
