case "$(uname -s)" in
    Linux*)     platform=linux;;
    Darwin*)    platform=macos;;
esac

curl -sL "https://github.com/borkdude/babashka/releases/download/v0.0.91/babashka-0.0.91-$platform-amd64.zip" -o bb.zip
unzip -qqo bb.zip
rm bb.zip
