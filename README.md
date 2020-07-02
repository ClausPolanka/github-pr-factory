## github-pr-factory
George Hiring Github Pull Request Factory

The **github-pr-factory** allows creating pull requests via command line.
The candidate has to create git branches with the following naming scheme: 
<pre><span><</span>first_name>_<span><</span>last_name>_iteration_<span><</span>nr>_<span><</span>pairing_partner></pre>
for example: <pre>firstname_lastname_iteration_1_pairingpartner</pre>

### Branching rules
The candidate hast to create a new branch when
- a new iteration starts
- a new pairing partner joins.

For example: a new iteration starts:<br />
`firstname_lastname_iteration_1_pairingpartner1`<br />
`firstname_lastname_iteration_2_pairingpartner1`<br />

For example: a new pairing partner joins:<br />
`firstname_lastname_iteration_1_pairingpartner1`<br />
`firstname_lastname_iteration_1_pairingpartner2`<br />

Note: New branches always must branch of the previous branch.

### github-pr-factory usage

<pre>
Usage: github-pr-factory [OPTION] COMMAND

A tool which helps opening and closing Github pull requests
for the George backend chapter 2nd round hirings.

Options:
  -?, --help			Print this help statement
  -v, --version			Print version information and quit

Commands:
  open		Open Github pull requests
  close		Open Github pull requests

Run 'github-pr-factory COMMAND --help' for more information on a command.
</pre>

### github-pr-factory open usage

<pre>
Usage: github-pr-factory open [OPTION]

Open new Github pull requests for a 2nd round hiring candidate

Options:
  -c		The candidate's first name and last-name separated by hyphen
  -g		Your Github basic authorization token
  -p		Seven pairing-partner names separated by hyphen
    		  Currently supported names:
    		  claus, berni, bernhard, nandor, dominik, mihai, lampe, shubi
    		  markus, tibor, christian, michal, tomas, peter, martin, john, andrej

Example:
  github-pr-factory open -c firstname-lastname \
    -g 10238sadf08klasjdf098 \
    -p claus-berni-nandor-dominik-mihai-lampe-shubi
</pre>

In root project-directory: <pre>mvn clean package</pre>

In root project-directory: <pre>java -jar target/github-pr-factory-<version>-jar-with-dependencies.jar open -c <first_name>-<last_name> -g <github_basic_auth_token> -p <[pairing_partner]></pre> 

For example: <pre>java -jar target/github-pr-factory-1.0-SNAPSHOT-jar-with-dependencies.jar open -c Firstname-Lastname -g asdif8970702als -p name1-name2-name3</pre>

### github-pr-factory close usage

<pre>
Usage: github-pr-factory close [OPTION]

Close Github pull requests for a 2nd round hiring candidate

Options:
  -c		The candidate's first name and last-name separated by hyphen
  -g		Your Github basic authorization token

Example:
  github-pr-factory close -c firstname-lastname \
    -g 10238sadf08klasjdf098 \
</pre>

In root project-directory: <pre>mvn clean package</pre>
In root project-directory: <pre>java -jar target/github-pr-factory-<version>-jar-with-dependencies.jar close -c <first_name>-<last_name> -g <github_basic_auth_token></pre>
For example: <pre>java -jar target/github-pr-factory-1.0-SNAPSHOT-jar-with-dependencies.jar close -c Firstname-Lastname -g asdif8970702als</pre>
### Continuous Integration with circleci

[![ClausPolanka](https://circleci.com/gh/ClausPolanka/github-pr-factory.svg?style=svg)](https://app.circleci.com/pipelines/github/ClausPolanka/github-pr-factory)
