(ns frontend.components.antd.core
  (:require
[reagent.core :refer [adapt-react-class]]
["antd" :as ant 
 :refer [Layout Card Button Divider Modal TimePicker] 
 :rename {Layout antLayout 
          Card antCard
          Button antButton 
          Divider antDivider
          Modal antModal
          TimePicker antTimePicker
          }]))

(def Layout (adapt-react-class antLayout))
(def Content (adapt-react-class ant/Layout.Content))
(def Card (adapt-react-class antCard))
(def CardGrid (adapt-react-class ant/Card.Grid))
(def Title (adapt-react-class ant/Typography.Title))
(def Button (adapt-react-class antButton))
(def Divider (adapt-react-class antDivider))
(def Modal (adapt-react-class antModal))
(def TimePicker (adapt-react-class antTimePicker))