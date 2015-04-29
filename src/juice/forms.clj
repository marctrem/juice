(ns juice.forms
  (:require [clojure.set :as set])
  (:gen-class))

(defn- validate-fields
  [form fields]

  (doall
    (map
      (fn [[k v]] (when-not (nil? v)
                    (when (contains? form k)
                      (when-not (v (form k))
                        (throw (Exception. (str "Field '" k "' validation failed.")))))))
      fields)))

(defn validate
  "Validates a form with the given specifications.
  The form must be a map.
  The spec is a map that can contain maps under the :required and :optional keys.
  The later ones must have a field name as a key and can have a validation function or nil."

  [form spec]

  (let [spec (merge {:required {} :optional {}} spec)
        form-keys (-> form keys set)
        required-keys (-> spec :required keys set)
        optional-keys (-> spec :optional keys set)
        authorized-keys (set/union required-keys optional-keys)

        missing-keys (set/difference required-keys form-keys)
        unknown-keys (set/difference form-keys authorized-keys)]

    (cond
      (-> unknown-keys empty? not) (throw (Exception. "Unknown keys"))
      (-> missing-keys empty? not) (throw (Exception. "Missing keys"))
      :else (do
              (validate-fields form (spec :required))
              (validate-fields form (spec :optional))
              true))))
