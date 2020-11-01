#! /bin/sh

check_arg() {
  if [ -z "$1" ]
    then
      echo "USAGE: ./rename_branch.sh <OLD_NAME> <NEW_NAME>"
      exit 1
  fi
}

check_arg $1
check_arg $2

OLD_NAME=$1
NEW_NAME=$2

echo "*** Rename branch '$OLD_NAME' to '$NEW_NAME' ***"
echo
echo "*** Checkout master and rename branch locally ***"
echo
git checkout master
git branch -m $OLD_NAME $NEW_NAME
echo
echo "*** Delete '$OLD_NAME' remote branch and push the '$NEW_NAME' local branch ***"
echo
git push origin :$OLD_NAME $NEW_NAME
echo
echo "*** Checkout branch '$NEW_NAME' and reset the upstream branch ***"
echo
git checkout $NEW_NAME
git push origin -u $NEW_NAME

git checkout master
