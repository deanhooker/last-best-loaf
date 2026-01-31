(ns last-best-loaf.router
  (:require
   [reitit.frontend :as rf]
   [reitit.frontend.easy :as rfe]
   [re-frame.core :refer [dispatch]]))

(def routes
  ["/"
   ["" {:name :menu}]
   ["cart" {:name :cart}]
   ["checkout" {:name :checkout}]])

(def router
  (rf/router routes))

(defn on-navigate [match _]
  (when match
    (dispatch [:navigate match])))

(defn start! []
  (rfe/start!
   router
   on-navigate
   {:use-fragment false}))
