(ns abracadabra.web
  (:require
   [bidi.bidi :refer (path-for ->Redirect ->ResourcesMaybe)]
   [modular.bidi :refer (WebService)]
   [com.stuartsierra.component :as component]
   ;;[cylon.core :refer (new-optionally-protected-bidi-routes)]
   [hiccup.core :refer (html)]
   [modular.template :refer (wrap-template TemplateModel)]
   [garden.core :refer (css)]
   [garden.units :refer (pt em)]
   [garden.color :refer (rgb)]))

(defn styles [req]
  {:status 200
   :headers {"Content-Type" "text/css"}
   :body (css
          [:h1 :h2 :h3 {:color (rgb 0 0 154)}]
          [:td {:font-family "monospace" :font-size (pt 12)}]
          [:td.numeric {:text-align :right}]
          [:th.numeric {:text-align :right}]
          [:div.container-narrow {:margin-left (pt 10) :font-size (pt 12)}]
          [:dt {:float :left}]
          [:dd {:margin-left (em 12 )}]
          [:p {:width (em 60)}]
          [:div.index {:padding-left (em 2)
                       :padding-top (em 1)
                       }])})

(defn index []
  (fn [req]
    {:body
     (html
      [:div
       [:h1 "Congratulations!"]
       [:h2 "Your modular application is up and running."]
       [:p "(edit this code in " [:tt "src/abracadabra/web.clj"] ")"]])}))

(defn make-main-handlers []
  {:index (wrap-template (index))
   :styles styles})

(defn make-main-routes []
  ["/"
   [["" (->Redirect 307 :index)]
    ["index.html" :index]
    ["style.css" :styles]
    ["" (->ResourcesMaybe {:prefix "public/"})]]])

(defrecord MainRoutes [uri-context]
  component/Lifecycle
  (start [this] (assoc this
                  :handlers (make-main-handlers)
                  :routes (make-main-routes)))

  (stop [this] this)

  WebService
  (ring-handler-map [this] (:handlers this))
  (routes [this] (:routes this))
  (uri-context [_] uri-context)

  TemplateModel
  (template-model [_ {{routes :modular.bidi/routes} :request}]
    {:home-href (path-for routes :index)}))

(defn new-main-routes
  ([] (new-main-routes ""))
  ([context]
     (->MainRoutes context)
     #_(new-optionally-protected-bidi-routes
      (make-main-routes (make-main-handlers)) :context context)))
