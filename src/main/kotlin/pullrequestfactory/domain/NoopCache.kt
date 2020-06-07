package pullrequestfactory.domain

class NoopCache : CacheRepo {

    override fun cache(json: String, pageNr: Int) {
        // no caching needed
    }

}
