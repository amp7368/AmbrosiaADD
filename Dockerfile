ARG JAVA_VERSION
FROM ${JAVA_VERSION}

ARG JAR_FILE
ARG WORKINGDIR

WORKDIR ${WORKINGDIR}
COPY ./${JAR_FILE} ./${JAR_FILE}

ENTRYPOINT java -jar ${WORKINGDIR}/${JAR_FILE}
