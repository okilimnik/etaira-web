= Fulcro RAD Demo (Firebase Version)

== Building the SPA

You must compile the CLJS source to run the client. If you want to be
able to edit it, just start a shadow-cljs watch:

[source, bash]
-----
$ yarn
$ clojure -M:cljs watch app
-----

if you don't have `yarn`, use `npm install` instead.


[source, bash]
Replace 'your-firebase-secret' with your firebase credentials.
-----
$ export FIREBASE_CONFIG=your-firebase-secret
$ clojure -M:clj
-----