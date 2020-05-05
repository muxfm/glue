# Util
phony-error:
	$(error someone requested a phony error)

# Bootstrap
install-jtm:
	./install-jtm.sh

install-bb:
	./install-bb.sh

install: install-jtm install-bb

# Do
feed:
	./bb get_feed.clj $(username) $(site-base)

# Clean
rm-tmp:
	rm -f anchor-feed.xml anchor.html anchor.json

clean: rm-tmp
	rm -f feed.xml feed.json meta.json bb jtm

# Samples
gen-sample-feed:
	 make feed username=dissect site-base=https://dsxt.fm
