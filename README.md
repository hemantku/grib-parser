grib-parser
===========

This is a standalone grib parser which will be soon included into tika. The parser consumes a grib format file - grib2 or grb and generates a HTML file

Welcome to the GRIB parser!
Usage: java -jar grib.jar [inputfilepath] [outputfilepath]


There are 3 parts here 

1. gribParser - This has source folder which contains main source code- grib.java and gribParser.java 
2. grib.jar - An executable to run which you can just download and run against the GRIB file
3. Samples - This has sample grib files which you can use to run and test the jar file
4. grib_lib - The library needed by grib.jar to execute successfully

How to run the application?
================================
1.  Clone or download the repository or Take the .jar file along with grib_lib/ folder
2.  Execute the jar file by typing in the following command 
        java - jar grib.jar <folderpath>/gribfilename  <folderfilename>/htmlfilename

Examples 
1. java -jar grib.jar sample/gdas1.forecmwf.2014062612.grib2  ~/Desktop/grib.html
2. java -jar grib.jar sample/NLDAS_FORA0125_H.A20130112.1200.002.grb  ~/Desktop/grib1.html
