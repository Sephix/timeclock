(ns frontend.subs
  (:require [re-frame.core :as re-frame]
            [frontend.db :as db]))

(re-frame/reg-sub ::name (fn [db] (:name db)))

(re-frame/reg-sub :data (fn [db] (:data db)))

(re-frame/reg-sub :new-date-modal (fn [db] (:new-date-modal db)))