#! /bin/sh
git push origin master
git fetch bplug
git subtree merge --squash -P build bplug/master
git push origin master
