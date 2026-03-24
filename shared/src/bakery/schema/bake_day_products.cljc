(ns bakery.schema.bake-day-products
  (:require
   [malli.core :as m]))

(def BakeDayProductInput
  [:map
   [:bake_day_id int?]
   [:product_id int?]
   [:inventory int?]
   [:sold_count {:optional true} int?]
   [:available {:optional true} boolean?]
   [:price_override {:optional true} int?]])

(def BakeDayProduct
  [:map
   [:id int?]
   [:bake_day_id int?]
   [:product_id int?]
   [:inventory int?]
   [:sold_count {:optional true} int?]
   [:available {:optional true} boolean?]
   [:price_override {:optional true} int?]])
