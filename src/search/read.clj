(ns search.read
  "El encargado de leer los archivos y obtener las funciones y ns"
 (:use 
   [fm.namespaces core]))

;soy un comentario inutil


(defn read-file
  "lista de cosas definidas en un archivo"
  [name] 
  (eval (read-string (str "'(" (slurp name) ")"))))


(defn functions
  "lista de los nomres de todo lo definido en un namespace" 
  [namespace]
  (map #(str (second %)) (rest (read-file (ns->path namespace)))))

(defn source-functions
  "lista del codigo de las funciones de un archivo"
  [namespace]
  (map str (rest (read-file (ns->path namespace)))))

(defn source-of 
  "codigo de una funcion o namespace"
  ([namespace function]
   (let [content (read-file (ns->path namespace))]
     (str (first (filter #(= function (str (second %))) content)))))
  ([completename]
   (if (re-find #"/" completename)
     (apply source-of (re-seq #"[^/]+" completename))
     (str (first (read-file  (ns->path completename)))))))
