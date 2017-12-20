#!/bin/sh

set -e
set -u

usage()
{
	echo "usage: $(basename $0) version-number" 1>&2
	echo "   e.g., $(basename $0) 1.5.2-SNAPSHOT" 1>&2
	exit 1
}

if [ $# -ne 1 ]; then
	usage
fi

version=$1

ant

mvn clean deploy:deploy-file \
-DgroupId=com.runtimeverification.install \
-DartifactId=rv-install \
-Dversion=${version} \
-Dpackaging=jar \
-Dfile=dist/rv-install-${version}.jar \
-Durl=s3://repo.runtime.verification/repository/snapshots \
-DrepositoryId=runtime.verification.snapshots