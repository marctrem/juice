(ns juice.forms-test
  (:require [clojure.test :refer :all]
            [juice.forms :refer :all]))

(deftest validate-test

  (let [form {:name    "John Doe"
              :age     23
              :balance 12}]

    (testing "A normal form"
      (is (validate form {:required {:name string?
                                     :age  integer?}
                          :optional {:balance number?}}))

      (is (validate form {:required {:name string?
                                     :age  #(> % 18)}
                          :optional {:balance #(>= % 0)}}))

      (is (thrown? Exception
                   (validate form {})))

      (is (thrown? Exception
                   (validate form {:required {:name string?
                                              :age  #(> % 30)}
                                   :optional {:balance #(>= % 0)}})))))

  )
