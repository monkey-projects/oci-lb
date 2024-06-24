(ns monkey.oci.lb.core
  "Core library for accessing the Load balancer api"
  (:require [martian.core :as mc]
            [monkey.oci.common
             [martian :as cm]
             [utils :as cu]]))

(def version "20170115")
(def json ["application/json"])

(def lb-routes
  [{:route-name :list-load-balancers
    :method :get
    :path-parts ["/loadbalancers"]
    :produces json}])

(def routes
  lb-routes)

(defn make-client [conf]
  (cm/make-context conf (comp (partial format (str "https://iaas.%s.oraclecloud.com/" version)) :region) routes))

(cu/define-endpoints *ns* routes mc/response-for)
