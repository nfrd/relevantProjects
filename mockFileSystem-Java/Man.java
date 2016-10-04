package commands;

import java.util.Hashtable;

public class Man extends Command {

  // Hashtable instance variable to store all manuals
  private Hashtable<String, String> manual = new Hashtable<String, String>();

  /**
   * Manual constructor that adds the necessary manual strings to the manual
   * hashtable instance variable
   */
  public Man() {
    // All the manual strings added to the manual hashtable

    String mkdirManual = "\nNAME: \n\tmkdir - make directory(ies) \n\n"
        + "SYNOPSIS: \n\tmkdir [DIR ...] \n\n"
        + "DESCRIPTION: \n\tCreate the DIR(s), "
        + "if they do not already exist.\n\tThe DIR(s) may be"
        + " relative to the current working-\n\tdirectory or they may be a"
        + " full path.\n";
    manual.put("mkdir", mkdirManual);

    String cdManual = "\nNAME: \n\t cd - change directory \n\n"
        + "SYNOPSIS: \n\t cd [DIR] \n\n"
        + "DESCRIPTION: \n\tChange the current working directory to the\n"
        + "\tone specified by DIR.\n\n"
        + "\tSpecial Paramters: \n\t\t'/' identifies the root\n "
        + "\t\t'..' identifies the parrent\n"
        + "\t\t'.' identifies the current working directory.\n";
    manual.put("cd", cdManual);

    String exitManual =
        "\nNAME: \n\texit\n\n" + "SYNOPSIS: \n\texit [VOID] \n\n"
            + "DESCRIPTION: \n\tQuits out of the program.\n"
            + "\tTakes no paramters.\n";
    manual.put("exit", exitManual);

    String pwdManual = "\nNAME: \n\tpwd - print working directory \n\n"
        + "SYNOPSIS: \n\tpwd [VOID] \n\n"
        + "DESCRIPTION: \n\tPrints the chain of directories from the\n"
        + "\troot to the current.\n";
    manual.put("pwd", pwdManual);

    String lsManual = "\nNAME: \n\tls - list directory contents \n\n"
        + "SYNOPSIS: \n\tls [-R] [VOID/DIR] \n\n"
        + "DESCRIPTION: \n\tIf no parameter is specified, ls will print\n"
        + "\tthe contents within the current directory.\n"
        + "\tOtherwise if a valid directory/path is specified,\n"
        + "\tls will print the contents in that directory.\n\n"
        + "\tIf the [-r] command is given, ls will recursively\n"
        + "\ttrace the given parameters and print the contents\n"
        + "\twithin them.";
    manual.put("ls", lsManual);

    String pushdManual = "\nNAME: \n\tpushd - push directory \n\n"
        + "SYNOPSIS: \n\tpushd [DIR] \n\n"
        + "DESCRIPTION: \n\tSaves the current working directory and then\n"
        + "\tchanges the current working directory to DIR.\n";
    manual.put("pushd", pushdManual);

    String popdManual = "\nNAME: \n\tpopd - pop directory \n\n"
        + "SYNOPSIS: \n\tpopd [VOID] \n\n"
        + "DESCRIPTION: \n\tChanges the current working directory to the\n"
        + "\tmost recent saved directory (by pushd) and then\n"
        + "\tdeletes that saved directory.\n";
    manual.put("popd", popdManual);

    String historyManual =
        "\nNAME: \n\thistory \n\n" + "SYNOPSIS: \n\thistory [NUM] \n\n"
            + "DESCRIPTION: \n\tPrints a list of previously executed"
            + " commands\n\twith their paramters. The number of previous\n"
            + "\tcommands is specified by NUM.\n";
    manual.put("history", historyManual);

    String catManual = "\nNAME: \n\tcat - print file contents \n\n"
        + "SYNOPSIS: \n\tcat [FILE] \n\n"
        + "DESCRIPTION: \n\tPrint the contents of FILE.\n";
    manual.put("cat", catManual);

    String echoManual =
        "\nNAME: \n\techo - 1.Replace/2.Append file content \n\n"
            + "SYNOPSIS: \n\t1. echo [STRING] [>] [FILE]\n"
            + "\t2. echo [STRING] [>>] [FILE]\n\n"
            + "DESCRIPTION: \n\t1. Relpaces the content of FILE with STRING\n"
            + "\t   (surrounded by double quotes). If FILE is not\n"
            + "\t   specified - print the STRING to the screen.\n"
            + "\t   IF FILE does not exist, create it and add STRING\n\n"
            + "\t2. Perform same as above, but instead of replacing\n"
            + "\t   FILE's content, add to it.\n";
    manual.put("echo", echoManual);

    String manManual =
        "\nNAME: \n\tman - manual \n\n" + "SYNOPSIS: \n\tman [CMD] \n\n"
            + "DESCRIPTION: \n\tDisplays user-level information about the\n"
            + "\tspecified CMD (command).\n";
    manual.put("man", manManual);

    String mvManual = "\nNAME: \n\tmv - move \n\n"
        + "SYNOPSIS: \n\tmv [OLDPATH] [NEWPATH] \n\n"
        + "DESCRIPTION: \n\tMoves the item given in old path to new path\n"
        + "\t(deleting the item from old path).\n";
    manual.put("mv", mvManual);

    String cpManual = "\nNAME: \n\tcp - copy \n\n"
        + "SYNOPSIS: \n\tcp [OLDPATH] [NEWPATH] \n\n"
        + "DESCRIPTION: \n\tCopies the item given in old path to new path\n"
        + "\t(without deleting it from oldpath).\n";
    manual.put("cp", cpManual);

    String getManual =
        "\nNAME: \n\tget - get(URL) \n\n" + "SYNOPSIS: \n\tget [URL] \n\n"
            + "DESCRIPTION: \n\tDownload (text)file from provided URL\n"
            + "\tand save to current directory.\n";
    manual.put("get", getManual);

    String redoManual = "\nNAME: \n\t(!)redo - repeat action \n\n"
        + "SYNOPSIS: \n\t![number] \n\n"
        + "DESCRIPTION: \n\tRepeats the command indexed at the given\n"
        + "[number] (based on history).\n";
    manual.put("redo", redoManual);

    String grepManual = "\nNAME: \n\tgrep - match regex \n\n"
        + "SYNOPSIS: \n\tgrep [-r] [REGEX] [PATH...]\n\n"
        + "DESCRIPTION: \n\tGrep checks each item in the provided list\n"
        + "\tof paths, and prints the lines within files that match the\n"
        + "\tprovided regex. [PATH...] is required to be a list of paths\n"
        + "\tdirected towards files.\n\n"
        + "\tIf the [-r] recursive indictor is present, [PATH...] can\n"
        + "\tcontain items directed towards director(ies). In this case\n"
        + "\tgrep will recursively trace and print the file lines of files\n"
        + "\twithin the director(ies).";
    manual.put("grep", grepManual);
  }

  /**
   * Manual factory method
   * 
   * @return Manual
   */
  public static Man makeManual() {
    return new Man();
  }


  /**
   * This method takes in user input and performs the user command (man)
   * 
   * @param userInput: User's raw input
   * @return a manual string or error
   */
  public String performUserCommand(String userInput) throws Exception {
    // Process the input
    String[] parmArray = super.processInput(userInput);

    // Check if there is only 1 parameter
    if (super.paramCheck(1, parmArray)) {
      String cmdName = parmArray[0];

      // If the key is in the hashtable
      if (this.manual.containsKey(cmdName)) {

        // return the manual
        return (this.manual.get(cmdName));
      } else {

        // Otherwise throw error
        String msg =
            "Invalid, there is no command named " + "\"" + cmdName + "\".";
        throw new Exception(msg);
      }
    } else {
      throw super.wrongParamError("Manual", parmArray);
    }
  }
}
