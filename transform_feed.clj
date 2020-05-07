;; Given a feed json from jtm, get channel meta, episode index and individual episodes

(defn episode? [m]
  (contains? m "item"))

(defn strip-cdata
  "Convert a string like [CDATA[Juice]] to Juice"
  [s]
  (when s (subs s 7 (- (count s) 2))))

(defn map->json [m]
  (json/generate-string m {:pretty true}))

(defn remove-meta-keys [m]
  (dissoc m "atom:link/"))

(defn meta-map->kv [m]
  (let [k (-> m keys first)]
    (cond
      (= k "image") [k (-> m vals first first (get "url"))]

      (= k "itunes:image/") ["itunes:image" (-> m vals first (get-in ["attributes" "href"]))]

      (= k "itunes:owner") [k (->> m vals first (apply merge))]

      (= k "itunes:category") [k (map
                                  #(if (contains? % "attributes")
                                     (get-in % ["attributes" "text"])
                                     (get-in % ["itunes:category/" "attributes" "text"])) (-> m vals first))]

      (contains? #{"author" "language" "description" "copyright" "title"} k)
      [k  (-> m vals first (get "!") strip-cdata)]

      :else [k  (-> m vals first)])))

(defn save-podcast-meta [channel file]
  (->> channel
       (filter #(not (episode? %)) )
       (reduce #(apply assoc %1 (meta-map->kv %2)) {})
       remove-meta-keys
       map->json
       (spit file)))

(defn json-path->channel [json-path]
  (-> json-path
      slurp
      json/parse-string
      second
      (get "rss")
      second
      (get "channel")))

(defn cleanse-episode-map [m]
  (into {} (pmap (fn [[k v]]
                   (cond
                     (contains? #{"description" "title" "dc:creator"} k)
                     [k (-> v (get "!") strip-cdata)]

                     (= k "guid") [k (second v)]

                     (= k "enclosure/") ["enclosure" (get v "attributes")]

                     (= k "itunes:image/") ["itunes:image" (get-in v ["attributes" "href"] nil)]
                     :else [k v])
                   ) m)))

(defn transform-episodes [channel]
  (->> channel
       (filter episode?)
       (map #(get % "item"))
       (map #(apply merge %))
       (map cleanse-episode-map)
       ))

(defn save-individual-episode [transformed]
  (let [file-name (-> transformed
                      (get "link")
                      (str/split #"/")
                      last)
        file-path (str "./public/episodes/" file-name ".json")]
    (io/make-parents file-path)
    (spit file-path  (map->json transformed))))

(defn main [json-path]
  (let [channel (json-path->channel json-path)
        transformed-episodes (transform-episodes channel)]
    (save-podcast-meta channel "./public/meta.json")

    (doall
     (pmap save-individual-episode transformed-episodes))
    (spit "./public/episodes/index.json" (map->json transformed-episodes))
    ))

(let [[json-path] *command-line-args*]
  (when (or (empty? json-path))
    (println "Usage: <json-path>")
    (System/exit 1))
  (main json-path))

(comment
  (def channel (json-path->channel "./tmp/feed.json"))
  (spit "./public/index.json"
        (transform-episodes channel)
        )

  (->> (transform-episodes channel)

       (pmap save-individual-episode))

  (let [tfd (transform-episodes (json-path->channel "./tmp/feed.json"))]
    (spit "./public/episodes/index.json" (map->json tfd)))


  )
