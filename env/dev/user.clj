(ns user
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [monkey.oci.common.utils :as u]
            [monkey.oci.lb.core :as lb]))

(defn load-config []
  (with-open [r (-> (io/resource "config.edn")
                    (io/reader)
                    (java.io.PushbackReader.))]
    (-> (edn/read r)
        (update :private-key u/load-privkey))))
