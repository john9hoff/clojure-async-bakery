(ns clojure-async-bakery.core
  (require [clojure.core.async :as async])
  (:gen-class))

(def cust-line (async/chan))
(def worker-line (async/chan))
(def pairs (async/chan))
(def results (async/chan))

(defn initialize-universe
  [num-cust num-workers]
  ; put all workers in worker channel right away
  (async/go
    (map (partial async/>! worker-line) (range 0 10))
    (println "HELLO"))
    (println "outside")
  ; put all customers in customer channel after sleeping
)

(defn -main
  [num-cust num-workers]
  (initialize-universe num-cust num-workers)
  (println "Main"))
