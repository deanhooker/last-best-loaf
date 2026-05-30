(ns bakery.main
  "Entry point for the website."
  (:require
   [re-frame.core :as rf]
   [reagent.dom :as dom]
   [bakery.events]
   [bakery.subs]
   [bakery.router :as router]
   [bakery.views :as views]))


;; `init` is called once on app startup.
;; `start` is called on code reload to re-render UI during dev

(defn ^:dev/after-load start []
  (dom/render
   [views/root]
   (.getElementById js/document "app")))

(defn ^:export init []
  (js/console.log "Initializing app")
  (rf/dispatch-sync [:initialize-db])
  (rf/dispatch [:hero/init])
  (rf/dispatch [:hero/start])
  (router/start!)
  (start))
