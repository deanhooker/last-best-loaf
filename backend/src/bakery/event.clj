(ns bakery.event
  (:require
   [bakery.bake-day :refer [get-bake-day]]
   [bakery.bake-day-products :refer [list-bake-day-products]]
   [bakery.products :refer [products-map]]))

(defn- event-products
  "List complete application products for a specified event."
  [id]
  (let [bake-day-products (list-bake-day-products id)
        all-products (products-map)]
    (->> bake-day-products
         (map (fn [product]
                (let [id (:product_id product)]
                  (merge (get all-products id) product)))))))

(defn get-event
  "A full event with summary and products."
  [id]
  (let [bake-day (get-bake-day id)
        event-products (event-products id)]
    (assoc bake-day :products event-products)))
