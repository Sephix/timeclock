(ns frontend.components.header.core
  (:require [reagent.core :refer [adapt-react-class]]
            ["antd" :as ant]))


(def Header (adapt-react-class ant/Layout.Header))

(defn main [] [Header
               {:class-name "site-layout-background" :style {:padding 0 :text-align "center"}}
               [:h2 "Timeclock"]])