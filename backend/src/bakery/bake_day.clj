(ns bakery.bake-day
  (:require
   [bakery.db :as db]
   [bakery.schema.bake-day :as schema]
   [malli.core :as m]
   [next.jdbc.sql :as sql])
  (:import
   [java.time LocalDate]))

;; TODO: Add pickup time window
;; TODO: Add pickup location
;; TODO: Add order by cutoff

(def table :bake_days)

(defn create-bake-day! [bake-day]
  (when-not (m/validate schema/BakeDayInput bake-day)
    (throw (ex-info "Invalid bake day"
                    {:errors (m/explain schema/BakeDayInput bake-day)})))

  (let [bake-day (update bake-day :date LocalDate/parse)]
    (sql/insert! db/datasource table bake-day)))

(defn list-bake-days []
  (sql/query db/datasource
             [(str "SELECT * FROM " (name table))]
             db/opts))

(defn get-bake-day [id]
  (first
   (sql/query db/datasource
              [(str "SELECT * FROM " (name table)
                    " WHERE id=" id)]
              db/opts)))

(def day1
  {:date "2026-01-01"
   :name "test day 1"})

(def day2
  {:date "2026-02-01"
   :name "test day 2"})
