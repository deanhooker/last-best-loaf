(ns last-best-loaf.util)

(defn format-date [ts]
  (let [d (js/Date. ts)]
    (.toLocaleDateString d #js {:year "numeric"
                                :month "long"
                                :day "numeric"})))
