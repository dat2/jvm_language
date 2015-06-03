package com.dujay.jvm_language.parser;

import java.io.IOException;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;

import com.dujay.jvm_language.args.CommandLineArgs;
import com.dujay.jvm_language.utils.Either;

/**
 * The parser for the language. For now it calls antlr, but the way this is setup is to allow
 * me to design my own parser if I choose to.
 * @author nick
 */
public class JvmLanguageParser {
  
  private CommandLineArgs args;
  public JvmLanguageParser(CommandLineArgs args) {
    this.args = args;
  }
  
  /**
   * Try to parse the tree. IF an exception is called, return Either.right.
   * @param file the filename to parse
   * @return
   */
  public Either<ParserRuleContext,Exception> parse(String file) {
    try {
      // setting up antlr, and calling it on the file
      Lexer lexer = new ExprLexer(new ANTLRFileStream(file));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      ExprParser parser = new ExprParser(tokens);
      ParserRuleContext tree = parser.prog();
      
      // if we want to dump the tree, show it graphically
      if(args.getDumpTree()) {
        tree.inspect(parser);
      }
      
      // return the tree
      return Either.left(tree);
    } catch (IOException e) {
      return Either.right(e);
    }
  }
}
