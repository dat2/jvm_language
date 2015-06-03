package com.dujay.jvm_language.args;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class CommandLineArgs {
  
  /**
   * Parameters
   */
  @Parameter(description="Input files")
  private List<String> files = new ArrayList<String>();
  
  /**
   * Class stuff
   */
  private JCommander commander;
  
  public CommandLineArgs() {
    commander = new JCommander(this);
    
    commander.setProgramName("jvm_language");
  }
  public void parse(String[] argv) {
    commander.parse(argv);
  }
  public void usage() {
    commander.usage();
  }
  
  /**
   * Getters and setters
   */
  public List<String> getFiles() {
    return files;
  }
}
