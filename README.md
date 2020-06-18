## github-pr-factory
George Hiring Github Pull Request Factory

The **github-pr-factory** allows creating pull requests via command line.
The candidate has to create git branches with the following naming scheme: 
<pre><span><</span>first_name>_<span><</span>last_name>_iteration_<span><</span>nr>_<span><</span>pairing_partner></pre>
for example: <pre>john_doe_iteration_1_claus</pre>

### Branching rules
The candidate hast to create a new branch when
- a new iteration starts
- a new pairing partner joins.

For example: a new iteration starts:<br />
`jon_doe_iteration_1_pairingpartner1`<br />
`jon_doe_iteration_2_pairingpartner1`<br />

For example: a new pairing partner joins:<br />
`jon_doe_iteration_1_pairingpartner1`<br />
`jon_doe_iteration_1_pairingpartner2`<br />

Note: New branches always must branch of the previous branch.

### How to run github-pr-factory
In root project-directory: <pre>mvn clean package</pre>

In root project-directory: <pre>java -jar target/github-pr-factory-<version>-jar-with-dependencies.jar <first_name>-<last_ame> <github_basic_auth_token> <[pairing_partner]></pre> 

For example: <pre>java -jar target/github-pr-factory-1.0-SNAPSHOT-jar-with-dependencies.jar Firstname-Lastname asdif8970702als name1-name2-name3`</pre>

[![ClausPolanka](https://circleci.com/gh/ClausPolanka/github-pr-factory.svg?style=svg)](https://app.circleci.com/pipelines/github/ClausPolanka/github-pr-factory)
