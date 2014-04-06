(ns abracadabra.main
  (:gen-class))

(defn -main [& args]
  ;; We eval so that we don't AOT anything beyond this class
  (eval '(do (require 'abracadabra.system)
             (require 'com.stuartsierra.component)
             (com.stuartsierra.component/start (abracadabra.system/new-system)))))
