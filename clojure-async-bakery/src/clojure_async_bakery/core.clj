(ns clojure-async-bakery.core
  (require [clojure.core.async :as async])
  (:gen-class))

;(def pairs (async/chan))
;(def results (async/chan))

(defn initialize-universe
  [num-cust num-workers]
  (let [worker-line (async/chan) customer-line (async/chan)]
    ; put all workers in worker channel right away
    (async/go
      (doseq [w (range num-workers)]
        (async/>! worker-line w)))
    (async/go
      (doseq [c (range num-cust)]
        (async/>! customer-line)))
  [customer-line worker-line])
  ; put all customers in customer channel after sleeping
)

(defn start
  [customer-line worker-line num-cust]
  (async/go
    (doseq [c (range num-cust)]
      (async/>! pairs
        [(async/<! customer-line) (async/<! worker-line)])))
; unfinished below
  (async/go
    (doseq [c (range num-cust)]
      (async/>! pairs
        [(async/<! customer-line) (async/<! worker-line)])))

(defn -main
  [num-cust num-workers]
  (let [[customer-line worker-line]
        (initialize-universe num-cust num-workers)]
        (start customer-line worker-line num-cust)))
