**Monitor**

Prototype program to instrument a Java program to find routine duration, call counts,
and elapsed time per routine. 

This prgram needs a configurtion file.  The location is controlled by the
MONITOR_PROPERTIES environment variable.  The configuration file can have any name.  

The format of the configuration file is "key=value", as it is parsed and 
turned into a Java properties object. 

The file currently supports: 
whereAmI={true}|false}    instrument each routine with a "got here" entry.
