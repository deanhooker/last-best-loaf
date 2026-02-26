(ns last-best-loaf.http
  (:require
   [cljs.reader :as reader]))

(defn fetch-edn
  "Fetch EDN from the given URL, returns a promise that resolves to Clojure data."
  [url]
  (-> (js/fetch url)
      (.then (fn [res]
               (if (.-ok res)
                 (.text res)
                 (js/Promise.reject (str "HTTP error " (.-status res))))))
      (.then reader/read-string)))
