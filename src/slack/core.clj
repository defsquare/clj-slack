(ns slack.core
  (:require [cli-matic.core :refer [run-cmd]])
  (:import [com.slack.api Slack]
           [com.slack.api.methods.request.chat ChatPostMessageRequest]
           [com.slack.api.methods.response.chat ChatPostMessageResponse]))

(def slack (Slack/getInstance))


(def cli-opts
  [{:as "Slack token"
    :option "token"
    :type :string}

   {:as "Channel to post message to"
    :option "channel"
    :type :string}

   {:as "Message to post"
    :option "message"
    :type :string}])

(defn post-msg! [token channel message]
  (let [request  (-> (ChatPostMessageRequest/builder)
                     (.channel channel)
                     (.text message)
                     (.build))
        response (-> slack
                     (.methods token)
                     (.chatPostMessage request))]
    response))


(defn post-msg-cli! [{:keys [token channel message] :as args}]
  (post-msg! token channel message))


(def CLI_CONFIG
  {:command "post"
   :description "clj-slack command line "
   :version "0.1.0"
   :runs post-msg-cli!
   :opts cli-opts})

(defn -main [& args]
  (run-cmd args CLI_CONFIG))



