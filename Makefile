anchor-feed-url = https://anchor.fm/s/1f178454/podcast/rss
anchor-username = oooxcast
custom-base-url = https://oooox.com

fetch-feed:
	curl -sL $(anchor-feed-url) -o anchor-feed.xml

install-jtm:
	./install-jtm.sh

replace-rss-url:
	sed 's#$(anchor-feed-url)#$(custom-base-url)/feed.xml#g' ./anchor-feed.xml > with-our-rss.xml

replace-episode-urls:
	sed 's#https://anchor.fm/$(anchor-username)#$(custom-base-url)#g' ./with-our-rss.xml > feed.xml

xml-to-json:
	./jtm feed.xml > feed.json

rm-tmp:
	rm -f anchor-feed.xml with-our-rss.xml

clean: rm-tmp
	rm -f feed.xml feed.json jtm

feed: fetch-feed install-jtm replace-rss-url replace-episode-urls xml-to-json rm-tmp
