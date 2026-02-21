(ns bakery.db
  (:require
   [next.jdbc :as jdbc])
  (:import
   [com.zaxxer.hikari HikariDataSource]))

(defonce ds
  (doto (HikariDataSource.)
    (.setJdbcUrl "jdbc:postgresql://localhost:5432/bakery")
    (.setUsername "bakery")
    (.setPassword "bakery")))

(defn test-connection []
  (jdbc/execute! ds ["select 1"]))
