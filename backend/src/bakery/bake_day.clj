(ns bakery.bake-day
  (:require
   [bakery.db :as db]
   [bakery.schema.bake-day :as schema]
   [malli.core :as m]
   [next.jdbc.sql :as sql])
  (:import
   [java.time LocalDate]))

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
