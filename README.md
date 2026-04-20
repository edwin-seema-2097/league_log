# **Description.**
A Java command‑line application that reads match results, calculates league standings, and outputs a ranked table following standard football rules.

# **How to Run.**
To run the application, run the shell script build_and_run.sh
This script will compile code and run the application.
If you have already built the code in a previous run, you can simply run the resubmit.sh script.

**compile and run**\
e.g $ ./build_and_run.sh

**resubmit (run using previous build)**\
e.g $ ./resubmit.sh

When the application is running, the user will be guided by prompts on how to proceed.

# **Inputs.**
A user has a choice to enter inputs using the command prompt or by passing a file name of a file that contains the inputs.

## **1st prompt**

The 1st prompt the user will get will be to enter the number 1 or 2.

1 will allow the user to capture inputs on the command prompt.

2 will allow the user to enter the full path and name of the file with inputs.

## **2nd prompt**

The second prompt will be inputs depending on the choice from 1st prompt.

## **Choices**

### If the choice is 1

The user will be asked to enter inputs line by line and enter an empty line when they are done.


### If the choice is 2

The user will be asked to enter the file path and file name

## Input Format
Lions 3, Snakes 3\
Tarantulas 1, FC Awesome 0\
Lions 1, FC Awesome 1\
Tarantulas 3, Snakes 1\
Lions 4, Grouches 0

## Output Format

1. Tarantulas, 6 pts
2. Lions, 5 pts
3. FC Awesome, 1 pt
3. Snakes, 1 pt
5. Grouches, 0 pts
