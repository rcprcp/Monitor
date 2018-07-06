# **Monitor**

This is a prototype program to instrument a given Java program to find call counts
and elapsed time per routine. 

Also, we can probably determine the number of classes in the class loader which invoked the agent code.

This program needs a configuration file.  The configuration file's location is controlled by the
MONITOR_PROPERTIES environment variable.  The configuration file can have any name.  

The format of the configuration file is "key=value", as it is parsed and 
turned into a Java Properties object. 

The file currently supports:

|key|value|description|
|---|---|---| 
whereAmI|true or false|instrument each routine with a "got here" entry.

## **Installation and Use:** 
* create a monitor.properties file: `echo whereAmI=true >~/monitor.properties` - or false depending on the level of trace desired
* set the enviroment variable Monitor uses to point to the properties file: `export MONITOR_PROPERTIES=~/monitor.properties`
* create a directory which will contain Monitor and Victim, for example `cd bobtest`
* `cd` into that directory eg - `cd bobtest`
* `git clone https://github.com/rcprcp/Monitor.git`
* `cd Monitor`
* `mvn clean package`
* `cd ..`
* `git clone https://github.com/rcprcp/Victim.git`
* `cd Victim`
* `mvn clean package`
* `cd target`
* run Victim with Monitor as a Java Agent.  fix up the path names in this command to match your path. 
* `java -javaagent:/home/bob/IdeaProjects/Monitor/target/Monitor-1.0-SNAPSHOT-jar-with-dependencies.jar -jar Victim-1.0-SNAPSHOT-jar-with-dependencies.jar`



## **Todo**
* plenty of things.
* JavaDocs
* more complete instructions
* some form of reporting mechanism (http, log?)
* currently testing with Victim - should try Data Collector.  :)


