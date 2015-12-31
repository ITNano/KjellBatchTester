# KjellBatchTester
Tests the batch scheduler output from the course EDA092

## Requirements
Requires an installation of Java8 including JavaFX.
The eclipse project has been made for eclipse Luna, but will certainly work for later versions as well.

## Getting started
Either run the jar file **KjellBatchTester.jar** in base folder or run class **gui/GUIMain.java**. Make sure that the log file that should be parsed is located as **data/log.txt**.

## Log file
All log file rows that does not match the regex is ignored. Valid formats are as follow:

1. {counterID int} Arrive {taskID int} {prio int} {dir int}
2. {counterID int} Transfer {taskID int}
3. {counterID int} Leave {taskID int}

- The counterID is a 0-indexed variable which represents time. For every event, this should count up.
- The taskID is a unique identifier for each task.
- The priority can either be 0 (NORMAL) or 1 (HIGH).
- The direction can either be 0 (DOWN) or 1 (UP).

## Complaints and bugs
The project will only be maintained until late in Jan 2016. If any bug is found before this date, feel free to leave an issue here at github. Otherwise, skip it - it will probably not be read and therefore not fixed.

## Credits
All credits to the almighty smurfs. And some to the programmer Nano.
