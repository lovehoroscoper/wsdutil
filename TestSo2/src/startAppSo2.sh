cd /app/testso

PATH=/usr/java/jdk1.6.0_23/bin:/usr/java/jdk1.6.0_23/jre/bin:$PATH

export PATH

JAVA_HOME=/usr/java/jdk1.6.0_23
export JAVA_HOME
CLASSPATH=.:/app/testso/lib/commons-lang-2.4.jar:/app/testso/lib/commons-logging-1.1.1.jar:/app/testso/lib/JNative.jar:/app/testso/lib/log4j-1.2.16.jar
export CLASSPATH

nohup java com.test.TextStart >/dev/null &
