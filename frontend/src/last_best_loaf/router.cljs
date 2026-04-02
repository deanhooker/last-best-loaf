(ns last-best-loaf.router
  (:require
   [reitit.frontend :as rf]
   [reitit.frontend.easy :as rfe]
   [re-frame.core :refer [dispatch]]))

(def routes
  ["/"
   ["" {:name :home-page}]
   ["menu" {:name :menu}]
   ["about" {:name :about}]
   ["cart" {:name :cart}]
   ["checkout" {:name :checkout}]
   ["contact" {:name :contact}]
   ["event"
    ["/:event-id" {:name :event}]]])

(def router
  (rf/router routes))

(defn on-navigate [match _]
  (when match
    (dispatch [:navigated match])))

(defn start! []
  (rfe/start!
   router
   on-navigate
   {:use-fragment false}))
