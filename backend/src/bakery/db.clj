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
