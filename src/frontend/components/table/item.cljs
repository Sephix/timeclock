(ns frontend.components.table.item
  (:require [re-frame.core :as r]
            [reagent.core :refer [atom]]
            [frontend.components.antd.core :refer [Modal Button TimePicker]]
            ["moment" :as moment]))

(defn convertEpochToDate [epoch] (js/Date. (* epoch 1000)))

(defn dispatchRemove [id] (r/dispatch [:delete-log id]))

(-> moment
    (.unix 1664826254)
    .unix)

(defn modal
  [item opened handler]
  (let [timeAtom (atom (-> moment
                           (.unix (:epoch item))))]
    [Modal
     {:title "Change time log"
      :open @opened
      :onOk (fn [e]
              (.stopPropagation e)
              (r/dispatch [:update-log (:id item) (.unix @timeAtom)])
              (handler false))
      :onCancel (fn [e] (.stopPropagation e) (handler false))}
     [:div
      [TimePicker
       {:defaultValue (-> moment
                          (.unix (:epoch item)))
        :onChange (fn [time _] (reset! timeAtom time))}]
      [Button
       {:onClick (fn [e]
                   (.stopPropagation e)
                   (dispatchRemove (:id item))
                   (handler false))} "Delete"]]]))


(defn Item
  [item]
  (let [modalOpened (atom false)
        handleModal (fn [state] (reset! modalOpened state))]
    [Button
     {:key (:id item)
      :class-name "time-item"
      :on-click (fn [] (handleModal true))
      :shape "round"
      :type "primary"
      :ghost true
      :danger (not= (:attribute item) "in")
      :style {:margin "0 4px"}}
     (subs (.toLocaleTimeString (convertEpochToDate (:epoch item))) 0 5)
     [modal item modalOpened handleModal]]))