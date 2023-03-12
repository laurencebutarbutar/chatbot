#!/bin/bash

echo "CONTAINER_ENV : ${CONTAINER_ENV}"

# define default attribute..
APPLICATION_JAR="${APPLICATION_NAME}.jar"

# prepared arguments allocation memory..
if [[ "x${XMSLimit}" == "x" ]]; then
    # default allocation memory minimum
    XMSLimit="128m"
fi

if [[ "x${XMXLimit}" == "x" ]]; then
    # default allocation memory maximum
    XMXLimit="256m"
fi

JAVA_OPTS="-Xms${XMSLimit} -Xmx${XMXLimit}"

# set up spring profile active.
if [[ "x${CONTAINER_ENV}" != "x" ]]; then
    # default environment variable
    JAVA_OPTS="${JAVA_OPTS} -Dspring.profiles.active=${CONTAINER_ENV,,}"
fi

if [[ "x${SPRING_CLOUD_CONFIG_URI}" != "x" ]]; then
 JAVA_OPTS="${JAVA_OPTS} -Dspring.cloud.config.uri=${SPRING_CLOUD_CONFIG_URI}"
fi


# run apps.
eval "java -jar ${JAVA_OPTS} ${APPLICATION_JAR}"
