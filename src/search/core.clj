(ns search.core
  (:use
    [clojure.contrib.string :only [replace-str]]))

(defn safe-pattern [string]
    (->> string
          (replace-str "\\" "\\\\")
          (replace-str "-" "\\-")
          (replace-str "." "\\.")
          (replace-str "(" "\\(")
          (replace-str "[" "\\[")
          (replace-str ")" "\\)")
          (replace-str "]" "\\]")
          (replace-str "?" "\\?")
          (replace-str "*" "\\*")
          (replace-str "+" "\\+")))

(defn take-first [pred coll]
    (first (filter pred coll)))
