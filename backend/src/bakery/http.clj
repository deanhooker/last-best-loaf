(ns bakery.http
  (:require
   [bakery.db :as db]
   [reitit.ring :as ring]
   [ring.util.response :as res]))

;; TODO: Stubbed for now
(defn get-scheduled-bakes []
  {:day1 {:date "2026-02-28"
          :items {:bagels {:qty 2 :price 12}}}})

(defn ping [_]
  (res/response "pong"))

;; TODO: Responses should be JSON
(defn scheduled-bakes [_]
  (res/response (str (get-scheduled-bakes))))

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
    ["/bakes" {:get scheduled-bakes}]]))

(def app
  (ring/ring-handler router))
