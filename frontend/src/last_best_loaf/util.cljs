(ns last-best-loaf.util)

(defn format-date [ts]
  (let [d (js/Date. ts)]
    (.toLocaleDateString d #js {:year "numeric"
                                :month "long"
                                :day "numeric"})))

(defn format-money [cents]
  (let [dollars (/ cents 100)
        formatter (js/Intl.NumberFormat.
                   "en-US" #js {:style "currency"
                                :currency "USD"})]
    (.format formatter dollars)))
