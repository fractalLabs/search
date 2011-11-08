(ns search.namespaces
  (:use [search core read]
        fm.namespaces 
        fm.namespaces.core
        [clojure.contrib.string :only [substring?]]))

(defn use-ns? [ns-target in-ns]
  (let [uses (re-find #"\(:use[\w\W]+?\)" (source-of in-ns))
        requires (re-find #"\(:require[\w\W]+?\)" (source-of in-ns))
        oneword (substring? ns-target (str uses requires))
        fpart (re-find #"^[^.]+" ns-target)
        lpart (re-find #"[^.]+$" ns-target)
        as-vec (re-find (re-pattern (str "\\[" (safe-pattern fpart) "[\\w\\W]+" (safe-pattern lpart) "[\\w\\W]+?\\]")) (str uses requires))]
    (or oneword as-vec)))
