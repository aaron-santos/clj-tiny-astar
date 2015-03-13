(defproject clj-tiny-astar "0.1.1-SNAPSHOT"
  :description "a mini a* pather for 2d binary grids"
  :url "http://github.com/danstone/clj-tiny-astar"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clj-tuple "0.1.2"]
                 [org.clojure/data.priority-map "0.0.5"]
                 [tailrecursion/cljs-priority-map "1.1.0"]]
  :source-paths
  ["src/clj_tiny_astar"
   "target/generated-src/clj"
   "target/generated-src/cljs"]


  :cljx
  {:builds [{:source-paths ["src/clj_tiny_astar"]
             :output-path "target/generated-src/clj"
             :rules :clj}
            {:source-paths ["src/clj_tiny_astar"]
             :output-path "target/generated-src/cljs"
             :rules :cljs}]}

  :prep-tasks [["cljx" "once"]]

   :cljsbuild
  {:builds [{:id "dev"
             :source-paths ["src" "dev"]
             :compiler {:output-to "resources/public/clj-tiny-astar.js"
                        :output-dir "resources/public/out"
                        :optimizations :none
                        :source-map true}}]}

  :profiles {
    :dev {:dependencies
          [#_[org.clojure/clojurescript "0.0-3030"]]

          :plugins
          [[com.keminglabs/cljx "0.6.0"]
           #_[com.cemerick/piggieback "0.1.5-SNAPSHOT"]
           [lein-cljsbuild "1.0.4"]]}})


