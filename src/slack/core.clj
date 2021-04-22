(ns slack.core
  (:require [cli-matic.core :refer [run-cmd]])
  (:import [com.slack.api Slack]
           [com.slack.api.methods.request.chat ChatPostMessageRequest]
           [com.slack.api.model Attachment]
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

(defn post-msg!
  ([{:keys [token channel message attachments] :as args}]
   (post-msg! token channel message attachments))
  ([token channel message attachments]
   (let [attachments-list (java.util.ArrayList.)
         _                (doseq [{:keys [pretext text] :as attachment} attachments]
                            (.add attachments-list (-> (Attachment/builder)
                                                       (.pretext pretext)
                                                       (.text text)
                                                       (.build))))
         request  (-> (ChatPostMessageRequest/builder)
                      (.channel channel)
                      (.text message)
                      (.attachments attachments-list)
                      (.build))
         response (-> slack
                      (.methods token)
                      (.chatPostMessage request))]
     response)))


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

"xapp-1-A01ULDGPCAF-1965474607862-de4f35260aed2f0c8aa3b9c61b3a408f01580fa1e10fc5474095bb9d5e6451b1"
"xoxb-494114936464-1996087116864-kNtMFXgLu27J7N8qQp3CIb9A"

(comment
  (post-msg!
             "xoxb-494114936464-1996087116864-kNtMFXgLu27J7N8qQp3CIb9A"
             "team-galaxie"
             "text"
             "attachment text"))
