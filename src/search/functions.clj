(ns search.functions
  "ns dedicado a la busquda de funciones, revisa donde se definen y donde se usan"
(:use [search core read]
      [clojure.contrib.string :only [join]]
      fm.namespaces))

(defn reggex-fn
  "crea el reggex para buscar en donde se usa una funcion"
  [fname]
  (re-pattern (str "(\\(|\\/)" (safe-pattern fname) "[ \n]+")))

(defn define-x?
  "Revisa si esta definido x en el filename
  por (defn x, (defmacro x, (def x etc"
  [x filename]
  (let [all-functions (source-functions filename)]
    (list 
      (take-first #(not (nil? %))
                  (map #(re-find 
                          (re-pattern (str "(\\(defn|\\(def|\\(defmacro)[ \\n]+" (safe-pattern x) "[ \\n]+")) %) 
                       all-functions))
      filename)))

(defn printdef
  "imprime el re-group de define-x de manera entendible"
  [re-group file]
  (case (second re-group)
    "(defn" (println (str "La funcion " (re-find #"[^ ]+$" (first re-group)) " esta definida en " file))
    "(demacro" (println (str "El macro " (re-find #"[^ ]+$" (first re-group)) " esta definido en " file))
    "(def" (println (str  (re-find #"[^ ]+$" (first re-group)) " esta definido en " file))))
 
(defn search-defn
  "busca en los archivos clj del path donde esta definida una funcion"
  [func]
  (let [searchs (map #(define-x? func %) *filesclj*)] ;juntamos todos los resultados de busqueda
    (loop [s searchs]
      (if (not (nil? s))
        (do
          (if (not (nil? (first (first s))))  ;(first s) es algo como '(["(defn search-fn " "(defn"], some/file.clj) sii la fn esta definida en some/file.clj
            (apply printdef (first s)))  ;para los files que tengan definida la fn aplica printdef
          (recur (next s)))))))

(defn use-fn?
  "Busca si se usa una funcion en un ns. true si esta, false si no.
  -f lista las  funciones donde se usa"
  ([function namespace]
   (re-find (reggex-fn function) (join "\n\n" (source-functions namespace))))
  ([function namespace opt]
   (case opt
     "-f" [namespace (map #(second (re-seq #"[^ \n]+" %)) (filter #(re-find (reggex-fn function) %) (source-functions namespace)))]
     (use-fn? function namespace))))


