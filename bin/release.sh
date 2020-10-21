#! /bin/sh

# ! Make sure Nexus Maven Proxy is running !

# This file got copied from https://github.com/alexec/reltut/blob/master/release.sh
# and minor changes were done to optimize release process for this project.

# -e  Exit immediately if a command exits with a non-zero status.
# -u  Treat unset variables as an error when substituting.
set -eu

function commitChangedPoms() {
    POMS=$(find . -name pom.xml -not -path '*/target/*')
    if [ $(git status -s $POMS|grep -vc '^??') -gt 0 ]; then
        git commit -m "$1" $POMS
        git push
    fi
}

if [ $(git branch | grep '^*' | cut -c 2-) = 'master' ]; then
    echo 'Performing mainline release'

    # figure out version
    VERSION=$(mvn help:evaluate -Dexpression=project.version|grep '^[0-9]*\.[0-9]*\.[0-9]*-SNAPSHOT'|sed 's/-SNAPSHOT//')

    MAJOR=$(echo $VERSION|tr '.' ' '|awk '{print $1}')
    MINOR=$(echo $VERSION|tr '.' ' '|awk '{print $2}')
    PATCH=$(echo $VERSION|tr '.' ' '|awk '{print $3}')

    NEW_DEV_VERSION=$MAJOR.$(expr $MINOR + 1).0-SNAPSHOT

    # Prepare Changelog for new release
    GITHUB_COMPARE_URL="https:\/\/github\.beeone\.at\/George\/georgebackend\/compare"
    RELEASE_DATE=$(date +"%Y-%m-%d")
    PREVIOUS_RELEASE="release-$MAJOR\.$(expr $MINOR - 1)\.0"

    sed -i "s/## \[$VERSION-SNAPSHOT\]/\
## \[$NEW_DEV_VERSION\] \
\n\n### Added       \
\n\n### Changed     \
\n\n### Fixed       \
\n\n### Removed     \
\n\n### Internal    \
\n\n## \[$VERSION\] - $RELEASE_DATE/g" CHANGELOG.md

    sed -i "s/\[$VERSION-SNAPSHOT\]: $GITHUB_COMPARE_URL\/$PREVIOUS_RELEASE\.\.\.master/\
\[$NEW_DEV_VERSION\]: $GITHUB_COMPARE_URL\/release-$VERSION\.\.\.master\
\n\[$VERSION\]: $GITHUB_COMPARE_URL\/$PREVIOUS_RELEASE\.\.\.release-$VERSION\
/g" CHANGELOG.md

    git add CHANGELOG.md
    git commit -m "Release `$VERSION` in changelog"
    git push
    # End of Changelog preperation for new release

    # create a mainline release branch
    BRANCH='release/'$MAJOR.$MINOR.x

    mvn release:branch -B -DbranchName=$BRANCH  \
        -DreleaseVersion=$MAJOR.$MINOR.0        \
        -DdevelopmentVersion=$NEW_DEV_VERSION

    git checkout $BRANCH

    # perform release
    mvn release:prepare release:perform -B -DreleaseVersion=$MAJOR.$MINOR.0
    commitChangedPoms 'Updated master project version after release'

    git checkout master
    commitChangedPoms 'Updated master dependency minor versions after release'
else
    echo 'Performing interim release'
    mvn release:prepare release:perform -B
    commitChangedPoms 'Updated branch project version after release'
fi
