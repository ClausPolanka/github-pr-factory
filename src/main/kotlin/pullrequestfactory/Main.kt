package pullrequestfactory

import pullrequestfactory.io.Programs

fun main(args: Array<String>) {
    val p = Programs().create_program_for(args)
    p.execute()
}
