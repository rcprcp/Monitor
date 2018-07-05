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
whereAmI|true or false|instrument each routine with a "got here" entry.|



## **Todo**
* plenty of things.
* JavaDocs
* instructions
* some form of reporting mechanism (http, log?)

