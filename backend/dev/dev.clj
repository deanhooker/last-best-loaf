(ns dev
  (:require
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
  (products/create-product! products/rosemary-bagels))
