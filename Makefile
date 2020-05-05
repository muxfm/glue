# Util
phony-error:
	$(error someone requested a phony error)

# Bootstrap
install-jtm:
	./install-jtm.sh

install-bb:
	./install-bb.sh

install: install-jtm install-bb

fetch-feed:
	@test $(feed-url) || (echo "usage: make fetch-feed feed-url=<url>" ; exit 1)
	curl $(feed-url) -o anchor-feed.xml

feed:
	./bb get_feed.clj $(username) $(site-base)

# Clean
rm-tmp:
	rm -f anchor-feed.xml anchor.html anchor.json

clean: rm-tmp
	rm -f feed.xml feed.json bb jtm

# Samples
gen-sample-feed:
	 make feed username=bravenotperfect site-base=https://bravenotperfect.com
