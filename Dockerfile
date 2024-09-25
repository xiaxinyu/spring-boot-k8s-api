FROM 10.32.224.129/library/maven:3.8.1-openjdk-21-mirrors

ENV DIST_NAME=spring-boot-hello-world \
    DIST_TYPE=jar

COPY target/$DIST_NAME*.$DIST_TYPE /$DIST_NAME.$DIST_TYPE

ENTRYPOINT java $DEFAULT_OPTS $GC_LOG_FILE_OPTS $AGENT_OPTS $APOLLO_OPTS $JAVA_OPTS -jar /$DIST_NAME.$DIST_TYPE