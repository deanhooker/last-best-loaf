(ns last-best-loaf.events
  (:require
   [re-frame.core :as rf]
   [reitit.frontend.easy :as rfe]
   [last-best-loaf.db :as db]))

(rf/reg-event-db
 :initialize-db
 (fn [_ _]
   db/default-db))

(rf/reg-fx
 :navigate
 (fn [route-name]
   (rfe/push-state route-name)))

(rf/reg-event-db
 :navigate
 (fn [db [_ match]]
   (assoc db :route match)))

(rf/reg-event-fx
 :navigate!
 (fn [_ [_ route-name]]
   {:fx [[:navigate route-name]]}))

(rf/reg-event-db
 :cart/add
 (fn [db [_ item-id]]
   (update-in db [:cart :items item-id]
              (fn [item]
                (if item
                  (update item :qty inc)
                  {:id item-id :qty 1})))))

(rf/reg-event-db
 :cart/remove
 (fn [db [_ item-id]]
   (update-in db [:cart :items item-id]
              (fn [item]
                (when (> (:qty item) 1)
                  (update item :qty dec))))))

(rf/reg-event-db
 :checkout/set
 (fn [db [_ k v]]
   (assoc-in db [:checkout k] v)))

(rf/reg-event-db
 :customer/set
 (fn [db [_ k v]]
   (assoc-in db [:customer k] v)))
