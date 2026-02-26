(ns bakery.http
  (:require
   [bakery.db :as db]
   [reitit.ring :as ring]
   [ring.util.response :as res]
   [ring.middleware.cors :refer [wrap-cors]]))

(defn ping [_]
  (res/response "pong"))

(defn scheduled-bakes [_]
  (-> (res/response (pr-str db/bakes-stub))
      (res/content-type "application/edn")))

;; TODO: Move to db ns
(defn db-ok? []
  (try
    (db/test-connection)
    true
    (catch Exception _
      false)))

(defn health [_]
  (let [db (db-ok?)]
    (-> (str {:server "ok"
              :database (if db "ok" "error")})
        res/response
        (res/status (if db 200 503)))))

(def router
  (ring/router
   [["/ping" {:get ping}]
    ["/health" {:get health}]
    ["/api/bakes" {:get scheduled-bakes}]]))

(def app
  (wrap-cors (ring/ring-handler router)
             :access-control-allow-origin [#"http://localhost:8000"]
             :access-control-allow-methods [:get :post :put :delete]
             :access-control-allow-headers ["Content-Type"]))
