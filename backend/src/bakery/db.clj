(ns bakery.db
  (:require
   [next.jdbc :as jdbc]
   [next.jdbc.result-set :as rs]
   [migratus.core :as migratus]))

(def db-spec
  {:dbtype "postgresql"
   :dbname "bakery"
   :user "bakery"
   :password "bakery"})

(def migration-config
  {:store :database
   :migration-dir "migrations"
   :db db-spec})

(def opts
  {:builder-fn rs/as-unqualified-lower-maps})

(defonce datasource
  (jdbc/get-datasource db-spec))

(defn migrate []
  (migratus/migrate migration-config))

(defn rollback []
  (migratus/rollback migration-config))

(defn test-connection []
  (jdbc/execute! datasource ["select 1"]))

(def bakes-stub
  [{:id #uuid "6bdf4f2b-b551-4d61-a3cd-b46b2a401407"
    :date "2026-02-28"
    :items
    [{:id #uuid "0b4920f6-b4c6-4835-bd5b-1d5b9e416886"
      :name "Classic Sourdough"
      :description "Yum yum yum"
      :price 12.50
      :quantity 20}
     {:id #uuid "b96b324f-27b2-4b15-8a72-e7f1507a201a"
      :name "Classic Bagels"
      :description "Yums yums yums"
      :price 10
      :quantity 100}]}])
