ariJ - **a**nother **r**eally **i**nteresting **J**ira 
====
...client for Android. Yes, I came up with this acronym right now.

It's my pet project, I want to learn new stuff, test different approaches using different libraries, so don't expect
well formed codebase, at least at the beginning.

Download
===
[![Google play](https://developer.android.com/images/brand/en_generic_rgb_wo_45.png)](http://play.google.com/store/apps/details?id=com.tadamski.arij)

or [compile it yourself](https://github.com/tmszdmsk/arij/blob/master/README.md#how-to-build)

Current state
===
* multiple accounts support
* presents list issues assigned to logged in user
* screen with basic informations about issue
* allows to start work on issue with timer and log that work later

Features planned
===
* assigning tasks to logged in user
* transition between issue states (starting work, stopping it, resolving)
* comments view/add
* worklog view/add
* multiple filters
* plugin for jira to support Google Cloud Messaging to send push notifications
* tbd

How to build
===
I usually care about as little external(non easy-maven-downloadable) dependencies as possible, so it shouldn't be a problem.
Just make sure that your ANDROID_HOME environment variable is set properly.

```bash
mvn install
``` 
and magic should happen ;)
If not, [create an issue](http://github.com/tmszdmsk/arij/issues/new) or write an [e-mail](mailto:tomasz.adamski@gmail.com)

License
===
MIT, details [here](http://github.com/tmszdmsk/arij/blob/master/LICENSE)

Thanks
===
* to [Cogision](http://cogision.com), company standing behind [UsabilityTools.com](http://usabilitytools.com) - all commits sent as "t.adamski" were created during research week.
