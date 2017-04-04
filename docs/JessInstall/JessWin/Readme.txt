
William E. Wheeler

9 Jan 2004

About JessWin:

This is a close approximation to ClipsWin that can be downloaded from the CLIPS website.
As such, it is a nice little tool to view the current set of facts, rules, agenda and the
defglobals.  

The JessEditor can be run either from the command-line or called from the JessWin menu.
This makes it easy type a complicated set of rules or facts, save them to a file, and then
cut and paste them from the JessEditor into the input area (bottom half) of JessWin.  
Pressing <return> then sends it to the Jess engine.  Also, the JessEditor has a very 
basic 'Clips Validator' selection to help find that missing or extra left or right 
arenthesis character that the Jess parser finds and appropriately throws an exception.  
This helps prevent a problem before run-time.  The other menu selections are self explanatory.

A help system is included for the various menu items.  Also, the 'functions.html' from the JESS
documentation is part of the JessWin help system.

This is a work in progress so recommendations and bug reports are welcome.

CHANGES from previous release:

1.  moved the readme file to the JessWin directory.

2.  added jess.jar to the classpath as it should have been before.  Sorry.
    
    
---------------------------------------------------------------------------------------------------
Known bugs:

1.  The 'Execute' menu in JessWin contains two submenus for 'Strategy' and 'Watch' options.
    They do not paint properly in Solaris 8 but work fine in Windows and Linux.
---------------------------------------------------------------------------------------------------
Features under development for the next release:

1.  Tooltips
2.  improved JessWin console

---------------------------------------------------------------------------------------------------

It has been tested on the following systems:

Windows 95, 98, ME, XP with several JDK's from Sun
Linux 6.2 with JDK1.3 from Sun and Blackdown
Solaris 8 with JDK1.2.2_06 from Sun

***** This version of JessWin requires Jess60a5 or higher. *****

Windows Environment Settings:

SET JAVA_HOME=C:\JDK1.3

SET PATH=%JAVA_HOME%\BIN

SET CLASSPATH=.;%JAVA_HOME%\SRC.JAR;C:\JESS60a5\jess.jar


Linux or Solaris

The .profile file has the following typical settings:

PATH:  whatever is on your system:/usr/local/jdk1.3/bin
export PATH

CLASSPATH=.:/usr/local/jdk1.3/src.jar:/usr/local/Jess60a5/jess.jar
export CLASSPATH


Instructions:

Make a directory for JessWin and move the WinZip or tar file into that directory
(for example, c:\JessWin or /usr/local/JessWin) and unzip and compile the files 
into that directory.

In the /usr/local/JessWin directory on Linux or Solaris

	tar -xvf JessWin_v1.0.tar

Then:
	javac *.java

Same type of thing for Windows.
  

>From a DOS box (or XWindow), type 

	java JessWin

to invoke the JessWin tool.  The Jess engine starts immediately.  The look at this time is
very similar to invoking

	java jess.Console

from the Jess directory since JessWin is based on that framework.

Similarly, type

	java JessEditor

to invoke the editor.

Comments, recommendations, etc. are welcome.  Please address them to the following account:

home:	physicsguyaz@comcast.net (checked morning and evening)
work:	bill.wheeler@healthtrio.com


Enjoy.


