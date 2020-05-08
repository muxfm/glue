(ns glue.get-feed
  "Given an anchor profile json, figure out the link to rss"
  (:require [clojure.java.shell :as shell]
            [cheshire.core :as json]
            [babashka.wait :as wait]
            [clojure.string :as str]))

(defn json->feed-url [json-path]
  (-> json-path
      slurp ;; read to ram
      json/parse-string
      second ;; skip the doctype
      (get-in ["html" 1 "head"])
      (->> (filter #(contains? % "link/")))
      (->> (filter #(= (get-in % ["link/" "attributes" "type"])
                       "application/rss+xml")))
      first
      (get-in ["link/" "attributes" "href"])))

(defn fetch-file [url o]
  (shell/sh "curl" "-sL" url "-o" o))

(defn xhtml->json [in out]
  (shell/sh "bash" "-c" (str "./bin/jtm " in " > " out))
  (wait/wait-for-path out))

(defn fetch-podcast-feed-from-page-json [feed-url]
  (println "Fetching RSS feed at " feed-url)
  (shell/sh "curl" "-sL" feed-url "-o" "./tmp/anchor-feed.xml")
  (wait/wait-for-path "./tmp/anchor-feed.xml")
  (slurp "./tmp/anchor-feed.xml"))

(defn replace-anchor-feed-links [anchor-feed feed-url username site-base]
  (-> anchor-feed
      ;; Replace Anchor links with our own
      (str/replace (re-pattern feed-url)
                   (str site-base "/feed.xml"))

      ;; Replace Episode urls with our own
      (str/replace (re-pattern (str "https://anchor.fm/" username))
                   site-base)
      (->> (spit "./public/feed.xml"))
      ))

(defn main [username site-base]
  (println "Fetching public profile")
  (fetch-file (str "https://anchor.fm/" username) "./tmp/anchor.html")

  (println "Converting public profile html to json")
  (xhtml->json "./tmp/anchor.html" "./tmp/anchor.json")

  (println "Parsing feed url")
  (let [feed-url (json->feed-url "./tmp/anchor.json")
        anchor-feed (fetch-podcast-feed-from-page-json feed-url)]

    (println "Replacing Anchor links")
    (replace-anchor-feed-links anchor-feed feed-url username site-base)

    (wait/wait-for-path "./public/feed.xml")
    (xhtml->json "./public/feed.xml" "./tmp/feed.json")
    ))


(when *command-line-args*
  (let [[username site-base] *command-line-args*]
    (when (or (empty? username) (empty? site-base))
      (println "Usage: <username> <site-base>")
      (println "Note: Sitebase should not included trailing slash")
      (System/exit 1))
    (main username site-base)))


(comment
  (spit "c.xml" (replace-anchor-feed-links
               (slurp "anchor-feed.xml")
               "https://anchor.fm/s/22b6608/podcast/rss"
               "bravenotperfect"
               "https://sitebase.co"))

  (spit "sp.xml" (str/replace (slurp "sample-feed.xml")
                            (re-pattern "https://anchor.fm/s/22b6608/podcast/rss")
                            "https://fite.com/feed.xml"))
)
