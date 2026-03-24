(ns bakery.bake-day-products
  (:require
   [bakery.db :as db]
   [bakery.schema.bake-day-products :as schema]
   [malli.core :as m]
   [next.jdbc.sql :as sql]))

(def table :bake_day_products)

(defn add-product! [product]
  (when-not (m/validate schema/BakeDayProductInput product)
    (throw (ex-info "Invalid bake day product input"
                    {:errors (m/explain schema/BakeDayProductInput product)})))
  (sql/insert! db/datasource table product))

(defn list-bake-day-products [bake-day-id]
  (sql/query
   db/datasource
   [(str "SELECT * FROM " (name table)
         " WHERE bake_day_id = " bake-day-id)]
   db/opts))

;; TODO: Update inventory
(def day1-classic-loaf
  {:bake_day_id 1
   :product_id 1
   :inventory 20})

(def day2-plain-bagels
  {:bake_day_id 2
   :product_id 2
   :inventory 12})

(def day2-rosemary-bagels
  {:bake_day_id 2
   :product_id 3
   :inventory 8})
