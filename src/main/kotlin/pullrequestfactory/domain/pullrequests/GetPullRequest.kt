package pullrequestfactory.domain.pullrequests

import kotlinx.serialization.Serializable

@Serializable
data class GetPullRequest(val number: Int, val title: String)
