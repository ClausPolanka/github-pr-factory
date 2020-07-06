package pullrequestfactory.domain.caches

interface Cache {

    fun cache(json: String, pageNr: Int)

}
