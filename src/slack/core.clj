(ns slack.core
  (:require [cli-matic.core :refer [run-cmd]]
            [clojure.java.data :as jd])
  (:import [com.slack.api Slack]
           [com.slack.api.methods.request.chat ChatPostMessageRequest]
           [com.slack.api.model Attachment]
           [com.slack.api.methods.response.chat ChatPostMessageResponse]
           [com.slack.api.methods.request.files FilesUploadRequest]
           [com.slack.api.bolt App AppConfig]))

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
  ([{:keys [token channel message message-filename attachment-filename] :as args}]
   (println (str "Post message to channel " channel))
   (when attachment-filename
     (print " with attachment " attachment-filename))
   (when message-filename
     (println " with message in file" message-filename))
   (post-msg! token channel
              (if (empty? message-filename) message (slurp message-filename) )
              (if (empty? attachment-filename) nil [{:pretext attachment-filename :text (slurp attachment-filename)}])))
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
     (clojure.pprint/pprint (jd/from-java response))
     response)))

(defn upload-file!
  ([{:keys [token filename initial-comment channel] :as args}]
   (println (format "Upload file %s to channel %s" filename channel))
   (upload-file! token filename initial-comment [channel]))
  ([token filename initial-comment channels]
   (let [channels-list (java.util.ArrayList.)
         _             (doseq [channel channels]
                         (.add channels-list channel))
         f        (java.io.File. filename)
         file-data (let [ary (byte-array (.length f))
                         is (java.io.FileInputStream. f)]
                     (.read is ary)
                     (.close is) ary)
         request (-> (FilesUploadRequest/builder)
                     (.content (slurp filename))
                     (.file f)
                     (.fileData file-data)
                     (.filename filename)
                     (.token token)
                     (.title filename)
                     (.initialComment initial-comment)
                     (.channels channels-list)
                     (.build))
         response (-> slack
                      (.methods token)
                      (.filesUpload request))]
     (clojure.pprint/pprint (jd/from-java response))
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

(comment
  (post-msg!
             "xoxb-494114936464-xxxx"
             "team-galaxie"
             "text"
             "attachment text"))
