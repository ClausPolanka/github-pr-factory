package pullrequestfactory

import pullrequestfactory.io.Programs

fun main(args: Array<String>) {
    val p = Programs().create_program(args)
    p.execute()
}
