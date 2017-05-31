#!/bin/bash

[ ! -e classes ] && mkdir classes

# Use C preprocessors in Room.c to generate large Room.java class
cpp Room.c Room.java
# Remove junk from cpp
sed 's/^# /\/\//g' Room.java > /tmp/Room.java
mv /tmp/Room.java Room.java

javac -Xlint:unchecked *.java || exit 0
mv *.class classes

echo Testing...
cd classes
java House
