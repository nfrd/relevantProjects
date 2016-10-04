// **********************************************************
// Assignment2:
// Student1: Abdulqader Saafan
// UTOR user_name:saafanab
// UT Student #:1001190374
// Author: Abdulqader Saafan
//
// Student2: Vincent Nafrada
// UTOR user_name: nafradav
// UT Student #: 1001537161
// Author: Vincent Nafrada
//
// Student3: Shayan Ghazi
// UTOR user_name: ghazisha
// UT Student #: 1001694758
// Author: Shayan Ghazi
//
// Student4:Nitharsan Kopu
// UTOR user_name:kopunith
// UT Student #:1001262493
// Author:Nitharsan Kopu
//
//
// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC 207 and understand the consequences.
// *********************************************************
package driver;

import java.io.*;
import filesystem.Directory;
import java.util.Arrays;
import commands.*;

public class JShell {

  private static Mkdir mkdirCommand = Mkdir.createMkdirObject();
  private static Man manCommand = Man.makeManual();
  private static Cd cdCommand = Cd.createCdObject();
  private static Pwd pwdCommand = Pwd.createAPwdObject();
  private static Ls lsCommand = Ls.createLsObject();
  private static Pushd pushdCommand = Pushd.makeDirectoryStack();
  private static Popd popdCommand = Popd.makePopd();
  private static Echo echoCommand = Echo.createEchoObject();
  private static Cat catCommand = Cat.createACatObject();
  private static History historyCommand = History.createHistoryObject();
  private static Exit exitCommand = Exit.createExitObject();
  private static Mv mvCommand = Mv.createMvObject();
  private static Cp cpCommand = Cp.createCpObject();
  private static Redo redoCommand = Redo.createRedoObject();
  private static Get getCommand = Get.createGetObject();
  private static Grep grepCommand = Grep.createGrepObject();
  private static Exception invalidInput =
      new Exception("Invalid Command, please try again");
  private static String[] commandsArray =
      {"mkdir", "exit", "cd", "ls", "pwd", "cat", "echo", "pushd", "popd",
          "history", "man", "mv", "cp", "get", "grep"};
  private static String cmdReturn = "";

  public static void main(String[] args) throws Exception {
    Directory.createRootDirectory();
    BufferedReader inputBr =
        new BufferedReader(new InputStreamReader(System.in));

    String line;

    while (true) {

      // print current directory
      System.out.print("#/ ");

      // Save user input
      line = inputBr.readLine();

      // Add user input to history
      historyCommand.addToHistory(line);

      // Trim line
      line = line.trim();
      try {
        String returnFromExe = JShell.executeLine(line);
        if (!returnFromExe.equals("") && returnFromExe != null) {
          System.out.println(returnFromExe);
        }

      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * This method is used to execute the command in the line given
   * 
   * @param line the command line (inputed by user or by another command)
   * @return cmdReturn the return of the command performed
   * @throws Exception invalid input used when invalid input is inputed
   */
  public static String executeLine(String line) throws Exception {
    String[] lineArray = line.split(" ");
    cmdReturn = "";
    // Get the string of the command
    String commandName = lineArray[0];
    // if the input has a command in it
    if (Arrays.asList(commandsArray).contains(commandName)
        || commandName.startsWith("!")) {

      // if the length of the command is 3 or greater words or operators
      // such as >, >>
      if (lineArray.length >= 3) {
        // if it is not an echo redirect command and the last two parts
        // of the command is a redirect operator and a file
        if (lineArray[(lineArray.length) - 2].contains(">")
            && !commandName.equals("echo")) {
          // extract the command portion
          String commandString = "";
          String[] commandArray =
              (Arrays.copyOfRange(lineArray, 0, (lineArray.length) - 2));
          for (String element : commandArray) {
            commandString = commandString + " " + element;
          }
          // perform the command and save the return
          cmdReturn = JShell.executeLine(commandString.trim());

          // extract the redirect portion
          String redirect = "";
          String[] redirectArray = (Arrays.copyOfRange(lineArray,
              (lineArray.length) - 2, (lineArray.length)));
          for (String element : redirectArray) {
            redirect = redirect + " " + element;
          }
          // redirect the command output
          echoCommand
              .performUserCommand("echo \"" + cmdReturn + "\" " + redirect.trim());
          // return an empty string since redirect has no return
          return "";

        }
      }
      // Exit Command
      if (commandName.equals("exit")) {
        cmdReturn = exitCommand.performUserCommand(line);

        // Mkdir Command
      } else if (commandName.equals("mkdir")) {
        cmdReturn = mkdirCommand.performUserCommand(line);

        // Cd Command
      } else if (commandName.equals("cd")) {
        cmdReturn = cdCommand.performUserCommand(line);

        // Ls Command
      } else if (commandName.equals("ls")) {
        cmdReturn = lsCommand.performUserCommand(line);

        // Pwd Command
      } else if (commandName.equals("pwd")) {
        cmdReturn = pwdCommand.performUserCommand(line);

        // History Command
      } else if (commandName.equals("history")) {
        cmdReturn = historyCommand.performUserCommand(line);

        // Cat Command
      } else if (commandName.equals("cat")) {
        cmdReturn = catCommand.performUserCommand(line);

        // Echo Command
      } else if (commandName.equals("echo")) {
        cmdReturn = echoCommand.performUserCommand(line);

        // Pushd Command
      } else if (commandName.equals("pushd")) {
        cmdReturn = pushdCommand.performUserCommand(line);

        // Popd Command
      } else if (commandName.equals("popd")) {
        cmdReturn = popdCommand.performUserCommand(line);

        // Man Command
      } else if (commandName.equals("man")) {
        cmdReturn = manCommand.performUserCommand(line);

      } else if (commandName.equals("mv")) {
        cmdReturn = mvCommand.performUserCommand(line);

      } else if (commandName.equals("cp")) {
        cmdReturn = cpCommand.performUserCommand(line);

      } else if (commandName.equals("grep")) {
        cmdReturn = grepCommand.performUserCommand(line);

      } else if (commandName.startsWith("!")) {
        cmdReturn = redoCommand.performUserCommand(line);

      } else if (commandName.equals("get")) {
        cmdReturn = getCommand.performUserCommand(line);
      }
      return (cmdReturn);
    }

    else {
      throw (invalidInput);
    }

  }
}
