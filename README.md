Project Summary - Livox List Classification
===
The goal of this project is to detect lists of objects from a list and display them as images.

There are two main portions of code in this project. The Python code and the Android code.


Python
---
The Python code contains the online classifier as well as the pre-processing and evaluation codes.
This code is designed to run on an AWS environment depending on a Elastic Beanstalk server and
an RDS MySQL Database. 


Android
---
The Android code contains a basic android application that was used to test the
online list classifier as it was being created. A more complex version was integrated into
the Livox codebase that contains the offline list classifier pipeline as well as integration for
the online classifier. Details of these can be found in the MQP report.


Label Generation: (./label_vocabulary_generation)
---
This section was used to explore various methods for pre-processing
the Tag and Label information to connect Image's to Entity phrases or words


Contact
---
Richard Valente (rcvalente@wpi.edu)

Andrew Robbertz (alrobbertz@wpi.edu)

Cole Winsor (ccwinsor@wpi.edu)

Zachary Emil (zgemil@wpi.edu)

