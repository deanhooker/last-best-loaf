(ns dev
  (:require
   [bakery.products :as products]
   [bakery.server :as server]))

(defn start []
  (server/start!))

(defn stop []
  (server/stop!))

(defn reset []
  (stop)
  (start))

(defn seed-db []
  (products/reset-products-db!)
  (products/create-product! products/classic-loaf)
  (products/create-product! products/plain-bagels)
  (products/create-product! products/rosemary-bagels))
