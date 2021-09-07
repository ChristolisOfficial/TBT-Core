#!/bin/sh
gradle build
rm ../TBT-Lobby/test/plugins/TBT-Core.jar
mv build/libs/TBT-Core.jar ../TBT-Lobby/test/plugins/
echo "Done!"
