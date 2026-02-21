(ns last-best-loaf.main
  "Entry point for the website."
  (:require
   [re-frame.core :as rf]
   [reagent.dom :as dom]
   [last-best-loaf.events]
   [last-best-loaf.subs]
   [last-best-loaf.router :as router]
   [last-best-loaf.views :as views]))


;; `init` is called once on app startup.
;; `start` is called on code reload to re-render UI during dev

(defn ^:dev/after-load start []
  (dom/render
   [views/root]
   (.getElementById js/document "app")))

(defn ^:export init []
  (js/console.log "Initializing app")
  (rf/dispatch-sync [:initialize-db])
  (router/start!)
  (start))
