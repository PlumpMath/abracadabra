(ns abracadabra.template
  (:require
   [com.stuartsierra.component :as component]
   [modular.ring :refer (RingHandlerProvider handler)]
   [bidi.bidi :refer (path-for)]
   [clostache.parser :as parser]
   [hiccup.core :refer (html)]
   [clojure.java.io :as io]))

(defn make-content-wrapper [template-path menu]
  (fn [{routes :modular.bidi/routes :as req} resp]
    (parser/render-resource
     template-path
     (merge
      {:title "abracadabra"
       :app-name "abracadabra"
       :title-link "/"
       :menu (for [{:keys [label handler]} (sort-by :order menu)]
               {:listitem (html [:li [:a {:href (path-for routes handler)} label]])})}
      resp))))

(defrecord Template [path]
  component/Lifecycle
  (start [this] this)
  (stop [this] this)
  RingHandlerProvider
  (handler [this]
    (let [prov (first (filter (partial satisfies? RingHandlerProvider) (vals this)))
          _  (assert prov)
          h (handler prov)
          _ (assert h)]
      (fn [req]
        ;; The template injects itself in the request map under the
        ;; :template key
        (h (assoc req :template (make-content-wrapper path (-> this :menu :menuitems))))))))

(defn new-template [& {:keys [template]}]
  (assert template)
  (assert (io/resource template) (format "Failed to find template resource: %s" template))
  (->Template template))

(defn wrap-template
  "Ring middleware to apply the template to the request."
  [h]
  (fn [req]
    (let [template (:template req)
          content (h req)]
      (assert template)
      {:status 200
       :body (template req content)})))
