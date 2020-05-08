# Util
phony-error:
	$(error someone requested a phony error)

repl:
	bb --nrepl-server 9000

# Bootstrap
install-jtm:
	./ops/install-jtm.sh

install-bb:
	./ops/install-bb.sh

install: install-jtm install-bb

# Do
feed:
	./bin/bb ./src/glue/get_feed.clj $(username) $(site-base)

transform:
	./bin/bb ./src/glue/transform_feed.clj ./tmp/feed.json

run-tests:
	./bin/bb --classpath "src:test" ./test/glue/get_feed_test.clj

# Clean
clean:
	rm -rf public/* tmp/*

hard-clean: clean
	rm -f bin/*

# Samples
gen-sample-feed:
	 make feed username=couples-therapy site-base=https://ct.fm
