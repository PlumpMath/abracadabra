;; There are many comments in this namespace. The purpose is to explain
;; the coding techniques that we use in modular applications. You may
;; want to keep it for reference, or dive in and make changes.

;; In a modular application, a module contributes functionality to an
;; application. It should have a clean separation so that a module can
;; be easily introduced or removed from an application, or indeed
;; dynamically discovered.

;; For this to work, modules need integration points into the
;; application. Integration points are specified by Clojure
;; protocols. Protocols provide just enough of a surface to interface
;; between the application and its modules.

(ns ^{:doc "This module contains one or more application components"}
  abracadabra.module-a
  (:require
   [bidi.bidi :refer (path-for)]
   [modular.bidi :refer (WebService)]
   [modular.menu :refer (MenuItems)]
   [modular.template :refer (wrap-template)]
   [hiccup.core :refer (html)]
   [com.stuartsierra.component :as component]))

;; We'll start by defining 2 pages.

(defn make-page-a1 []
  (wrap-template
   ;; See modular.bidi documentation for how routes and assoc'd with the request
   (fn [{routes :modular.bidi/routes :as req}]
     {:body
      (html
       [:div
        [:h3 "Page A1"]
        ;; Example: How to use path-for to construct a URL to another
        ;; handler. Note the absence of a forward declaration of
        ;; feature-a2.
        [:p "Here is a (generated) link to " [:a {:href (path-for routes :page-2)} "page A2"]]
        [:p "Edit this code in " [:tt "src/abracadabra/module_a.clj"]]])})))

(defn make-page-a2 []
  (wrap-template
   ;; This is not a Ring handler, it is 'template data' that becomes a
   ;; Ring handler by being wrapped by wrap-template. Template data can
   ;; include the HTML page title.
   (fn [req]
     {:body (html
             [:div
              [:h3 "Page A2"]
              [:p "Edit this code in " [:tt "src/abracadabra/module_a.clj"]]])
      ;; We can also return maps that will be merged with the template
      ;; data of other contributing components. Here we must use (and
      ;; know) the component key as the component is known by the templater.
      :title {:title-text "Look how this page changed the title"}})))

(defn make-handlers
  "Make handlers for module A"
  []
  ;; Web applications often need to create hyperlinks. But we don't want
  ;; to create URLs using cumbersome string concatenation. Handler logic
  ;; should be agnostic about, and de-coupled from, routing. Handlers
  ;; may mutually link to each other, or links may form other cycles. We
  ;; could resolve these issues by def'ing handlers in vars and using
  ;; forward declarations to resolve cycles. But an easy alternative is
  ;; to provide the handler map as a promise which will be resolved
  ;; before any HTTP requests are made. This pattern of delivering the
  ;; promise inside the function body ensures that an unrealized promise
  ;; doesn't escape.
  {:page-1 (make-page-a1)
   :page-2 (make-page-a2)
   :page-3
   ;; An anonymous handler. Bidi can route to these as well.
   (fn [{routes :modular.bidi/routes :as req}]
     {:body (html [:div
                   [:p "Look, no boilerplate!"]
                   [:p "Edit this code in src/abracadabra/module_a.clj"]
                   [:p [:a {:href (path-for routes [:main-routes :index])} "Home"]]])})})

;; Now we create a component. This is a record defined by the protocols
;; it satisfies. Modules provide multiple components if necessary. We
;; provide just one that provides HTML pages, URL routes and menu-items
;; to them.

(defrecord ModuleAPages []
  component/Lifecycle
  (start [this]
    ;; Handlers are created here and assoc'd. There can then be
    ;; referenced for route association by this and other components.
    (assoc this :handlers (make-handlers)))
  (stop [this] this)

  WebService ; this protocol lets us contribute bidi routes
  (ring-handler-map [this] (:handlers this))
  (routes [this]
    ;; We return a bidi route structure that associates routes to
    ;; handlers. Each module contributes routes into the application's
    ;; URI tree by satisfying this BidiRoutesProvider provider. (The
    ;; design supports a CompojureRoutesProvider, a PR would be welcome!)
    ["/" {"a1" :page-1
          "a2" :page-2
          "a3" :page-3}])
  ;; If there is no web context under which these route will be mounted,
  ;; return nil or an empty string here. So, putting it altogether,
  ;; "/example-module" + "/" + "a1" = "/example-module/a1".
  (uri-context [this] "/example-module")

  MenuItems ; this protocol lets us contribute
  (menu-items [_ context]
    [{:label "Menu item 1" :order "A1" :href :page-1 :parent "Menu A"}
     {:label "Menu item 2" :order "A2" :href :page-2 :parent "Menu A"}
     {:label "Menu item 3" :order "A3" :href :page-3 :parent "Menu A"}]))

;; By convention, each component has one or more constructors, prefixed
;; with 'new-'.

(defn new-module-a-pages
  []
  (->ModuleAPages))
