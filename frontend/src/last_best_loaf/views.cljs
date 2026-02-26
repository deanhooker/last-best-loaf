(ns last-best-loaf.views
  (:require
   [reagent.core :as r]
   [re-frame.core :as rf]))

(defn nav-link [content route active?]
  [:button
   {:style {:background "none"
            :border "none"
            :padding "0.25rem 0.5rem"
            :font-size "1rem"
            :cursor "pointer"
            ;; :display "flex"
            ;; :align-items "center"
            :text-decoration (when active? "underline")}
    :on-click #(rf/dispatch [:navigate! route])}
   content])

(defn cart-icon []
  [:svg {:xmlns "http://www.w3.org/2000/svg"
         :width 20
         :height 20
         :viewBox "0 0 24 24"
         :fill "none"
         :stroke "currentColor"
         :stroke-width 2
         :stroke-linecap "round"
         :stroke-linejoin "round"}
   [:circle {:cx 9 :cy 21 :r 1}]
   [:circle {:cx 20 :cy 21 :r 1}]
   [:path {:d "M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"}]])


(defn nav-bar []
  (let [current @(rf/subscribe [:route-name])
        cart-count @(rf/subscribe [:cart/count])]
    [:div {:style {:border-bottom "1px solid #eee"
                   :padding "0.75rem 1rem"
                   :margin-bottom "1.5rem"}}

     [:div {:style {:max-width "960px"
                    :margin "0 auto"
                    :display "flex"
                    :align-items "center"
                    :justify-content "space-between"}}

      ;; Brand
      [:div
       [:button
        {:style {:background "none"
                 :border "none"
                 :font-size "1.25rem"
                 :font-weight "bold"
                 :cursor "pointer"}
         :on-click #(rf/dispatch [:navigate! :menu])}
        "🥯 The Last Best Loaf Bakery"]]

      ;; Links
      [:div
       [nav-link "Menu" :menu (= current :menu)]
       [nav-link "About" :about (= current :about)]
       [nav-link "Contact" :contact (= current :contact)]
       [nav-link
        [:<>
         [cart-icon]
         (when (pos? cart-count)
           [:span {:style {:font-size "0.85rem"
                           :margin-left "0.25rem"}}
            cart-count])]
        :cart
        (= current :cart)]
       ]]]))


(defn error-msg [errors k]
  (when-let [msg (get errors k)]
    [:div {:style {:color "crimson"
                   :font-size "0.85rem"}}
     msg]))

(defn field [{:keys [label input error]}]
  [:div {:style {:margin-bottom "1rem"}}
   [:div {:style {:font-weight "bold"}} label]
   input
   [:div {:style {:min-height "1.2em"
                  :color "crimson"
                  :font-size "0.85rem"}}
    error]])

(defn page [{:keys [title body footer]}]
  [:div {:style {:max-width "640px"
                 :margin "2rem auto"
                 :padding "0 1rem"
                 :font-family "system-ui, -apple-system, sans-serif"}}
   [:h1 {:style {:margin-bottom "1.5rem"}} title]
   body
   (when footer
     [:div {:style {:margin-top "2rem"
                    :border-top "1px solid #eee"
                    :padding-top "1rem"}}
      footer])])


(defn menu-item [{:keys [id name price description]}]
  [:div {:style {:border "1px solid #eee"
                 :border-radius "8px"
                 :padding "1rem"
                 :margin-bottom "1rem"}}
   [:div {:style {:display "flex"
                  :justify-content "space-between"}}
    [:h3 {:style {:margin 0}} name]]
   [:p {:style {:color "#555"
                :margin "0.5rem 0 1rem"}}
    description]
   [:strong (str "$" price)]
   [:div
    [:button
     {:style {:padding "0.5rem 0.75rem"
              :border-radius "6px"
              :background "#222"
              :color "white"
              :border "none"}
      :on-click #(rf/dispatch [:cart/add id])}
     "Add to cart"]]])

(defn menu []
  (r/with-let [_ (rf/dispatch [:load-bake-days])]
    (let [items @(rf/subscribe [:catalog/items])
                     bake-days @(rf/subscribe [:bake-days])]
           [:div
            [:h2 "Menu"]
            [:div 
             [:p [:strong "Community Loaves:"]]
             [:p "We believe good bread should be accessible."]
             [:p "Last Best Loaf Bakery is built on small batches, long fermentation, and care for our neighbors. 
    For every full-price loaf purchased, one Community Loaf becomes available at a reduced price - offered on the honor system.
    Good bread should be rooted in place and shared at the table. This is our way of doing both. 
    "]]
            (for [item items]
              ^{:key (:id item)}
              [menu-item item])
            [:div (str bake-days)]
            [:button
             {:on-click #(rf/dispatch [:navigate! :cart])}
             "View Cart"]])))

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
        @(rf/subscribe [:customer])

        errors @(rf/subscribe [:errors])
        valid? @(rf/subscribe [:valid?])]
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
        [:div {:style {:display "flex"}}
         [field
          {:input [:input
                   {:type "date"
                    :value pickup-date
                    :on-change #(rf/dispatch
                                 [:checkout/set :pickup-date (.. % -target -value)])}]

           :error (:pickup-date errors)}]
         [field
          {:input
           [:input
            {:placeholder "Time window (e.g. 9–11am)"
             :value pickup-window
             :on-change #(rf/dispatch
                          [:checkout/set :pickup-window (.. % -target -value)])}]
           :error (:pickup-window errors)}]]])

     ;; Delivery
     (when (= fulfillment :delivery)
       [:div
        [:h4 "Delivery address"]
        [:textarea
         {:placeholder "Street address"
          :value delivery-address
          :on-change #(rf/dispatch
                       [:checkout/set :delivery-address (.. % -target -value)])}]
        [error-msg errors :delivery-address]
        [:p {:style {:font-size "0.9rem"
                     :color "#666"}}
         "Delivery available within 3 miles"]])

     ;; Customer
     [:div
      [:h3 "Your details"]
      [:div {:style {:display "flex"}}
       [field
        {:input
         [:input
          {:placeholder "Name"
           :value name
           :on-change #(rf/dispatch
                        [:customer/set :name (.. % -target -value)])}]

         :error [error-msg errors :name]}]
       [field
        {:input
         [:input
          {:placeholder "Email"
           :value email
           :on-change #(rf/dispatch
                        [:customer/set :email (.. % -target -value)])}]

         :error [error-msg errors :email]
         }]
       [field
        {:input
         [:input
          {:placeholder "Phone"
           :value phone
           :on-change #(rf/dispatch
                        [:customer/set :phone (.. % -target -value)])}]

         :error [error-msg errors :phone]}]]]

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
        :on-click #(rf/dispatch [:validate-checkout])
        :disabled (not valid?)}
       "Place Order"]]]))

