(ns frontend.views.sider
  (:require [reagent.core :refer [adapt-react-class]]
            ["antd" :as ant]))


(def Sider (adapt-react-class ant/Layout.Sider))

(defn main
  []
  [Sider
   {:width 100
    :style {:overflow 'auto'
            :height "100vh"
            :position "fixed"
            :left 0
            :top 0
            :bottom 0}}
   [:div {:style {:margin-top "16px" :justify-content "center" :display "flex"}}
    [:img {:src "./assets/images/clojure-icon.svg" :style {:color "black"}}]]])