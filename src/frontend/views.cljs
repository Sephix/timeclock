(ns frontend.views
  (:require
    [re-frame.core :as re-frame]
    [frontend.subs :as subs]
    [reagent.core :as r]
    ))

(def current-time (r/atom (js/Date.now)))


(defn main-panel []
      [:div
       [:h1 "Pointeuse"]
       [:div
        [:span @current-time]]
        [:span {:class-name "blabla"} "this si a span"]
        [:button {:on-click #(reset! current-time (js/Date.now))} "test"]
       ])
