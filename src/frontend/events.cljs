(ns frontend.events
  (:require [cljs.reader :refer [read-string]]
            [re-frame.core :as re-frame]
            [frontend.db :refer [default-db]]))



(defn reset-db [val] (reset! default-db val))



(defn updateLocalStorage [db] (js/window.localStorage.setItem "data" db))
(defn queryLocalStorage
  []
  (read-string (js/window.localStorage.getItem "data")))

(defn new-time-log
  [db modifier epoch]
  (update-in db
             [:data]
             conj
             {:id (-> (random-uuid)
                      .toString)
              :epoch epoch
              :attribute modifier}))

(defn update-db
  [db modifier]
  (new-time-log db modifier (quot (js/Date.now) 1000)))

(defn log
  [mod]
  (let [db (queryLocalStorage)]
    (updateLocalStorage (update-db db mod))
    {:db (queryLocalStorage)}))

(defn log-in [_ _] (log "in"))
(defn log-out [_ _] (log "out"))

(defn delete-log
  [_ events]
  (js/console.log events)
  (let [item-id (second events)
        db (queryLocalStorage)
        new-db {:data (->> (:data db)
                           (filter #(not= (:id %) item-id)))}]
    (js/console.log new-db)
    (updateLocalStorage new-db)
    {:db (queryLocalStorage)}))



(defn update-log
  [_ events]
  (let [item-id (second events)
        epoch (nth events 2)
        db (queryLocalStorage)
        new-db
          {:data (->>
                   (:data db)
                   (map (fn [item]
                          (if (not= (:id item) item-id)
                            item
                            (update-in item [:epoch] (constantly epoch))))))}]
    (js/console.log new-db)
    (updateLocalStorage new-db)
    {:db (queryLocalStorage)}))

(defn show-time-modal [coeffects _]
  (let [db  (:db coeffects)
        new-db (update-in db [:new-date-modal] (constantly true))]
    {:db new-db}))
(defn close-new-time-modal [coeffects _]
  (let [db  (:db coeffects)
        new-db (update-in db [:new-date-modal] (constantly false))]
    {:db new-db}))


(defn new-time-log-modal [coeffects events]
  (js/console.log coeffects events)
  (let [db  (:db coeffects)
        db-with-new-log (new-time-log db (second events) (nth events 2))
        closed-modal-db (update-in db-with-new-log [:new-date-modal] (constantly false))]
    (updateLocalStorage closed-modal-db)
    {:db (queryLocalStorage)}))

(re-frame/reg-fx :log-in reset-db)

(re-frame/reg-fx :log-out reset-db)

(re-frame/reg-fx :delete-log reset-db)

(re-frame/reg-fx :show-new-time-modal reset-db)

(re-frame/reg-fx :close-new-time-modal reset-db)

(re-frame/reg-fx :new-time-log reset-db)

(re-frame/reg-event-fx :log-in log-in)

(re-frame/reg-event-fx :log-out log-out)

(re-frame/reg-event-fx :delete-log delete-log)

(re-frame/reg-event-fx :update-log update-log)

(re-frame/reg-event-fx :show-new-time-modal show-time-modal)

(re-frame/reg-event-fx :close-new-time-modal close-new-time-modal)

(re-frame/reg-event-fx :new-time-log new-time-log-modal)

(re-frame/reg-event-db ::initialize-db (fn [_ _] (queryLocalStorage)))


(def currentlist
  '({:id "03b50481-2ea9-477a-bf31-5c97be20f646"
     :epoch 1664778272
     :attribute "in"}))

(def dummyDb {:data (into () currentlist)})

(update-in dummyDb
           [:data]
           conj
           {:id (-> (random-uuid)
                    .toString)
            :epoch (quot (js/Date.now) 1000)
            :attribute "modifier"})