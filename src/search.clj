(ns search
  (:use [search core read functions namespaces]
        fm.namespaces
        fm.namespaces.core
        [clojure.contrib.string :only [substring?]]))

(defn def-ns
  "Imprime en que archivo se definio un ns"
  [namespace]
  (if-let [filename (take-first #(= (ns->path namespace) %) *filesclj*)]
    (if namespace
      (println (str "El archivo del ns " namespace " es: " filename))
      (println (str "No se encontro el archivo para el ns " namespace)))))

(defn uses-ns 
  "Imprime donde se usa un namespace"
  [namespace]
  (if-let [namespaces (filter #(use-ns? namespace %) *namespaces*)]
    (if (not (empty? namespaces))
      (do
        (println (str "Los archivos donde se usa el namespace " namespace " son:"))
        (loop [nss namespaces]
          (if (not (nil? nss))
            (do
              (println (first nss))
              (recur (next nss))))))
      (println (str "No se encontraron namespaces donde se use " namespace ".")))))

(defn def-fn
  "Imprime los archivos clj del path donde esta definida una funcion"
  [func]
  (let [searchs (map #(define-x? func %) *namespaces*)] ;juntamos todos los resultados de busqueda
    (loop [s searchs]
      (if (not (nil? s))
        (do
          (if (not (nil? (first (first s))))  ;(first s) es algo como '(["(defn search-fn " "(defn"], some/file.clj) sii la fn esta definida en some/file.clj
            (apply printdef (first s)))  ;para los files que tengan definida la fn aplica printdef
          (recur (next s)))))))

(defn printnl-coll
  "imprime los elementos de coll con salto de linea"
  [coll]
  (loop [coll coll]
    (if (not (nil? coll))
      (do
        (println (first coll))
        (recur (next coll))))))

(defn uses-fn
  "Imprime los archivos donde se use fn
  -f imprime el nombre de las funciones donde se use"
  ([func]
   (let [files (map ns->path (filter #(use-fn? func %) *namespaces*))]
     (if (empty? files)
       (println (str "No se encontro donde se use " func))
       (do
         (println (str func " se usa en los siguintes archivos"))
         (loop [fs files]
           (if (not (nil? fs))
             (do
               (println (first fs))
               (recur (next fs)))))))))
  ([func opt]
   (case opt
     "-f" (do
            (let [results (filter #(not (empty? (second %))) (map #(use-fn? func % "-f") *namespaces*))]
              (if (empty? results)
                (println (str func " no se usa en ningun namespace"))
                (loop [rs results]
                  (if (not (nil? rs))
                    (let [r (first rs)]
                      (println (str "El archivo " (ns->path (first r)) " usa la funcion " func " en"))
                      (printnl-coll (second r))
                      (recur (next rs))))))))
     (println (str opt " no es una opcion valida, pruebe -f")))))


(defn search
  "busca donde se define arg y en dond se usa"
  [arg & opt]
   (if (re-find #"\." arg)
     (do
       (def-ns arg)
       (uses-ns arg))
     (do
       (def-fn arg)
       (if (empty? opt)
         (uses-fn arg)
         (uses-fn arg (first opt))))))
