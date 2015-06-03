package com.dujay.jvm_language.parser;

import java.io.IOException;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;

import com.dujay.jvm_language.utils.Either;

public class JvmLanguageParser {
  public JvmLanguageParser() {
    
  }
  
  public Either<ParserRuleContext,Exception> parse(String file) {
    Lexer lexer;
    try {
      lexer = new ExprLexer(new ANTLRFileStream(file));
      
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      
      ExprParser parser = new ExprParser(tokens);
      
      ParserRuleContext tree = parser.prog();
      tree.inspect(parser);
      
      return Either.left(tree);
    } catch (IOException e) {
      return Either.right(e);
    }
  }
}
