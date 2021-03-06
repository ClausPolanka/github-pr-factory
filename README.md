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
Usage: github-pr-factory [OPTIONS] COMMAND [ARGS]...

Options:
  --version   Show the version and exit
  -h, --help  Show this message and exit

Commands:
  open   pull requests. App prompts for options if not passed
  close  pull requests. App prompts for options if not passed
</pre>

### github-pr-factory open usage

<pre>
Usage: github-pr-factory open [OPTIONS]

  Opens pull requests. App prompts for options if not passed.

Options:
  -d, --debug
  -fn, --first-name TEXT           Candidate's first name
  -ln, --last-name TEXT            Candidate's last name
  -g, --github-token TEXT          Your personal GitHub authorization token.
                                   Can be set in a file user.properties in the
                                   root directory. The file's format:
                                   "github-token=your-token"
  -l, --last-finsished             Was the last iteration finished by the
                                   candidate? App doesn't prompt for this
                                   option.
  -pp1, --pairing-partner-1 [ANDREJ|SHUBHI|CLAUS|BERNI|DOMINIK|MIHAI|MICHAL|NANDOR|CHRISTIAN|TOMAS|LAMPE|MARKUS|JOHN|MARTIN|PETER|TIBOR|JAKUB|LUKAS|JOSEF|JAROMIR|VACLAV]
  -pp2, --pairing-partner-2 [ANDREJ|SHUBHI|CLAUS|BERNI|DOMINIK|MIHAI|MICHAL|NANDOR|CHRISTIAN|TOMAS|LAMPE|MARKUS|JOHN|MARTIN|PETER|TIBOR|JAKUB|LUKAS|JOSEF|JAROMIR|VACLAV]
  -pp3, --pairing-partner-3 [ANDREJ|SHUBHI|CLAUS|BERNI|DOMINIK|MIHAI|MICHAL|NANDOR|CHRISTIAN|TOMAS|LAMPE|MARKUS|JOHN|MARTIN|PETER|TIBOR|JAKUB|LUKAS|JOSEF|JAROMIR|VACLAV]
  -pp4, --pairing-partner-4 [ANDREJ|SHUBHI|CLAUS|BERNI|DOMINIK|MIHAI|MICHAL|NANDOR|CHRISTIAN|TOMAS|LAMPE|MARKUS|JOHN|MARTIN|PETER|TIBOR|JAKUB|LUKAS|JOSEF|JAROMIR|VACLAV]
  -pp5, --pairing-partner-5 [ANDREJ|SHUBHI|CLAUS|BERNI|DOMINIK|MIHAI|MICHAL|NANDOR|CHRISTIAN|TOMAS|LAMPE|MARKUS|JOHN|MARTIN|PETER|TIBOR|JAKUB|LUKAS|JOSEF|JAROMIR|VACLAV]
  -pp6, --pairing-partner-6 [ANDREJ|SHUBHI|CLAUS|BERNI|DOMINIK|MIHAI|MICHAL|NANDOR|CHRISTIAN|TOMAS|LAMPE|MARKUS|JOHN|MARTIN|PETER|TIBOR|JAKUB|LUKAS|JOSEF|JAROMIR|VACLAV]
  -pp7, --pairing-partner-7 [ANDREJ|SHUBHI|CLAUS|BERNI|DOMINIK|MIHAI|MICHAL|NANDOR|CHRISTIAN|TOMAS|LAMPE|MARKUS|JOHN|MARTIN|PETER|TIBOR|JAKUB|LUKAS|JOSEF|JAROMIR|VACLAV]
  -h, --help                       Show this message and exit
</pre>

### github-pr-factory close usage

<pre>
Usage: github-pr-factory close [OPTIONS]

  Closes pull requests. App prompts for options if not passed.

Options:
  -d, --debug
  -g, --github-token TEXT  Your personal GitHub authorization token. Can be
                           set in a file user.properties in the root
                           directory. The file's format:
                           "github-token=your-token"
  -fn, --first-name TEXT   Candidate's first name
  -ln, --last-name TEXT    Candidate's last name
  -h, --help               Show this message and exit
</pre>

### user.properties - Setting user properties to reduce typing

You can set your Github authentication token in a file called `user.properties`. 
The `github-pr-factory` app expects the file to be either on the classpath or in 
the same directory in which the app gets executed. 
In case you are using a `user.properties` you can set the Github authentication 
token by adding a key-value-pair `github-token=TODO` to the file where the `TODO` 
must be replaced with your token. Therefore, you don't need to pass the token 
neither to the `open` nor `close` command.

### Continuous Integration with circleci

[![ClausPolanka](https://circleci.com/gh/ClausPolanka/github-pr-factory.svg?style=svg)](https://app.circleci.com/pipelines/github/ClausPolanka/github-pr-factory)
### Automated Code Quality Check via bettercodehub

[![BCH compliance](https://bettercodehub.com/edge/badge/ClausPolanka/github-pr-factory?branch=master)](https://bettercodehub.com/)
