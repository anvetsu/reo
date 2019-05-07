(ns motto.lib.charts
  (:require [incanter.core :as ic]
            [incanter.charts :as charts]
            [motto.tab :as tab]))

(defn histogram
  ([xs options]
   (let [nbins (get options 'nbins 10)
         density (get options 'density false)
         title (get options 'title "Histogram")
         ss (str xs)
         x-label (get options 'x_label ss)
         y-label (get options 'y_label "Frequencies")
         legend (get options 'legend false)
         series-label (get options 'series_label ss)
         visible? (get options 'visible true)
         hg (charts/histogram xs :nbins nbins :density density
                              :title title :x-label x-label
                              :y-label y-label :legend legend
                              :series-label series-label)]
     (when visible?
       (ic/view hg))
     hg))
  ([xs]
   (histogram xs nil)))

(defn view [x]
  (if (tab/tab? x)
    (ic/view (tab/tab->dset x))
    (ic/view x))
  nil)
