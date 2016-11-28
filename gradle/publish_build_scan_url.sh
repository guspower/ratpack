#!/bin/bash

# Usage: publish_build_scan_url.sh <token> <channel> <log>

URL=$(grep --color=never "https://gradle.com/" $3)

$(dirname $0)/post_to_slack.sh $1 $2 ${SEMAPHORE_PROJECT_NAME}/${BRANCH_NAME}/#${SEMAPHORE_BUILD_NUMBER} ${URL} ${SEMAPHORE_THREAD_RESULT}
