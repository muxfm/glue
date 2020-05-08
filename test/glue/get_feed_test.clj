(ns glue.get-feed-test
  (:require [clojure.test :refer [is deftest run-tests]]
            [glue.get-feed :as gf]))

(deftest hoax
  (is true))

(run-tests)
