(ns bakery.server
  (:require
   [bakery.db :as db]
   [bakery.http :as http]
   [ring.adapter.jetty :as jetty]))

(defonce server (atom nil))

(defn start! []
  (if-not @server
    (do
      (println "Database migrations...")
      (db/migrate)
      (println "Starting server...")
      (reset! server
              (jetty/run-jetty #'http/app
                               {:port 3000
                                :join? false}))
      (println "Started server"))
    (println "Server already running")))

(defn stop! []
  (if-let [s @server]
    (do
      (.stop s)
      (reset! server nil)
      (println "Stopped server"))
    (println "No server running")))
