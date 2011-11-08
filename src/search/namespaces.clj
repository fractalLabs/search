(ns search.namespaces
  (:use [search core read]
        fm.namespaces 
        fm.namespaces.core
        [clojure.contrib.string :only [substring?]]))

(defn is-ns? [arg] (re-find #"." arg))

(defn use-ns? [arg source-ns]
  (let [uses (re-find #"\(:use[\w\W]+?\)" (source-of "search.namespaces"))
        requires (re-find #"\(:require[\w\W]+?\)" (source-of "search.namespaces"))
        oneword (substring? arg (str uses requires))
        fpart (re-find #"^[^.]+" arg)
        lpart (re-find #"[^.]+$" arg)
        as-vec (re-find (re-pattern (str "\\[" (safe-pattern fpart) "[\\w\\W]+" (safe-pattern lpart) "[\\w\\W]+?\\]")) (str uses requires))]
    (or oneword as-vec)))
