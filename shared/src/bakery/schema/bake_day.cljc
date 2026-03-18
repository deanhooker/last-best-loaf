(ns bakery.schema.bake-day
  (:require
   [malli.core :as m]))

(def iso-date?
  [:and
   string?
   [:fn #(re-matches #"\d{4}-\d{2}-\d{2}" %)]])

(def BakeDayInput
  [:map
   [:date iso-date?]
   [:name {:optional true} string?]])

(def BakeDay
  [:map
   [:id int?]
   [:date iso-date?]
   [:name {:optional true} string?]])
