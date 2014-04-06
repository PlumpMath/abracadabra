(ns abracadabra.api
  (:require
   [liberator.core :refer (defresource)]
   [modular.bidi :refer (new-bidi-routes)]
   [cylon.liberator :refer (make-composite-authenticator)]
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
  (component/using
   (new-bidi-routes
    (fn [component]
      (-> component :protection-system make-composite-authenticator
          make-api-handlers make-api-routes)))
   [:protection-system]))
