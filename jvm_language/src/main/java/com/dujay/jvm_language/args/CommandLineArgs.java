package com.dujay.jvm_language.args;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

/**
 * This class stores all the arguments that the user supplies from the command line.
 * 
 * It lets you get these options from anywhere easily.
 * @author nick
 */
public class CommandLineArgs {
  
  /**
   * Parameters
   */
  
  /**
   * Run the compiler on many input files.
   * 
   * eg. jvm_language [a.jvm b.jvm c.jvm ...]
   */
  @Parameter(description="Input files")
  private List<String> files = new ArrayList<String>();
  
  /**
   * Show the parse result.
   */
  @Parameter(names={"-dt", "--dump-tree"}, description="Show the parse tree result.")
  private boolean dumpTree = true;
  
  /**
   * Class stuff
   */
  private JCommander commander;
  
  public CommandLineArgs() {
    commander = new JCommander(this);
    
    commander.setProgramName("jvm_language");
  }
  
  /**
   * Actually get the options from a list of args.
   * @param argv
   */
  public void parse(String[] argv) {
    commander.parse(argv);
  }
  
  /**
   * Print the program usage to the console.
   */
  public void usage() {
    commander.usage();
  }
  
  /**
   * Getters and setters
   */
  public List<String> getFiles() {
    return files;
  }

  public boolean getDumpTree() {
    return dumpTree;
  }
}
