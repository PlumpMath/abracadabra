# abracadabra

Work in progress, will eventually become a lein-template.

The name abracadabra was chosen to be easy to search-and-replace when
this project becomes a Leiningen template.

# Set up

Build latest dependencies (these will be released soon to clojars.org, making this step redundant)

    git clone https://github.com/juxt/modular.git
    cd modular
    lein install-all
    cd ..

    git clone https://github.com/juxt/cylon.git
    cd cylon
    lein install
    cd ..

    git clone https://github.com/malcolmsparks/abracadabra.git
    cd abracadabra
    lein repl
    user> (go)
    user> (reset)
    user> (reset)

Browse at http://localhost:3000.

# Discussion

What this shows is a set of components, with a dependency tree that is formed by a number of inferences :-

* Components that satisfy `modular.Index` are introduced to components they want to index.
* A template component that can create boilerplate using mustache template is injected between the webserver and the bidi router, such that a template function is available on the request for handlers that want it.

There are a number of other things here:

* [bidi](https://github.com/juxt/bidi.git) routes and use of path-for
* Liberator API
* All of the above secured with [cylon](https://github.com/juxt/cylon.git)

Soon to come will be ClojureScript building (based on shadow-build), Om/React integration with cljs-ajax to call the Liberator API...
