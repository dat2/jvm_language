package com.dujay.jvm_language;

import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import com.dujay.jvm_language.args.CommandLineArgs;
import com.dujay.jvm_language.codegen.CodeGenerator;
import com.dujay.jvm_language.parser.JvmLanguageParser;
import com.dujay.jvm_language.utils.Either;

/**
 * The main compiler class. This runs parsing, then semantics checking, then code generation.
 */
public class Compiler 
{
  /**
   * Using JCommander args, parse the command line arguments.
   */
  private CommandLineArgs args;
  public Compiler(String[] argv) {
    args = new CommandLineArgs();
    args.parse(argv);
  }
  
  /**
   * Parse the input file and return either the parse result, or an exception.
   * 
   * The first step of compilation.
   * @param file the input file to read from
   * @return an either
   */
  public Either<ParserRuleContext, Exception> parse(String file) {
    JvmLanguageParser parser = new JvmLanguageParser(args);
    return parser.parse(file);
  }
  
  /**
   * Check the semantics of the parse result, and return some value representing
   * the result of the semantics check.
   * 
   * The second step of compilation.
   * @param p the parse result
   * @return a semantically checked result
   */
  public Either<Void,Exception> semantics(ParserRuleContext p) {
    return Either.left(null);
  }
  
  /**
   * Generate the .class file code for the semantically checked input.
   * The third step of compilation.
   * 
   * @param v the semantically checked input
   * @return some result representing successs
   */
  public Either<Void,Exception> generate(Void v) {
    CodeGenerator generator = new CodeGenerator();
    return generator.generate();
  }
  
  /**
   * Do something with an exception. The final step of compilation.
   * @param e
   * @return nothing, but needed to use this as a function ref.
   */
  public Void handleException(Exception e) {
    System.out.println(e);
    return null;
  }
  
  /**
   * Run the compiler given the command line arguments.
   */
  public void run() {
    // this whole block of code compiles multiple files in parallel
    args.getFiles()
      .parallelStream()
       // parse the file (duh)
      .map(this::parse)
      
      // check that the parse result is semantically valid
      // leftMap will change the left result of the either, but if the either is a Right
      // then the exception will carry through here
      .map(x -> x.leftMap(this::semantics))
      
      // similarly, generate from the semantic result and pass through exceptions
      .map(x -> x.leftMap(this::generate))
      
      // at this point, its a list of either<generateResult,exception>
      // either do something with the generation result
      // or handle exceptions called for each file
      .forEach(x -> x.fold(y -> y, this::handleException));;
  }
  
  /**
   * Run the main compiler.
   * @param args
   */
  public static void main( String[] args )
  {
    // turn off verbose logging for all the jvm_backend code
    Logger root = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    root.setLevel(Level.INFO);
    
    // run the compiler on the command line args
    Compiler c = new Compiler(args);
    c.run();
  }
}
