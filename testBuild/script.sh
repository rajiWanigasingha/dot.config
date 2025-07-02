#!/bin/bash

FOLDERUI="./hyprlandui.AppDir"

if [ -d "$FOLDERUI" ]; then
  echo "Folder exists: $(basename "$FOLDERUI")"
else
  echo "Folder not found: $FOLDERUI"
fi

FOLDERHYPR="./hyprland"

if [ -d "$FOLDERHYPR" ]; then
  echo "Folder exists: $(basename "$FOLDERHYPR")"
else
  echo "Folder not found: $FOLDERHYPR"
fi

if ! command -v java &> /dev/null; then
  echo "Java is not installed."
  exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | awk -F[\".] '/version/ {print $2}')

if [[ "$JAVA_VERSION" == "21" ]]; then
  echo "Java 21 is installed."
else
  echo "Java is installed, but version is $JAVA_VERSION (not 21)."
fi

echo "Starting server..."
sh "$FOLDERHYPR/bin/hyprland" &
SERVER_PID=$!

echo "Server started with PID $SERVER_PID"
echo "Sleep for 10"
sleep 10

echo "Starting Ui"
"$FOLDERUI/usr/bin/app"