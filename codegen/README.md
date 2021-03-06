CodeGen
=======


Description
-----------
CodeGen is a commandline tool to build a BiBiServ application archives from a xml tool description. The tool description can be either manual written or generated by the Wizard.


Requirements
------------
* Maven 3.3 or newer
* Java 8


How to use
----------

CodeGen comes with a minimalistic builtin help funcionality.

~~~BASH
$ java -jar codegen-1.0.jar -h
usage: codegen -h | -V | -g  [...]
 -g,--generate <runnableitem.xml>   Generate app from xml description.
 -h,--help                          help
 -p,--project <arg>                 Path to project dir. If unset
                                    ${target}+${toolid} will be used!
 -t,--target <arg>                  Path to target dir. If not set java
                                    temporary directory will be used!
 -v,--verbose                       more verbose output
 -V,--version                       version
Supported configuration properties : none
~~~

To build an application from a description file and deploy it on your bibiserv web application you must perform the following two steps.

* Build a BiBiServ application from the description ...
 
~~~BASH  
$ java -jar codegen-1.0.jar -g ~/Desktop/dialign.xml  -t /tmp
~~~

* ... and build a BiBiServ archive from the application and deploy it on the application server.

~~~BASH
$ mvn package
~~~ 

The package task uses a credetenials file named '.bibiserv2_manager' located in the users home dir. The credentials file is generated by the instantBibi task (for local installations) and looks like :

~~~BASH
$ cat ~/.bibiserv2_manager
#Thu, 10 Nov 2016 08:40:31 +0100

role=<adminrole>
password=<hashed password>
port=<server port>
server=<server name or ip>
ssl=true
