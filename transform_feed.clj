;; Given a feed json from jtm, get channel meta, episode index and individual episodes

(defn episode? [m]
  (contains? m "item"))

(defn strip-cdata [s]
  (subs s 7 (- (count s) 2)))

(defn map->json [m]
  (json/generate-string m {:pretty true}))

(defn meta-map->kv [m]
  (let [k (-> m keys first)]
    [k
     (cond
       (contains? #{"author" "language" "description" "copyright" "title"} k) (-> m vals first (get "!") strip-cdata)
       :else (-> m vals first))]))

(defn save-podcast-meta [channel file-name]
  (->> channel
       (filter #(not (episode? %)) )
       (reduce #(apply assoc %1 (meta-map->kv %2)) {})
       map->json
       (spit file-name)))

(defn main [json-path]
  (let [channel (-> json-path slurp
                    json/parse-string
                    second
                    (get "rss")
                    second
                    (get "channel"))]
    (save-podcast-meta channel "meta.json")))

(let [[json-path] *command-line-args*]
  (when (or (empty? json-path))
    (println "Usage: <json-path>")
    (System/exit 1))
  (main json-path))

(comment
  )
