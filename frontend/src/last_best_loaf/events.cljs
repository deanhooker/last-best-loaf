(ns last-best-loaf.events
  (:require
   [re-frame.core :as rf]
   [reitit.frontend.easy :as rfe]
   [last-best-loaf.db :as db]
   [last-best-loaf.http :refer [fetch-edn fetch-json]]
   [last-best-loaf.validation :as v]))

(rf/reg-event-db
 :initialize-db
 (fn [_ _]
   db/default-db))

(rf/reg-fx
 :navigate
 (fn [{:keys [name path-params]}]
   (rfe/push-state name path-params)))

(rf/reg-event-fx
 :navigate!
 (fn [_ [_ route]]
   {:fx [[:navigate route]]}))

(rf/reg-event-fx
 :navigated
 (fn [{:keys [db]} [_ match]]
   (let [route-name (get-in match [:data :name])
         params (:path-params match)]
     (case route-name
       :event
       {:db (assoc db :route match)
        :dispatch [:load-event (:event-id params)]}

       {:db (assoc db :route match)}))))

;; TODO: Add validation
;; - Check new item allowed in cart
;; - Send warning to user if not
;; - Allow user to reset cart
(rf/reg-event-db
 :cart/add
 (fn [db [_ item]]
   (let [event-id (:bake_day_id item)
         item-id (:id item)
         current-cart-event-id (:event-id (:cart db))]
     (if (or (nil? current-cart-event-id)
             (= event-id current-cart-event-id))
       (-> db
           (assoc-in [:cart :event-id] event-id)
           (update-in [:cart :items item-id]
                      (fn [current-item]
                        (if current-item
                          (update current-item :qty inc)
                          {:item item :qty 1}))))
       (println "Error: Can't have multiple events in cart!")))))

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

(rf/reg-event-db
 :validate-checkout
 (fn [db _]
   (let [errors (v/validate
                 {:checkout (:checkout db)
                  :customer (:customer db)})]
     (assoc-in db [:ui :errors] errors))))

;; Fetch Products
(rf/reg-event-fx
 :load-products
 (fn [_ _]
   {:fetch-products {:on-success #(rf/dispatch [:set-products %])
                     :on-failure #(js/console.error "Failed to fetch products:" %)}}))

(rf/reg-fx
 :fetch-products
 (fn [{:keys [on-success on-failure]}]
   (-> (fetch-json "http://localhost:3000/api/products")
       (.then on-success)
       (.catch on-failure))))

(rf/reg-event-db
 :set-products
 (fn [db [_ response]]
   (assoc db :products response)))

;; Fetch Bake Days
(rf/reg-event-fx
 :load-events
 (fn [_ _]
   {:fetch-events {:on-success #(rf/dispatch [:set-events %])
                   :on-failure #(js/console.error "Failed to fetch events: " %)}}))

(rf/reg-fx
 :fetch-events
 (fn [{:keys [on-success on-failure]}]
   (-> (fetch-json "http://localhost:3000/api/bake-days")
       (.then on-success)
       (.catch on-failure))))

(rf/reg-event-db
 :set-events
 (fn [db [_ response]]
   (assoc db :events-list response)))

;; Fetch Event
(rf/reg-event-fx
 :load-event
 (fn [_ [_ event-id]]
   {:fetch-event
    {:event-id event-id
     :on-success #(rf/dispatch [:set-event event-id %])
     :on-failure #(js/console.error "Failed to fetch event: " %)}}))

(rf/reg-fx
 :fetch-event
 (fn [{:keys [event-id on-success on-failure]}]
   (-> (fetch-json (str "http://localhost:3000/api/event/" event-id))
       (.then on-success)
       (.catch on-failure))))

(rf/reg-event-db
 :set-event
 (fn [db [_ event-id response]]
   (assoc-in db [:events event-id] response)))
