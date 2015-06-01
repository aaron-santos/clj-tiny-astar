(ns clj-tiny-astar.path
  #+clj
  (:use [clj-tuple])
  (:require [clj-tiny-astar.util :as util]
            #+clj
            [clojure.data.priority-map :refer [priority-map]]
            #+cljs
            [tailrecursion.priority-map :refer [priority-map]])
  #+clj
  (:import [java.lang Math]))

#+clj
(defn abs [n]
  (Math/abs n))

#+cljs
(defn abs [n]
  (.abs js/Math n))

#+cljs
(defn tuple [& v]
  (apply vector v))

(defn manhattan-dist
 [[x0 y0] [x1 y1]]
  (+ (abs ^int(- x1 x0)) (abs ^int(- y1 y0))))

(defn initial-open-set
  [start]
  (priority-map start 0))

(defrecord Square [loc g h parent])
(defn f [square] (+ (:g square) (:h square)))

(defn a*-adj-squares
  [[w h] dist pred closed loc goal]
  (->> (util/adj w h loc)
       (filter #(and (pred %)
                     (not (closed %))))
       (map (fn [p]
              (->Square p
                        (if (util/diagonal? loc p) 1.4 1)
                        (dist p goal)
                        loc)))))

(defn a*-collect-path
  [square sq-map goal]
  (loop [acc [(:loc square)]
         sq square]
    (if-let [parent (sq-map (:parent sq))]
      (let [loc (:loc parent)]
        (if (not= goal loc)
          (recur (conj acc loc) parent)
          (conj acc loc)))
      acc)))



(defn a*
"find a path from a to b given the predicate function 'pred'.
pred is a function of point -> bool"
([bounds dist pred a b]
  (loop [open (initial-open-set b)
          squares {b (->Square b 0 0 nil)}
          closed #{}]
     (if-not (empty? open)
       (let [curr (ffirst open)
             curr-square (squares curr)]      
         (if (= curr a)
           (a*-collect-path curr-square squares b)
           (let [adj (a*-adj-squares bounds dist pred closed curr a)
                 reducer (fn [[squares open :as data] a]
                           (let [loc (:loc a)]
                             (cond
                              (not (open loc))
                              (tuple (assoc squares loc a)
                                     (assoc open loc (f a)))
                              (< (:g a) (:g (squares loc)))
                              (tuple (assoc squares loc a)
                                     open)
                              :else data)))
                 [new-squares new-open] (reduce reducer (tuple squares open) adj)]
             (recur (dissoc new-open curr) new-squares (conj closed curr))))))))
([bounds pred a b]
   (a* bounds manhattan-dist pred a b)))





