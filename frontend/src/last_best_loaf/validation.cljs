(ns last-best-loaf.validation)

(defn required? [s]
  (and (string? s) (not (clojure.string/blank? s))))

(defn validate [{:keys [checkout customer]}]
  (let [{:keys [fulfillment pickup-date pickup-window delivery-address]}
        checkout

        {:keys [name email phone]}
        customer

        errors {}]

    (cond-> errors
      (not (required? name))
      (assoc :name "Name is required")

      (not (required? email))
      (assoc :email "Email is required")

      (not (required? phone))
      (assoc :phone "Phone number is required")

      (and (= fulfillment :pickup)
           (not (required? pickup-date)))
      (assoc :pickup-date "Pickup date is required")

      (and (= fulfillment :pickup)
           (not (required? pickup-window)))
      (assoc :pickup-window "Pickup time window is required")

      (and (= fulfillment :delivery)
           (not (required? delivery-address)))
      (assoc :delivery-address "Delivery address is required"))))
