cd /mydata/app/testso

PATH=/usr/java/jdk1.7.0_09/bin:/usr/java/jdk1.7.0_09/jre/bin:$PATH

export PATH

JAVA_HOME=/usr/java/jdk1.7.0_09
export JAVA_HOME
CLASSPATH=.:/mydata/app/testso/lib/commons-lang-2.4.jar:/mydata/app/testso/lib/commons-logging-1.1.1.jar:/mydata/app/testso/lib/JNative.jar:/mydata/app/testso/lib/log4j-1.2.16.jar
export CLASSPATH

nohup java com.test.TextStart >/dev/null &
