#!/bin/sh

set -e
set -u

ant

mvn clean deploy