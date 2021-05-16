# Change Log
All notable changes to this project will be documented in this file. We try to 
adhere to https://github.com/olivierlacan/keep-a-changelog.

## [1.4.0-SNAPSHOT] 

### Added
- [Issue-#35] Add -d, --debug flag to open and close command. Currently, when active rate limit info is shown

### Changed     

### Fixed       
- [Issue-#36] GitHub API accepts PullRequest JSON to open new pull requests

### Removed     

### Internal    
- [Issue-#31] Reformat code according to Kotlin coding conventions
- [Issue-#33] Move all GitHub API calls to GitHubAPIClient to adhere to the Single Responsibility Principle
- [Issue-#40] Migrate Klaxon to kotlinx.serialization JSON Serializer library

## [1.3.0] - 2021-02-01 

### Added
- [Issue-#14] GitHub's authentication token configurable through app.properties
- [Issue-#29] Improve app user experience by using `Clikt` library for command line user interaction 

### Changed

### Fixed
- [Issue-#18] Use GitHub's OAuth 2.0 token for Authorization
- [Issue-#22] Make `OPEN` commands' `-g` option optional when GitHub token is set in user.properties
- [Issue-#25] Make `CLOSE` commands' `-g` option optional when GitHub token is set in user.properties
- [Issue-#27] Support `-l` (last iteration is finished) option for interactive `OPEN` command

### Removed

### Internal
- [Issue-#20] Update patch version of Maven dependencies

## [1.2.0] - 2020-10-21

### Added
- [Issue-#14] Interactive command to open pull requests for better user experience

### Changed

### Fixed
- [Issue-#7] Remove legacy pairing-partner names (new ones are SHUBHI and LAMPE)

### Removed

### Internal

## [1.1.0] - 2020-09-01

### Added
- [Issue-#4] Interactive command to close pull requests for better user experience

### Fixed
- [Issue-#6] Allow multiple sessions per pairing-partner

### Internal

## [1.0.0] - 2020-06-12

### Added
- Open pull requests for George 2nd round hiring candidates via CLI
- Close pull requests for George 2nd round hiring candidates via CLI

[1.4.0-SNAPSHOT]: https://github.com/ClausPolanka/github-pr-factory/compare/github-pr-factory-1.3.0...master
[1.3.0]: https://github.com/ClausPolanka/github-pr-factory/compare/github-pr-factory-1.2.0...github-pr-factory-1.3.0
[1.2.0]: https://github.com/ClausPolanka/github-pr-factory/compare/github-pr-factory-1.1.0...github-pr-factory-1.2.0
[1.1.0]: https://github.com/ClausPolanka/github-pr-factory/compare/github-pr-factory-1.0.0...github-pr-factory-1.1.0
[Issue-#4]: https://github.com/ClausPolanka/github-pr-factory/issues/4
[Issue-#6]: https://github.com/ClausPolanka/github-pr-factory/issues/6
[Issue-#7]: https://github.com/ClausPolanka/github-pr-factory/issues/7
[Issue-#14]: https://github.com/ClausPolanka/github-pr-factory/issues/14
[Issue-#18]: https://github.com/ClausPolanka/github-pr-factory/issues/18
[Issue-#20]: https://github.com/ClausPolanka/github-pr-factory/issues/20
[Issue-#22]: https://github.com/ClausPolanka/github-pr-factory/issues/22
[Issue-#25]: https://github.com/ClausPolanka/github-pr-factory/issues/25
[Issue-#27]: https://github.com/ClausPolanka/github-pr-factory/issues/27
[Issue-#29]: https://github.com/ClausPolanka/github-pr-factory/issues/29
[Issue-#31]: https://github.com/ClausPolanka/github-pr-factory/issues/31
[Issue-#33]: https://github.com/ClausPolanka/github-pr-factory/issues/33
[Issue-#35]: https://github.com/ClausPolanka/github-pr-factory/issues/35
[Issue-#36]: https://github.com/ClausPolanka/github-pr-factory/issues/36
[Issue-#40]: https://github.com/ClausPolanka/github-pr-factory/issues/40
