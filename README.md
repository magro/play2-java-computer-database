# Play 2 Computer Database Sample

This is the demo code for my Play Framework 2 presentation at JAX 2013 ([slides](http://www.slideshare.net/martin_grotzke/jax2013-play2)). It's based on the computer-database sample shipped with the play distribution, modified to show more features.

It demonstrates:

* Accessing a JDBC database, using Ebean.
* Table pagination and CRUD forms.
* Integrating with a CSS framework (Twitter Bootstrap ).

Additionally to the play sample, it also shows:
* Asset compilation (coffee script)
* JavaScript reverse routing (used for ajax calls)
* RequireJS integration
* Asynchronous request handling (blocking, non-blocking)

`play-coda/` is the sample code itself, `coda-backend/` is the webservice
used for comparison of sync/async WS usage.
