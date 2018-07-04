#**Monitor**

This is a prototype program to instrument a given Java program to find routine duration, call counts,
and elapsed time per routine. 

This program needs a configuration file.  The configuration file's location is controlled by the
MONITOR_PROPERTIES environment variable.  The configuration file can have any name.  

The format of the configuration file is "key=value", as it is parsed and 
turned into a Java Properties object. 

The file currently supports:

|key|value|description|
|---|---|---| 
whereAmI|true or false|instrument each routine with a "got here" entry.|
