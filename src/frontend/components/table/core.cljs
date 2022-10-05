(ns frontend.components.table.core
  (:require [frontend.components.antd.core :refer [Card Divider Button]]
            [frontend.events]
            [frontend.subs]
            [frontend.utils.date :refer [seconds->duration]]
            [frontend.components.table.item :refer [Item]]
            [frontend.components.table.NewDateModal :refer [NewDateModal]]
            [goog.string :as gstring]
            [goog.string.format]
            [re-frame.core :as r]))


(def monthTraduction
  {:0 "January"
   :1 "February"
   :2 "March"
   :3 "April"
   :4 "May"
   :5 "June"
   :6 "July"
   :7 "August"
   :8 "September"
   :9 "October"
   :10 "November"
   :11 "December"})

(def currentMonth (.getMonth (js/Date.)))
(def currentMonthKeyword (keyword (str currentMonth)))

(def currentMonthString (currentMonthKeyword monthTraduction))

(def currentDay (.getUTCDate (js/Date.)))
(def currentYear (.getUTCFullYear (js/Date.)))

(defn convertDateToEpoch
  [day month year hours minutes]
  (/ (.getTime
      (js/Date.
       (gstring/format "%s/%s/%s %s:%s" month day year hours minutes)))
     1000))
(defn getDayTimeRange
  [day month year]
  [(convertDateToEpoch day month year 0 0)
   (convertDateToEpoch day month year 23 59)])

(def dateRange (drop 1 (range (+ 1 currentDay))))

(defn isEpochInRange
  [epoch range]
  (and (>= epoch (first range)) (<= epoch (second range))))

(defn isDataInRange [epoch range] (isEpochInRange epoch range))

(defn formatDay
  [day]
  {(keyword (str day)) (getDayTimeRange day (+ 1 currentMonth) currentYear)})

(defn getMapDayRange [dayRange] (into {} (map formatDay dayRange)))

(defn isDataInDay
  [epoch day]
  (isDataInRange epoch ((keyword (str day)) (getMapDayRange dateRange))))

(defn formatData
  [data]
  (map (fn [day]
         (filter (fn [timeData] (isDataInDay (:epoch timeData) day)) data))
       dateRange))


(defn TableTitle
  []
  [:div [:span (str currentMonthString " " (str (.getUTCFullYear (js/Date.))))]
   [Button {:onClick #(r/dispatch [:show-new-time-modal])} "+"]
   (let [modalOpened (r/subscribe [:new-date-modal])]
     [NewDateModal @modalOpened
      (fn [attribute epoch]
        (r/dispatch [:new-time-log attribute epoch]))
      #(r/dispatch [:close-new-time-modal])])])

(defn timeReduce
  [row]
  (reduce +
          (map (fn [data]
                 (if (= "in" (:attribute data)) (- (:epoch data)) (:epoch data)))
               row)))

(defn timeDelta
  [row]
  (let [currentDelta (timeReduce row)]
    (if (= 0 currentDelta)
      nil
      (if (< 0 currentDelta)
        currentDelta
        (+ (quot (js/Date.now) 1000) currentDelta)))))

(defn Row
  [idx row]
  [:div {:key (str idx "rowDate")} [Divider]
   [:span {:style {:padding-right "4px"}} (+ idx 1) " |"]
   [:<> (map Item (sort-by :epoch row))]
   [:<> "| " (seconds->duration (timeDelta row))]])

(defn Table [data] (map-indexed Row data))

(def data (r/subscribe [:data]))

(defn main
  []
  [Card {:style {:margin "4px 0"}} [TableTitle]
   [:<> (Table (formatData @data))]])

