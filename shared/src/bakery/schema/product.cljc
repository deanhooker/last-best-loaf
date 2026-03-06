(ns bakery.schema.product
  (:require
   [malli.core :as m]))

(def ProductInput
  [:map
   [:name string?]
   [:price_cents pos-int?]
   [:description {:optional true} string?]])

(def Product
  [:map
   [:id int?]
   [:name string?]
   [:price_cents pos-int?]
   [:description {:optional true} string?]
   [:active boolean?]])
