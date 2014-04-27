(ns abracadabra.api
  (:require
   [liberator.core :refer (defresource)]
   [modular.bidi :refer (new-web-service)]
   ;;[cylon.liberator :refer (make-composite-authenticator)]
   [com.stuartsierra.component :as component]))

(defresource entities [authorized?]
  :authorized? authorized?
  :available-media-types ["text/html"]
  :handle-ok "Entities")

(defn make-api-handlers [authorizer]
  {:entities (entities authorizer)})

(defn make-api-routes [handlers]
  ["/" [["entities" (:entities handlers)]]])

(defn new-api-routes []
  (new-web-service
    :ring-handler-map {:a nil}
    :routes ["/" :a] ; TODO
    )

  #_(component/using
     (new-web-service
    :ring-handler-map {:a nil}
    :routes ["/" :a] ; TODO
    #_(fn [component]
      (-> component :protection-system make-composite-authenticator
          make-api-handlers make-api-routes)))
   [:protection-system]))
