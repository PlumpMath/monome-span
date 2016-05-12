(ns monome-span.core
  (:gen-class)
  (:use overtone.osc))

(defn -main []
  (defonce duplex-in (osc-server 8082))
  (def duplex-out (osc-client "127.0.0.1" 8002))

  (defonce a-in (osc-server 32345))
  (defonce b-in (osc-server 42345))
  (def a-out (osc-client "127.0.0.1" 12345))
  (def b-out (osc-client "127.0.0.1" 22345))

  (osc-handle duplex-in "/duplex/grid/led/set"
              (fn [{[x y s] :args}]
                (if (< x 8)
                  (osc-send a-out "/duplex/grid/led/set" x y s)
                  (osc-send b-out "/duplex/grid/led/set" (- x 8) y s))))

  (osc-handle a-in "/duplex/grid/key"
              (fn [{[x y s] :args}]
                (osc-send duplex-out "/duplex/grid/key" x y s)))

  (osc-handle b-in "/duplex/grid/key"
              (fn [{[x y s] :args}]
                (osc-send duplex-out "/duplex/grid/key" (+ x 8) y s)))

  (println "Now spanning monomes!"))
