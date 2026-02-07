(ns last-best-loaf.db)

(def default-db
  {:route nil

   :catalog
   {:items
    {:classic-loaf {:id :classic-loaf
                    :name "Classic Loaf"
                    :price 7.50
                    :description "The last best loaf you'll ever have."}

     :plain-bagels {:id :plain-bagels
                    :name "Plain Bagels"
                    :price 10
                    :description "Plain Bagels - 11/10"}

     :rosemary-bagels {:id :rosemary-bagels
                       :name "Rosemary Sea-salt Bagels"
                       :price 11
                       :description "YUM!"}}}

   :cart {:items {}}

   :checkout
   {:fulfillment :pickup   ;; :pickup | :delivery
    :pickup-date ""
    :pickup-window ""
    :delivery-address ""
    :notes ""}

   :customer
   {:name ""
    :email ""
    :phone ""}

   :ui
   {:errors {}
    :submitting? false}
   })
