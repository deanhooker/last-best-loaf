(ns bakery.schema.product
  (:require
   [malli.core :as m]))

(def ProductInput
  [:map
   [:name string?]
   [:price pos-int?]
   [:description {:optional true} string?]])

(def Product
  [:map
   [:id int?]
   [:name string?]
   [:price pos-int?]
   [:description {:optional true} string?]
   [:active boolean?]])
