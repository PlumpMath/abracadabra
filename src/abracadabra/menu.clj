(ns abracadabra.menu
  (:require
   [com.stuartsierra.component :as component]
   [modular.index :refer (Index)]))

(defprotocol MenuItems
  (menu-items [_]))

(defrecord MenuIndex []
  component/Lifecycle
  (start [this]
    (assoc this
      :menuitems (->> this vals (filter (partial satisfies? MenuItems)) (mapcat menu-items))))
  (stop [this] this)

  Index
  (satisfying-protocols [this] #{MenuItems}))

(defn new-menu-index []
  (->MenuIndex))
