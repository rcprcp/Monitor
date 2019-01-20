# **Monitor**

This is a prototype program to instrument a given Java program to find call counts
and elapsed time per routine.  

In the future it would also be interesting to monitor JVM operational metadata such as
thread counts, gc activity, detailed heap utilization, etc.  It would also be interesting
to see if we can track inbound and outbound socket communication
to monitor HTTP/HTTPS traffic and database (and external system) connectivity.  

We can probably determine the number of classes in the class loader which invoked the agent code.
This is implemented - it's available in a JMX MBean.  We need to test with privsate classloaders. 

This program uses a Java-properties-style configuration file.  The configuration file's
location is controlled by the MONITOR_PROPERTIES environment variable.
The configuration file can have any name.  

The format of the configuration file is "key=value"; it is parsed into a Java Properties object. 

The file currently supports:

|key|value|description|
|---|---|---| 
includeList|String| Comma separated list of regexes for selecting classes to instrument.
excludeList|String| Comma separated list of regexes for selecting classes to not instrument.
whereAmI|boolean|Instrument each routine with a "got here" entry.
httpPort| integer between 1025 and 65535|specify the port for Monitor's http server. 1128 is the default.
appname|String|name under which statistics will be published.
destinationHostName|String|hostname to which we're sending the event records. 
destinationPort|integer between 1025 and 65536 |specify the (non-privileged) port on which the MonitorServer is running. 

### **includeList example:**
includeList=com/cottagecoders/victim/Vi.\*,com/streamsets.\*

. represents any character, * for 0 to any number of characters.

## **Installation and Use:** 
* create a monitor.properties file with the following lines:
```
whereAmi=true
includeList=com/cottagecoders/victim/Vi.*,com/streamsets.*
httpPort=22277
appname=myapp
destinationHostname=localhost
destinationPort=1234
```
* Set the environment variable Monitor uses to point to the properties file: `export MONITOR_PROPERTIES=~/monitor.properties`
* Create a directory which will contain Monitor and Victim, for example `mkdir bobtest`
* `cd` into that directory eg - `cd bobtest`
* `git clone https://github.com/rcprcp/Monitor.git`
* `cd Monitor`
* `mvn clean package`
* `cd ..`
* `git clone https://github.com/rcprcp/Victim.git`
* `cd Victim`
* `mvn clean package`
* `cd target`
* run Victim from within the target subdirectory specify Monitor as a Java Agent.
* `java -javaagent:<PATH>/target/Monitor-1.0-SNAPSHOT-jar-with-dependencies.jar -jar Victim-1.0-SNAPSHOT-jar-with-dependencies.jar`

To run the program with Victim- follow the above steps then... 
* `cd bobtest`
* download Victim: [https://github.com/rcprcp/Victim.git]  you may have to copy it over from Downloads directory to the bobtest directory.
* When Victim is built - from the Victim/target directory run `java -XX:+PrintGCDetails -verbose:gc -Xms10g -Xmx10g -javaagent:<PATH>/Monitor/target/Monitor-1.0-SNAPSHOT-jar-with-dependencies.jar -jar Victim-1.0-SNAPSHOT-jar-with-dependencies.jar >x 2>x2` nb.  I redirect stdout, stderr to 'x' and 'x2' to review them later. 
## **Todo**
plenty of things.
- [ ] JavaDocs
- [ ] More complete instructions
- [ ] Some form of logging mechanism (file, log4j)
- [ ] Seems to crash when instrumenting some classes in Data Collector.
- [ ] JUnit tests for some of the classes.
- [x] Fix the pom file for packaging.
- [ ] Integration tests for some classes.
- [x] Configuration file.
- [x] Gather info about number of loaded classes.
- [ ] determine call hierarchy
- [ ] Object creation code and heap space monitoring code.
- [x] Collect information on number of classes loaded.
- [ ] Add some CSS to the html.   :smile: 
- [ ] More HTML pages - for additional metrics.
- [ ] Can we use a templating engine for HTML?
- [ ] Add sort(s) the the Metrics table (name, elapsed time, hits)
- [ ] Need to identify how to walk the heap to find and "bucketize" objects for counting and size monitoring. 
- [ ] Need to intercept inbound and outbound socket data.  
- [ ] Currently testing with Victim - and Data Collector
- [ ] UI code needs implementation.  
- [x] work on DB schema. (V1 is done)