(ns frontend.views.timeclock
  (:require [reagent.core :as r]
            [re-frame.core :as reframe]
            [frontend.components.antd.core :refer [Card CardGrid Title Button]]
            [frontend.components.table.core :refer [main]
             :rename {main TimeTable}]))



(def current-time (r/atom (js/Date.now)))

(defn formatTime [millis] (.toLocaleTimeString (js/Date. millis)))

(js/setInterval #(reset! current-time (js/Date.now)) 1000)

(def gridStyle
  {:width "50%"
   :display "flex"
   :flex-direction "column"
   :justify-content "center"
   :align-items "center"})

(def buttonStyle {:margin "4px 0"})

(defn clock
  []
  [Card [CardGrid {:style gridStyle} [Title (formatTime @current-time)]]
   [CardGrid {:style gridStyle}
    [Button 
     {:type "primary" :shape "round" :size "large" :style buttonStyle
      :on-click #(reframe/dispatch [:log-in])}
     "In"]
    [Button {:type "secondary" :shape "round" :size "large" :style buttonStyle
             :on-click #(reframe/dispatch [:log-out])}
     "Out"]]])


()

(defn main [] [:<> [clock] [TimeTable]])