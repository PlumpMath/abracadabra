(defproject abracadabra "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}

  :dependencies [[com.stuartsierra/component "0.2.1"]

                 [juxt.modular/http-kit "0.4.0-SNAPSHOT"]
                 [juxt.modular/bidi "0.4.0-SNAPSHOT"]
                 [juxt.modular/template "0.1.0-SNAPSHOT"]
                 [juxt.modular/clostache "0.1.0-SNAPSHOT"]
                 [juxt.modular/wire-up "0.1.0-SNAPSHOT"]
                 [juxt.modular/cljs "0.4.0-SNAPSHOT"]
                 [juxt.modular/menu "0.1.0-SNAPSHOT"]
                 [juxt.modular/maker "0.1.0-SNAPSHOT"]

                 [hiccup "1.0.5"]
                 ;;[cylon "0.2.0-SNAPSHOT" :exlusions [hiccup bidi juxt.modular/bidi]]

                 ;; CSS generation
                 [garden "1.1.5"]

                 ;; EDN reader with location metadata
                 [org.clojure/tools.reader "0.8.3"]

                 ;; Liberator for API access
                 [liberator "0.11.0"]]

  :profiles {:dev {:dependencies [[org.clojure/clojure "1.6.0"]
                                  [org.clojure/tools.namespace "0.2.4"]]
                   :source-paths ["dev"]}})