(defn about []
  [:div "Coming soon!!"])

(defn contact-info []
  [:div {:style {:flex "1 1 380px"
                 :line-height "1.6"}}

   [:p "The Last Best Loaf Bakery"]
   [:p "3100 Technology Blvd W, Apt 301"]
   [:p "Bozeman, MT 59718 USA"]
   [:p "Questions? Call us or send us a DM on Instagram or Facebook"]
   [:p
    [:strong "Phone: "] "(610) 730-1579"]
   [:p
    [:strong "Instagram: "] "@lastbestloafbakery"]
   [:p
    [:strong "Email: "]
    [:a {:href "mailto:thelastbestloafbakery@gmail.com"}
     "thelastbestloafbakery@gmail.com"]]])


(defn contact []
  [:div {:style {:padding "3rem 1rem"}}

   [:div {:style {:max-width "1100px"
                  :margin "0 auto"
                  :display "flex"
                  :gap "3rem"
                  :align-items "flex-start"
                  :flex-wrap "wrap"}}

    [contact-info]]])

(defn footer []
  [:footer
   {:style {:background "#8b3a0e"
            :color "white"
            :padding "2rem 1rem"
            :margin-top "4rem"}}

   [:div
    {:style {:max-width "1100px"
             :margin "0 auto"
             :display "flex"
             :flex-wrap "wrap"
             :gap "2rem"
             :font-size "0.9rem"}}

    ;; Left: address
    [:div {:style {:flex "1 1 240px"}}
     [:div {:style {:font-weight "bold" :margin-bottom "0.5rem"}}
      "The Last Best Loaf Bakery"]
     [:div "3100 Technology Blvd W, Apt 301"]
     [:div "Bozeman, MT"]]

    ;; Middle: contact
    [:div {:style {:flex "1 1 240px"}}
     [:div {:style {:font-weight "bold" :margin-bottom "0.5rem"}}
      "Contact"]
     [:div "Phone: (610) 730-1579"]
     [:div
      [:a {:href "mailto:thelastbestbakery@gmail.com"
           :style {:color "white"
                   :text-decoration "underline"}}
       "thelastbestbakery@gmail.com"]]]

    ;; Right: social
    [:div {:style {:flex "1 1 240px"}}
     [:div {:style {:font-weight "bold" :margin-bottom "0.5rem"}}
      "Follow"]
     [:div "Instagram: @lastbestbakery"]
     [:div "Facebook: The Last Best Loaf Bakery"]]]])

(defn root []
  (let [route @(rf/subscribe [:route-name])]
    [:div
     [nav-bar]
     [:div {:style {:max-width "640px"
                    :margin "0 auto"
                    :padding "0 1rem"}}
      (case route
        :about [about]
        :menu [menu]
        :cart [cart]
        :checkout [checkout]
        :contact [contact]
        [:div "Loading…"])]
     [footer]]
    ))
