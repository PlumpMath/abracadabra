(ns abracadabra.web
  (:require
   [bidi.bidi :refer (->Redirect ->ResourcesMaybe)]
   [com.stuartsierra.component :as component]
   [cylon.core :refer (new-optionally-protected-bidi-routes)]
   [hiccup.core :refer (html)]
   [garden.core :refer (css)]
   [garden.units :refer (pt em)]
   [garden.color :refer (rgb)]
   [abracadabra.template :refer (wrap-template)]))

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

(defn index [handlers]
  (fn [req]
    {:body
     (html
      [:div
       [:h1 "Congratulations!"]
       [:h2 "Your modular application is up and running."]
       [:p "(edit this code in " [:tt "src/abracadabra/web.clj"] ")"]])}))

(defn make-main-handlers []
  (let [p (promise)]
    @(deliver p {:index (wrap-template (index p))
                 :styles styles})))

(defn make-main-routes [handlers]
  ["/"
   [["" (->Redirect 307 (:index handlers))]
    ["index.html" (:index handlers)]
    ["style.css" (:styles handlers)]
    ["" (->ResourcesMaybe {:prefix "public/"})]]])

(defn new-main-routes
  ([] (new-main-routes ""))
  ([context]
     (new-optionally-protected-bidi-routes
      (make-main-routes (make-main-handlers)) :context context)))
