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
      (is (= 200 (:status @(sut/list-load-balancers c {:compartment-id "test-compartment"}))))))

  (testing "can get load balancer by id"
    (let [c (-> (sut/make-client fake-conf)
                (mt/respond-with {:get-load-balancer (constantly {:status 200})}))]
      (is (= 200 (:status @(sut/get-load-balancer c {:load-balancer-id "test-id"})))))))

(deftest backendset-routes
  (testing "can list backendsets"
    (let [c (-> (sut/make-client fake-conf)
                (mt/respond-with {:list-backend-sets (constantly {:status 200})}))]
      (is (= 200 (:status @(sut/list-backend-sets c {:load-balancer-id "test-lb"}))))))

  (testing "can get backendset by name"
    (let [c (-> (sut/make-client fake-conf)
                (mt/respond-with {:get-backend-set (constantly {:status 200})}))]
      (is (= 200 (:status @(sut/get-backend-set c {:load-balancer-id "test-lb"
                                                   :backend-set-name "test-backendset"})))))))

(deftest backend-routes
  (testing "can list backends"
    (let [c (-> (sut/make-client fake-conf)
                (mt/respond-with {:list-backends (constantly {:status 200})}))]
      (is (= 200 (:status @(sut/list-backends c {:load-balancer-id "test-lb"
                                                 :backend-set-name "test-backendset"}))))))

  (testing "can get backend by name"
    (let [c (-> (sut/make-client fake-conf)
                (mt/respond-with {:get-backend (constantly {:status 200})}))]
      (is (= 200 (:status @(sut/get-backend c {:load-balancer-id "test-lb"
                                               :backend-set-name "test-backendset"
                                               :backend-name "test-backend"}))))))

  (testing "can create new backend"
    (let [c (-> (sut/make-client fake-conf)
                (mt/respond-with {:create-backend (constantly {:status 200})}))]
      (is (= 200 (:status @(sut/create-backend
                            c
                            {:load-balancer-id "test-lb"
                             :backend-set-name "test-backendset"
                             :backend
                             {:backup false
                              :drain false
                              :ip-address "10.2.13.4"
                              :max-connections 200
                              :offline false
                              :port 8080
                              :weight 1}}))))))

  (testing "can update backend"
    (let [c (-> (sut/make-client fake-conf)
                (mt/respond-with {:update-backend (constantly {:status 200})}))]
      (is (= 200 (:status @(sut/update-backend
                            c
                            {:load-balancer-id "test-lb"
                             :backend-set-name "test-backendset"
                             :backend-name "1.2.3.4:12342"
                             :backend
                             {:backup false
                              :drain false
                              :max-connections 200
                              :offline false
                              :weight 1}}))))))

  (testing "can delete backend"
    (let [c (-> (sut/make-client fake-conf)
                (mt/respond-with {:delete-backend (constantly {:status 204})}))]
      (is (= 204 (:status @(sut/delete-backend
                            c
                            {:load-balancer-id "test-lb"
                             :backend-set-name "test-backendset"
                             :backend-name "1.2.3.4:12342"}))))))

  (testing "can get backend health status"
    (let [c (-> (sut/make-client fake-conf)
                (mt/respond-with {:get-backend-health (constantly {:status 200})}))]
      (is (= 200 (:status @(sut/get-backend-health
                            c
                            {:load-balancer-id "test-lb"
                             :backend-set-name "test-backendset"
                             :backend-name "1.2.3.4:12342"})))))))
