(ns last-best-loaf.db)

(def default-db
  {:route nil

   :products []
   
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

   :events nil

   :ui
   {:errors {}
    :submitting? false}
   })
