# github-pr-factory
George Hiring Github Pull Request Factory

The **github-pr-factory** allows creating pull requests via command line.
The candidate has to create git branches with the following naming scheme: 
`<first_name>_<last_name>_iteration_<nr>_<pairing_partner_name>` for example: `john_doe_iteration_1_claus`.

# How to run github-pr-factory
In root project-directory: `mvn package -DskipTests`

In root project-directory: `java -jar target/github-pr-factory-<version>-jar-with-dependencies.jar <first_name>-<last_ame> <basic_auth_token> <[pairing_partner]>` 

For example: `java -jar target/github-pr-factory-1.0-SNAPSHOT-jar-with-dependencies.jar Firstname-Lastname asdif8970702als name1-name2-name3`
