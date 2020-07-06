package pullrequestfactory.domain.caches

class NoopCache : Cache {

    override fun cache(json: String, pageNr: Int) {
        // no caching needed
    }

}
