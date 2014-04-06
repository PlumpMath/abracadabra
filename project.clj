(defproject abracadabra "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.stuartsierra/component "0.2.1"]
                 [juxt/modular "0.3.0-SNAPSHOT"]

                 [juxt.modular/http-kit "0.3.0-SNAPSHOT"]
                 [juxt.modular/bidi "0.3.0-SNAPSHOT"]

                 [hiccup "1.0.5"]
                 [bidi "1.10.2"]
                 [cylon "0.1.2-SNAPSHOT" :exlusions [hiccup bidi juxt.modular/bidi]]

                 ;; Templating with mustache
                 [de.ubercode.clostache/clostache "1.3.1"]

                 ;; CSS generation
                 [garden "1.1.5"]

                 ;; EDN reader with location metadata
                 [org.clojure/tools.reader "0.8.3"]

                 ;; Liberator for API access
                 [liberator "0.11.0"]]

  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.4"]]
                   :source-paths ["dev"]}})
