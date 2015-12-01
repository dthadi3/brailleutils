#!/bin/bash

if [ "$TRAVIS_REPO_SLUG" == "joeha480/brailleutils" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then

  echo -e "Publishing javadoc...\n"

  cp -R braille-utils.api/build/docs/javadoc $HOME/braille-utils.api
  cp -R braille-utils.pef-tools/build/docs/javadoc $HOME/braille-utils.pef-tools

  cd $HOME
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "travis-ci"
  git clone --quiet https://${GH_TOKEN}@github.com/joeha480/joeha480.github.io  > /dev/null

  cd joeha480.github.io
  git rm -rf ./braille-utils.api
  git rm -rf ./braille-utils.pef-tools
  
  cp -Rf $HOME/braille-utils.api ./braille-utils.api
  cp -Rf $HOME/braille-utils.pef-tools ./braille-utils.pef-tools
  git add -f .
  git commit -m "Lastest successful travis build of brailleutils ($TRAVIS_BUILD_NUMBER) auto-pushed to joeha480.github.io"
  git push -fq origin master > /dev/null

  echo -e "Published javadocs to joeha480.github.io.\n"
  
fi
