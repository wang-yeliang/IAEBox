#! /usr/bin/ksh
if [[ -z "$JAVA_HOME" ]]
then 
    echo "Please set the JAVA_HOME environment variable"
    exit
fi

TOOL_DIR=../../../../cots/tools
ANTLIB_DIR=${TOOL_DIR}/build/ant/lib


# set the classpath
CP=$(echo ${JAVA_HOME}/lib/tools.jar ${ANTLIB_DIR}/*.jar | tr ' ' ':')


${JAVA_HOME}/bin/java -classpath ${CP} org.apache.tools.ant.Main $1 $2 $3
