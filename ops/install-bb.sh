case "$(uname -s)" in
    Linux*)     platform=linux;;
    Darwin*)    platform=macos;;
esac

curl -L "https://github.com/borkdude/babashka/releases/download/v0.0.91/babashka-0.0.91-$platform-amd64.zip" -o ./tmp/bb.zip
unzip -qqo ./tmp/bb.zip
rm ./tmp/bb.zip
mv bb ./bin/
