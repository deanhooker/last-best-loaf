(ns last-best-loaf.subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 :current-route
 (fn [db _]
   (:route db)))

(rf/reg-sub
 :route-name
 :<- [:current-route]
 (fn [route _]
   (get-in route [:data :name])))

(rf/reg-sub
 :cart/items
 (fn [db _]
   (get-in db [:cart :items])))

(rf/reg-sub
 :cart/count
 :<- [:cart/items]
 (fn [items _]
   (reduce + (map :qty (vals items)))))

(rf/reg-sub
 :checkout
 (fn [db _]
   (:checkout db)))

(rf/reg-sub
 :customer
 (fn [db _]
   (:customer db)))

(rf/reg-sub
 :event
 (fn [db _]
   (let [event-id (get-in db [:route :path-params :event-id])]
     (get-in db [:events event-id]))))

(rf/reg-sub
 :events-list
 (fn [db _]
   (:events-list db)))

(rf/reg-sub
 :products
 (fn [db _]
   (:products db)))

(rf/reg-sub
 :errors
 (fn [db _]
   (get-in db [:ui :errors])))

(rf/reg-sub
 :valid?
 :<- [:errors]
 (fn [errors _]
   (empty? errors)))
