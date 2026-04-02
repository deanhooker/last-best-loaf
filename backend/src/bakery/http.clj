(ns bakery.http
  (:require
   [bakery.bake-day :as bake-days]
   [bakery.bake-day-products :as bake-day-products]
   [bakery.db :as db]
   [bakery.event :as event]
   [bakery.products :as products]
   [cheshire.core :as json]
   [reitit.ring :as ring]
   [ring.util.response :as res]
   [ring.middleware.cors :refer [wrap-cors]]))

(defn ping [_]
  (res/response "pong"))

(defn products [_]
  (-> (products/list-products)
      json/generate-string
      res/response
      (res/content-type "application/json")))

(defn bake-days [_]
  (-> (bake-days/list-bake-days)
      json/generate-string
      res/response
      (res/content-type "application/json")))

(defn bake-day-products [req]
  (let [id (get-in req [:path-params :id])]
    (-> (bake-day-products/list-bake-day-products id)
        json/generate-string
        res/response
        (res/content-type "application/json"))))

(defn event [req]
  (let [id (get-in req [:path-params :id])]
    (-> (event/get-event id)
        json/generate-string
        res/response
        (res/content-type "application/json"))))

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
    ["/api/products" {:get products}]
    ["/api/bake-days" {:get bake-days}]
    ["/api/bake-day-products/:id" {:get bake-day-products}]
    ["/api/event/:id" {:get event}]]))

(def app
  (wrap-cors (ring/ring-handler router)
             :access-control-allow-origin [#"http://localhost:8000"]
             :access-control-allow-methods [:get :post :put :delete]
             :access-control-allow-headers ["Content-Type"]))
