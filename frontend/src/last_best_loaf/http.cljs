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

(defn fetch-json
  "Fetch JSON from the given URL, returns a promise that resolves to Clojure data."
  [url]
  (-> (js/fetch url)
      (.then (fn [res]
               (if (.-ok res)
                 (.json res)
                 (js/Promise.reject (str "HTTP error " (.-status res))))))
      (.then #(js->clj % :keywordize-keys true))))
