ariJ - **a**nother **r**eally **i**nteresting **J**ira client
====
yes, I came up with this acronym right now.

It's my pet project, I want to learn new stuff, test different approaches using different libraries, so don't expect
well formed codebase, at least at the beginning.

Download
===
[![Google play](https://developer.android.com/images/brand/en_generic_rgb_wo_45.png)](http://play.google.com/store/apps/details?id=com.tadamski.arij)

or [compile it yourself](https://github.com/tmszdmsk/arij/blob/master/README.md#how-to-build)

Current state
===
* multiple accounts support
* presents list of first N issues assigned to logged in user
* screen with basic informations about issue
* allows to start work on issue with timer and log that work later

Features planned
===
* assigning tasks to logged in user
* transition between issue states (starting work, stopping it, resolving)
* comments view/add
* worklog view/add
* multiple filters
* tbd

How to build
===
I usually care about as little external dependencies as possible, so it shouldn't be a problem.

use [mosabua/maven-android-sdk-deployer](https://github.com/mosabua/maven-android-sdk-deployer) to install some dependencies in your local repository and call

```bash
mvn install
``` 
and some magic should happen ;)
