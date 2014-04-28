(ns abracadabra.module-b
  (:require
   [com.stuartsierra.component :as component]
   [hiccup.core :refer (html)]
   [modular.template :refer (wrap-template)]
   [bidi.bidi :refer (path-for)]
   [modular.bidi :refer (WebService)]
   [modular.menu :refer (MenuItems)]))

(defrecord ModuleBPages []
  WebService
  (ring-handler-map [this]
    {:page-b1
     (wrap-template
      (fn [{routes :modular.bidi/routes :as req}]
        {:body
         (html
          [:div
           [:h3 "Page B1"]
           [:p "Here is a link across a module boundary, back to "
            ;; This demonstrates the construction of a href across a
            ;; module boundary. Instead of giving a keyword, we give a
            ;; pair of keywords. The first is the name of the component
            ;; that owns the handler. The second is the keyword of the
            ;; handler inside that component.
            [:a {:href (path-for routes [:module-a :page-1])}
             "page A1"]
            ]])}))})

  (routes [this] ["/b1" :page-b1])
  (uri-context [this] "/example-module-b")

  MenuItems                           ; this protocol lets us contribute
  (menu-items [_ _]
    [{:label "Menu item 4" :order "B1" :href :page-b1 :parent "Menu B"}
     ]))

(defn new-module-b-pages []
  (->ModuleBPages))
