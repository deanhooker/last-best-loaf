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
