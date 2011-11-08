(ns leiningen.search
  "Busca en donde se define y usa un ns o funcion"
  (:require [search :as s]))


(defn search
  "Busca en donde se define y usa un ns o funcion
  -f y regresa en que funciones usa c/archivo a la funcion"
  ([arg opt]
   (s/search arg opt))
  ([arg]
   (s/search arg)))
