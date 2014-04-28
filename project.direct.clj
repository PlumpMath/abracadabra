(defproject abracadabra "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}

  :dependencies [[com.stuartsierra/component "0.2.1"]

                 [hiccup "1.0.5"]

                 ;; Templating with mustache
                 [de.ubercode.clostache/clostache "1.3.1"]

                 ;; CSS generation
                 [garden "1.1.5"]

                 ;; EDN reader with location metadata
                 [org.clojure/tools.reader "0.8.3"]

                 ;; Liberator for API access
                 [liberator "0.11.0"]

                 ;; logging
                 [org.clojure/tools.logging "0.2.6"]
                 [ch.qos.logback/logback-classic "1.0.7" :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.2"]
                 [org.slf4j/jcl-over-slf4j "1.7.2"]
                 [org.slf4j/log4j-over-slf4j "1.7.2"]

                 ;; ext deps
                 [prismatic/schema "0.2.1"]
                 [http-kit "2.1.13"]
                 [bidi "1.10.3"]
                 [thheller/shadow-build "0.5.0"]
                 [prismatic/plumbing "0.2.2"]]

  :profiles {:dev {:dependencies [[org.clojure/clojure "1.6.0"]
                                  [org.clojure/tools.namespace "0.2.4"]]
                   :source-paths ["dev"
                                  "/home/malcolm/src/modular/modules/maker/src"
                                  "/home/malcolm/src/modular/modules/http-kit/src"
                                  "/home/malcolm/src/modular/modules/template/src"
                                  "/home/malcolm/src/modular/modules/clostache/src"
                                  "/home/malcolm/src/modular/modules/ring/src"
                                  "/home/malcolm/src/modular/modules/wire-up/src"
                                  "/home/malcolm/src/modular/modules/bidi/src"
                                  "/home/malcolm/src/modular/modules/menu/src"
                                  "/home/malcolm/src/modular/modules/cljs/src"
                                  "/home/malcolm/src/cylon/src"]}})
