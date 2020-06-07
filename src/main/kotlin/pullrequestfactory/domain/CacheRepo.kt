package pullrequestfactory.domain

interface CacheRepo {

    fun cache(json: String, pageNr: Int)

}
