# abracadabra

Work in progress, will eventually become a lein-template.

The name abracadabra was chosen to be easy to search-and-replace when
this project becomes a Leiningen template.

# Prep

Build latest dependencies (these will be released soon to clojars.org, making this step redundant)

    git clone https://github.com/juxt/modular.git
    cd modular
    lein install-all
    cd ..

    git clone https://github.com/juxt/cylon.git
    cd cylon
    lein install
    cd ..

# Go

    git clone https://github.com/malcolmsparks/abracadabra.git
    cd abracadabra
    lein repl
    user> (dev)
    user> (go)
    user> (reset)
    user> (reset)
    user> (reset)
    user> (reset)
    user> (reset) ; you get the idea


Browse at http://localhost:3000.
