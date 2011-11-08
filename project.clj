(defproject search "1.0.0-SNAPSHOT"
  :main search
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [fm.namespaces/fm.namespaces "1.0.0-SNAPSHOT"]
                 [cj "1.0.0-SNAPSHOT"]]
  :dev-dependencies[[org.clojars.autre/lein-vimclojure "1.0.0"]]
   :repositories {"fractal" {:url "http://67.205.67.146:8099/archiva/repository/fractal"}
                  "snapshots" {:url "http://67.205.67.146:8099/archiva/repository/snapshots"}})
