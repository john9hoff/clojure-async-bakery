(ns clojure-async-bakery.core
  (require [clojure.core.async :as async])
  (:gen-class))

;(def pairs (async/chan))
;(def results (async/chan))

; got this function from https://stackoverflow.com/questions/8939970/a-recursive-fibonacci-function-in-clojure
(defn fib [n]
  (take n
    (map first (iterate (fn [[a b]] [b (+ a b)]) [0 1]))))

(defn initialize-universe
  [num-cust num-workers]
  (let [worker-line (async/chan) customer-line (async/chan)]
    ; put all workers in worker channel right away
    (async/go
      (doseq [w (range num-workers)]
        (async/>! worker-line w)
        (println "worker got in line")))
    (async/go
      (doseq [c (range num-cust)]
        (async/>! customer-line [c (rand-int 43)])
        (println "customer got in line")))
  [customer-line worker-line])
  ; put all customers in customer channel after sleeping
)

(defn start
  [customer-line worker-line num-cust]
  (let [pairs (async/chan) results (async/chan)]
    (async/go
      (doseq [c (range num-cust)]
        (async/>! pairs
          [(async/<!! customer-line) (async/<!! worker-line)])
        (println "paired")))
    (async/pipeline
      10
      results
      (fn [x] (println x) x)
      #_(fn
        [x]
        (fib (second (first x))))
      pairs)))
; unfinished below
  ; (async/go
  ;   (doseq [c (range num-cust)]
  ;     (async/>! pairs
  ;       [(async/<! customer-line) (async/<! worker-line)])))

(defn -main
  [num-cust num-workers]
  (let [[customer-line worker-line]
        (initialize-universe num-cust num-workers)]
        (start customer-line worker-line num-cust)))
