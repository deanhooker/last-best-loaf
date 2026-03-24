(ns dev
  (:require
   [bakery.bake-day :as bake-day]
   [bakery.bake-day-products :as bake-day-products]
   [bakery.db :as db]
   [bakery.products :as products]
   [bakery.server :as server]))

(defn start []
  (server/start!))

(defn stop []
  (server/stop!))

(defn reset []
  (stop)
  (start))

(defn reset-db []
  (db/rollback)
  (db/migrate))

(defn seed-db []
  (products/create-product! products/classic-loaf)
  (products/create-product! products/plain-bagels)
  (products/create-product! products/rosemary-bagels)
  (bake-day/create-bake-day! bake-day/day1)
  (bake-day-products/add-product! bake-day-products/day1-classic-loaf)
  (bake-day/create-bake-day! bake-day/day2)
  (bake-day-products/add-product! bake-day-products/day2-plain-bagels)
  (bake-day-products/add-product! bake-day-products/day2-rosemary-bagels))
