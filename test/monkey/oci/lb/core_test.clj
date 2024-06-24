(ns monkey.oci.lb.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [martian.test :as mt]
            [monkey.oci.common.utils :as u]
            [monkey.oci.lb.core :as sut]))

(def fake-conf {:tenancy-ocid "test-tenancy"
                :user-ocid "test-user"
                :key-fingerprint "test-fingerprint"
                :private-key (u/generate-key)})

(deftest make-client
  (testing "creates martian context"
    (is (some? (:handlers (sut/make-client fake-conf))))))

(deftest lb-routes
  (testing "can list load balancers"
    (let [c (-> (sut/make-client fake-conf)
                (mt/respond-with {:list-load-balancers (constantly {:status 200})}))]
      (is (= 200 (:status @(sut/list-load-balancers c)))))))
