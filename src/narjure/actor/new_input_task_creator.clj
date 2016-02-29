(ns narjure.actor.new-input-task-creator
  (:require
    [co.paralleluniverse.pulsar
     [core :refer :all]
     [actors :refer :all]
     ])
  (:refer-clojure :exclude [promise await])
  (:gen-class))

(declare process-system-time)
(declare process-sentence)
(declare process-unhandled-msg)
(declare new-input-task-creator-actor)

(defn process-system-time [time state]
  (! :logger [:log-msg :log-debug "process-system-time"])
  {:time time})

(defn process-sentence [_ _]
  (! :logger [:log-msg :log-debug "process-sentence"]))

(defn process-unhandled-msg [msg]
  (! :logger [:log-msg :log-debug (str "In new-input-task-creator :else" msg)]))

(defsfn new-input-task-creator-actor
        "state is system-time"
        [in-state]
        (register! :input-task-creator @self)
        (set-state! in-state)
        (loop []
          (receive [msg]
                   [:system-time-msg time] (set-state! (process-system-time time @state))
                   [:sentence-msg sentence] (process-sentence sentence @state)
                   :else (process-unhandled-msg msg))
          (recur)))
