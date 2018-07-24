# **Monitor**

This is a prototype program to instrument a given Java program to find call counts
and elapsed time per routine.  It needs to be expanded to track objects in the heap and inbound and outbound socket communication.

We can probably determine the number of classes in the class loader which invoked the agent code.
This is implemented - it's available in a JMX MBean.

This program uses a configuration file.  The configuration file's location is controlled by the
MONITOR_PROPERTIES environment variable.  The configuration file can have any name.  

The format of the configuration file is "key=value"; it is parsed into a Java Properties object. 

The file currently supports:

|key|value|description|
|---|---|---| 
includeList|String| list of comma separated regexes to select classes to instrument. 
whereAmI|true or false|instrument each routine with a "got here" entry.
httpPort| integer between 1025 and 65535|specify the port for the http server. 1128 is the default.
### **includeList example:**
includeList=com/cottagecoders/victim/Vi.\*,com/streamsets.\*

. represents any character, * for 0 to any number of characters.

## **Installation and Use:** 
* create a monitor.properties file with the following lines:
```
whereAmi=true
includeList=com/cottagecoders/victim/Vi.*,com/streamsets.*,org/owasp/webgoat.*,org/springframework.*
httpPort=22277
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
* `java -javaagent:../../Monitor/target/Monitor-1.0-SNAPSHOT-jar-with-dependencies.jar -jar Victim-1.0-SNAPSHOT-jar-with-dependencies.jar`

To run the program with Webgoat - follow the above steps then... 
* `cd bobtest`
* download Webgoat - i downloaded the jar from here: [https://github.com/WebGoat/WebGoat/releases]  you may have to copy it over from Downloads directory to the bobtest directory.
* run WebGoat and redirect output to review later:
* When WebGoat is in this directory run `java -javaagent:./Monitor/target/Monitor-1.0-SNAPSHOT-jar-with-dependencies.jar -jar webgoat-server-8.0.0.M21.jar >x 2>x2` nb.  I redirect stdout, stderr to 'x' and 'x2' to review them later. 
## **Todo**
plenty of things.
- [ ] JavaDocs
- [ ] More complete instructions
- [ ] Some form of logging mechanism (file, log4j)
- [ ] Seems to crash when instrumenting some classes in WebGoat.
- [ ] JUnit tests for some of the classes.
- [x] Fix the pom file for packaging.
- [ ] Integration tests for some classes.
- [x] Configuration file.
- [x] Gather info about number of loaded classes.
- [ ] read the fine print before enabling Travis-CI.
- [ ] Object creation code and heap space monitoring code.
- [x] Collect information on number of classes loaded.
- [ ] Add some CSS to the html.   :smile: 
- [ ] More HTML pages - for additional metrics.
- [ ] Can we use a templating engine wfor out HTML?
- [ ] Add sort(s) the the Metrics table (name, elapsed time, hits)
- [ ] Need to identify how to walk the heap to find and "bucketize" objects for counting and size monitoring. 
- [ ] Need to intercept inbound and outbound socket data.  
- [ ] Currently testing with Victim - and WebGoat - should try Data Collector

