(ns monkey.oci.lb.core
  "Core library for accessing the Load balancer api"
  (:require [martian.core :as mc]
            [monkey.oci.common
             [martian :as cm]
             [pagination :as p]
             [utils :as cu]]
            [schema.core :as s]))

(def version "20170115")
(def json ["application/json"])

(def Id s/Str)

;; Look out: endpoint is case sensitive!!
(def lb-base-path ["/loadBalancers/"])
(def lb-path (conj lb-base-path :load-balancer-id))

(def lb-path-schema {:load-balancer-id Id})
(def lb-query-schema {:compartmentId Id})

(defn api-route [opts]
  (assoc opts :produces json))

(def lb-routes
  [(p/paged-route
    (api-route
     {:route-name :list-load-balancers
      :method :get
      :path-parts lb-base-path
      :query-schema lb-query-schema}))

   (api-route
    {:route-name :get-load-balancer
     :method :get
     :path-parts lb-path
     :path-schema lb-path-schema})])

(def bs-base-path (conj lb-path "/backendSets/"))
(def bs-path (conj bs-base-path :backend-set-name))
(def bs-path-schema (assoc lb-path-schema
                           :backend-set-name s/Str))

(def backendset-routes
  [(p/paged-route
    (api-route
     {:route-name :list-backend-sets
      :method :get
      :path-parts bs-base-path
      :path-schema lb-path-schema}))

   (api-route
    {:route-name :get-backend-set
     :method :get
     :path-parts bs-path
     :path-schema bs-path-schema})])

(def be-base-path (conj bs-path "/backends/"))
(def be-path (conj be-base-path :backend-name))
(def be-path-schema (assoc bs-path-schema
                           :backend-name s/Str))

(s/defschema UpdateBackend
  {(s/optional-key :backup) s/Bool
   (s/optional-key :drain) s/Bool
   (s/optional-key :offline) s/Bool
   (s/optional-key :maxConnections) s/Int
   (s/optional-key :weight) s/Int})

(s/defschema CreateBackend
  (assoc UpdateBackend
         :ipAddress s/Str
         :port s/Int))

(def backend-routes
  [(p/paged-route
    (api-route
     {:route-name :list-backends
      :method :get
      :path-parts be-base-path
      :path-schema bs-path-schema}))

   (api-route
    {:route-name :get-backend
     :method :get
     :path-parts be-path
     :path-schema be-path-schema})

   (api-route
    {:route-name :create-backend
     :method :post
     :path-parts be-base-path
     :path-schema bs-path-schema
     :body-schema {:backend CreateBackend}
     :consumes json})

   (api-route
    {:route-name :update-backend
     :method :put
     :path-parts be-path
     :path-schema be-path-schema
     :body-schema {:backend UpdateBackend}
     :consumes json})

   (api-route
    {:route-name :delete-backend
     :method :delete
     :path-parts be-path
     :path-schema be-path-schema})])

(def routes
  (concat lb-routes
          backendset-routes
          backend-routes))

(defn make-client [conf]
  (cm/make-context conf (comp (partial format (str "https://iaas.%s.oraclecloud.com/" version)) :region) routes))

(cu/define-endpoints *ns* routes mc/response-for)
