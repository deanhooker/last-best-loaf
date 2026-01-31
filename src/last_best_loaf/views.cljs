(ns last-best-loaf.views
  (:require
   [reagent.core :as r]
   [re-frame.core :as rf]))

(defn menu-item [{:keys [id name price description]}]
  [:div {:style {:border "1px solid #ddd"
                 :padding "1rem"
                 :margin-bottom "1rem"}}
   [:h3 name]
   [:p description]
   [:strong (str "$" price)]
   [:div
    [:button
     {:on-click #(rf/dispatch [:cart/add id])}
     "Add to cart"]]])

(defn menu []
  (let [items @(rf/subscribe [:catalog/items])]
    (println items)
    [:div
     [:h2 "Menu"]
     (for [item items]
       ^{:key (:id item)}
       [menu-item item])
     [:button
      {:on-click #(rf/dispatch [:navigate! :cart])}
      "View Cart"]
     ]))

(defn cart-summary []
  (let [count @(rf/subscribe [:cart/count])]
    [:div {:style {:position "fixed"
                   :top "1rem"
                   :right "1rem"}}
     [:strong "Cart: " count]]))

(defn cart []
  (let [items @(rf/subscribe [:cart/items])]
    [:div
     [:h2 "Your Cart"]

     (if (empty? items)
       [:p "Your cart is empty."]
       [:ul
        (for [[id {:keys [qty]}] items]
          ^{:key id}
          [:li (str (name id) " × " qty)])])

     [:div {:style {:margin-top "1rem"}}
      [:button
       {:on-click #(rf/dispatch [:navigate! :menu])}
       "Back to menu"]

      (when (seq items)
        [:button
         {:style {:margin-left "1rem"}
          :on-click #(rf/dispatch [:navigate! :checkout])}
         "Checkout"])]]))

(defn radio [label checked? on-click]
  [:label {:style {:margin-right "1rem"}}
   [:input {:type "radio"
            :checked checked?
            :on-change on-click}]
   " " label])

(defn checkout []
  (let [{:keys [fulfillment pickup-date pickup-window delivery-address notes]}
        @(rf/subscribe [:checkout])

        {:keys [name email phone]}
        @(rf/subscribe [:customer])]
    [:div
     [:h2 "Checkout"]

     ;; Fulfillment
     [:div
      [:h3 "Pickup or Delivery"]
      [radio "Pickup" (= fulfillment :pickup)
       #(rf/dispatch [:checkout/set :fulfillment :pickup])]
      [radio "Delivery" (= fulfillment :delivery)
       #(rf/dispatch [:checkout/set :fulfillment :delivery])]]

     ;; Pickup
     (when (= fulfillment :pickup)
       [:div
        [:h4 "Pickup details"]
        [:input
         {:type "date"
          :value pickup-date
          :on-change #(rf/dispatch
                       [:checkout/set :pickup-date (.. % -target -value)])}]
        [:input
         {:placeholder "Time window (e.g. 9–11am)"
          :value pickup-window
          :on-change #(rf/dispatch
                       [:checkout/set :pickup-window (.. % -target -value)])}]])

     ;; Delivery
     (when (= fulfillment :delivery)
       [:div
        [:h4 "Delivery address"]
        [:textarea
         {:placeholder "Street address"
          :value delivery-address
          :on-change #(rf/dispatch
                       [:checkout/set :delivery-address (.. % -target -value)])}]
        [:p {:style {:font-size "0.9rem"
                     :color "#666"}}
         "Delivery available within 3 miles"]])

     ;; Customer
     [:div
      [:h3 "Your details"]
      [:input
       {:placeholder "Name"
        :value name
        :on-change #(rf/dispatch
                     [:customer/set :name (.. % -target -value)])}]
      [:input
       {:placeholder "Email"
        :value email
        :on-change #(rf/dispatch
                     [:customer/set :email (.. % -target -value)])}]
      [:input
       {:placeholder "Phone"
        :value phone
        :on-change #(rf/dispatch
                     [:customer/set :phone (.. % -target -value)])}]]

     ;; Notes
     [:div
      [:h3 "Notes (optional)"]
      [:textarea
       {:value notes
        :on-change #(rf/dispatch
                     [:checkout/set :notes (.. % -target -value)])}]]

     ;; Actions
     [:div {:style {:margin-top "1.5rem"}}
      [:button
       {:on-click #(rf/dispatch [:navigate! :cart])}
       "Back to cart"]

      [:button
       {:style {:margin-left "1rem"}
        :on-click #(js/alert "Order submission coming next!")}
       "Place Order"]]]))


(defn root []
  (let [route @(rf/subscribe [:route-name])]
    [:div {:style {:max-width "600px"
                   :margin "2rem auto"
                   :font-family "sans-serif"}}
     [cart-summary]

     (case route
       :menu [menu]
       :cart [cart]
       :checkout [checkout]
       [:div "Loading…"])]))
