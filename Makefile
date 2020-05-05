# Util
phony-error:
	$(error someone requested a phony error)

# Bootstrap
install-jtm:
	./ops/install-jtm.sh

install-bb:
	./ops/install-bb.sh

install: install-jtm install-bb

# Do
feed:
	./bin/bb get_feed.clj $(username) $(site-base)

transform:
	./bin/bb transform_feed.clj ./tmp/feed.json

# Clean
clean:
	rm -rf public/* tmp/*

hard-clean: clean
	rm -f bin/*

# Samples
gen-sample-feed:
	 make feed username=couples-therapy site-base=https://ct.fm
