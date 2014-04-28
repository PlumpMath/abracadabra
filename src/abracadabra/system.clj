;; By convention, component applications define a system namespace.

;; It is usual to create one system per environment type (e.g. dev,
;; test, production). That way, each environment can dictate a different
;; configuration, a different set of components and a different wiring
;; specification - in other words, a system has full control of which
;; components make up a system, how they are configured and how they are
;; put together.

;; A dev system might want to disable the access control,
;; or have a local password store. A test system might want to configure
;; some components that loads test data into a system (and tears it down
;; on every reset).

;; It is a conscious decision to assemble systems in code, rather than
;; configuration. This gives maximum control and flexibility.

(ns abracadabra.system
  (:refer-clojure :exclude (read))
  (:require
   [com.stuartsierra.component :refer (system-map system-using) :as component]
   [clojure.java.io :as io]
   [clojure.tools.reader :refer (read)]
   [clojure.tools.reader.reader-types :refer (indexing-push-back-reader)]
   [clojure.string :as str]

   [modular.http-kit :refer (new-webserver)]
   [modular.ring :refer (new-ring-binder RingBinding)]
   [modular.bidi :refer (new-router WebService)]
   [modular.maker :refer (make)]
   [modular.wire-up :refer (autowire-dependencies-satisfying)]
   [modular.clostache :refer (new-clostache-templater)]
   [modular.template :refer (new-single-template new-template-model-contributor TemplateModel)]
   [modular.menu :refer (new-menu-index new-bootstrap-menu MenuItems)]
   [modular.cljs :refer (new-cljs-module new-cljs-builder ClojureScriptModule)]

   ;;[cylon.core :refer (new-default-protection-system)]

   [abracadabra.api :refer (new-api-routes)]

   [abracadabra.web :refer (new-main-routes)]
   [abracadabra.module-a :refer (new-module-a-pages)]
   [abracadabra.module-b :refer (new-module-b-pages)]))

(defn config
  "Return a map of the static configuration used in the component
  constructors."
  []
  ;; In the future, dynamic configurators will be available, such as
  ;; juxt/stoic. Here, we simply use a local file in the user's home
  ;; directory.
  (let [f (io/file (System/getProperty "user.home") ".abracadabra-config.edn")]
    (if (.exists f)
      (read
       ;; This indexing-push-back-reader gives better information if the
       ;; file is misconfigured.
       (indexing-push-back-reader
        (java.io.PushbackReader. (io/reader f))))
      ;; If the file isn't there, we assume defaults.
      {})))

(defn new-system-map [config]
  (system-map
   :webserver (make new-webserver config :port nil)
   :router (make new-router)

   #_:protection-system
   #_(make new-default-protection-system config
         :password-file (io/file (System/getProperty "user.home") ".abracadabra-passwords.edn"))

   :main-routes (new-main-routes)
   :api-routes (new-api-routes)
   :html-template (make new-single-template config :template "templates/page.html.mustache")
   :module-a (make new-module-a-pages)
   :module-b (make new-module-b-pages)
   :menu-index (make new-menu-index)
   :bootstrap-menu (make new-bootstrap-menu)
   :clostache (make new-clostache-templater)
   :ring-binder (make new-ring-binder)

   :title (make new-template-model-contributor config
                :title-text (str/capitalize "abracadabra")
                :app-name "abracadabra")

   :cljs-core (new-cljs-module :name :cljs :mains ['cljs.core] :dependencies #{})
   :cljs-main (new-cljs-module :name :abracadabra :mains ['abracadabra.main] :dependencies #{:cljs})
   :cljs-builder (new-cljs-builder)))

(defn new-dependency-map [system-map]
  (-> {:webserver [:ring-binder]
       :ring-binder {:ring-handler :router}
       :html-template {:templater :clostache}
       :bootstrap-menu [:menu-index]}
      (autowire-dependencies-satisfying system-map :router WebService)
      (autowire-dependencies-satisfying system-map :ring-binder RingBinding)
      (autowire-dependencies-satisfying system-map :html-template TemplateModel)
      (autowire-dependencies-satisfying system-map :menu-index MenuItems)
      (autowire-dependencies-satisfying system-map :cljs-builder ClojureScriptModule)))

(defn new-system []
  (let [s-map (new-system-map (config))
        d-map (new-dependency-map s-map)]
    (clojure.pprint/pprint d-map)
    (component/system-using s-map d-map)))
