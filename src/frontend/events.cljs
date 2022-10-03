(ns frontend.events
  (:require [cljs.reader :refer [read-string]]
            [re-frame.core :as re-frame]
            [frontend.db :refer [default-db]]))



(defn reset-db [val] (reset! default-db val))



(defn updateLocalStorage [db] (js/window.localStorage.setItem "data" db))
(defn queryLocalStorage
  []
  (read-string (js/window.localStorage.getItem "data")))

(defn update-db
  [db modifier]
  (update-in db
             [:data]
             conj
             {:id (-> (random-uuid)
                      .toString)
              :epoch (quot (js/Date.now) 1000)
              :attribute modifier}))

(defn log
  [mod]
  (let [db (queryLocalStorage)]
    ;new-db (update-db db mod)
    (js/console.log db)
    (js/console.log (update-db db mod))
    (updateLocalStorage (update-db db mod))
    {:db (queryLocalStorage)}))

(defn log-in [_ _] (log "in"))
(defn log-out [_ _] (log "out"))

(defn compareId
  [id]
  (js/console.log id)
  (fn [seq]
    (js/console.log seq)
    (js/console.log (:id seq))
    (js/console.log (not= (:id seq) id))
    #(not= (:id seq) id)))
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
          {:data (->> (:data db)
                      (map
                        (fn [item]
                          (if (not= (:id item) item-id)
                            item
                            (update-in item [:epoch] (constantly epoch))))))}]
    (js/console.log new-db)
    (updateLocalStorage new-db)
    {:db (queryLocalStorage)}))

(def testMap
  {:id "43dfa8a2-276c-49b0-b8e0-9856f8ce1488"
   :epoch 1664827403
   :attribute "in"})

(update-in testMap [:epoch] (constantly 10))

(re-frame/reg-fx :log-in reset-db)

(re-frame/reg-fx :log-out reset-db)

(re-frame/reg-fx :delete-log reset-db)

(re-frame/reg-event-fx :log-in log-in)

(re-frame/reg-event-fx :log-out log-out)

(re-frame/reg-event-fx :delete-log delete-log)

(re-frame/reg-event-fx :update-log update-log)

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