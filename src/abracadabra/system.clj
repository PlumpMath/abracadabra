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

   [modular.http-kit :refer (new-webserver)]
   [modular.bidi :refer (new-bidi-ring-handler-provider BidiRoutesProvider)]
   [modular.core :refer (make make-args interpose-component merge-dependencies)]

   [modular.index :refer (add-index-dependencies)]

   [cylon.core :refer (new-default-protection-system)]

   [abracadabra.api :refer (new-api-routes)]
   [abracadabra.menu :refer (new-menu-index MenuItems)]
   [abracadabra.template :refer (new-template)]
   [abracadabra.web :refer (new-main-routes)]
   [abracadabra.module-a :refer (new-module-a-pages)]))

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
   :bidi-ring-handler (make new-bidi-ring-handler-provider)

   :protection-system
   (make new-default-protection-system config
         :password-file (io/file (System/getProperty "user.home") ".abracadabra-passwords.edn"))

   :home-routes (new-main-routes)
   :api-routes (new-api-routes)
   :html-template (make new-template config :template "templates/page.html.mustache")
   :module-a (make new-module-a-pages)
   :menu (make new-menu-index)
   ))

(defn new-dependency-map [system-map]
  (-> {}
      (add-index-dependencies system-map)
      (merge-dependencies {:html-template [:menu]})

      ;; We interpose the template component between the webserver and its
      ;; dependencies. That way, it can inject itself into every request
      ;; delivered by the webserver.
      (interpose-component :webserver :html-template)))

(defn new-system []
  (let [s-map (new-system-map (config))
        d-map (new-dependency-map s-map)]
    (clojure.pprint/pprint d-map)
    (component/system-using
     s-map
     d-map)))
