(ns frontend.components.table.NewDateModal
  (:require [reagent.core :refer [atom]]
            [frontend.components.antd.core :refer [Modal DatePicker Segmented]]
            ["moment" :as moment]))

(def attribute (atom "out"))
(def timeAtom (atom (moment)))
(defn NewDateModal
  [opened onOk onCancel]
  [Modal
   {:title "Change time log"
    :open opened
    :onOk (fn [e] (.stopPropagation e) (onOk @attribute (.unix @timeAtom)))
    :onCancel (fn [e] (.stopPropagation e) (onCancel))}
   [:div
    [Segmented
     {:defaultValue @attribute
      :onChange (fn [attr] (reset! attribute attr))
      :options `({:value "in" :label "in"} {:value "out" :label "out"})}]
    [DatePicker
     {:defaultValue @timeAtom
      :showTime true
      :onChange (fn [time _] (reset! timeAtom time))}]]])
