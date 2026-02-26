(ns bakery.server
  (:require
   [bakery.http :as http]
   [ring.adapter.jetty :as jetty]))

(defonce server (atom nil))

(defn start []
  (if-not @server
    (do (reset! server
                (jetty/run-jetty #'http/app
                              {:port 3000
                               :join? false}))
      :started)
    :already-running))

(defn stop []
  (if-let [s @server]
    (do
      (.stop s)
      (reset! server nil)
      :stopped)
    :no-server-running))

(defn restart []
  (stop)
  (start))
