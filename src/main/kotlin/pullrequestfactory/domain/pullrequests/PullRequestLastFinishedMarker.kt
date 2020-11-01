package pullrequestfactory.domain.pullrequests

class PullRequestLastFinishedMarker : PullRequestMarker {

    override fun mark_titles_of(pullRequests: List<PullRequest>): List<PullRequest> {
        val prs = PullRequestLastNotFinishedMarker().mark_titles_of(pullRequests)
        if (prs.isEmpty()) {
            return emptyList()
        }
        val newLastPr = prs.last().mark_title()
        return prs.dropLast(1) + newLastPr
    }

}
