(ns abracadabra.dev-system
  (:require
   [com.stuartsierra.component :as component]
   [abracadabra.system :refer (config new-system-map new-dependency-map)]))

(defn new-dev-system []
  "Create a development system"
  (let [s-map (new-system-map (config))
        d-map (new-dependency-map s-map)]
    (clojure.pprint/pprint d-map)
    (component/system-using
     s-map
     d-map)))
