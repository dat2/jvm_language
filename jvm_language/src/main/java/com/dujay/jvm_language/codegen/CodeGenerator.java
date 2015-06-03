package com.dujay.jvm_language.codegen;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.dujay.jvm.attributes.builders.CodeAttributeBuilder;
import com.dujay.jvm.constants.ConstantPool;
import com.dujay.jvm.constants.ConstantPoolBuilder;
import com.dujay.jvm.constants.enums.AccessFlag;
import com.dujay.jvm.file.ClassFile;
import com.dujay.jvm.methods.builders.MethodPoolBuilder;
import com.dujay.jvm_language.utils.Either;

public class CodeGenerator {

  public CodeGenerator() {

  }

  public Either<Void, Exception> generate() {
    try {
      ClassFile f = new ClassFile("Test", AccessFlag.PUBLIC, AccessFlag.SUPER,
          AccessFlag.SYNTHETIC);
      ConstantPoolBuilder cpb = f.makeConstantPoolBuilder();
      MethodPoolBuilder mpb = f.makeMethodPoolBuilder();

      // header info
      cpb
      // this and super class
      .thisClass("Test")
          // TODO should be inferred from the class file name
          .superClass(Object.class)

          // attributes
          .attribute("Code").attribute("LineNumberTable")
          .attribute("LocalVariableTable").source("Hello.jvm");

      // constructor method
      Constructor<Object> c = Object.class.getConstructor();

      // generate constants
      cpb.clazz(Object.class)

      .constructor(c).constructor("this");

      // generate method
      CodeAttributeBuilder init = mpb
          .beginMethod()
          .signature(ConstantPool.uniqueConstructorNTName(), 1, 1,
              AccessFlag.PUBLIC, AccessFlag.SYNTHETIC)

          .beginCode().aload_0().invokespecial(c).vreturn();

      // main method
      Method printlnS = PrintStream.class.getMethod("println", String.class);
      Method printlnB = PrintStream.class.getMethod("println", boolean.class);
      Method printlnC = PrintStream.class.getMethod("println", char.class);
      Method printlnI = PrintStream.class.getMethod("println", int.class);
      Method printlnF = PrintStream.class.getMethod("println", float.class);
      Method printlnL = PrintStream.class.getMethod("println", long.class);
      Method printlnD = PrintStream.class.getMethod("println", double.class);
      Method printlnO = PrintStream.class.getMethod("println", Object.class);

      Field out = System.class.getField("out");

      // main method constants
      cpb.literal("s", "Hello World").literal("i", 27).literal("f", 3.14f)
          .literal("l", 12345678910L).literal("d", 4.0)

          .clazz(System.class).clazz(PrintStream.class)

          .field(out)

          .method(printlnS).method(printlnB).method(printlnC).method(printlnI)
          .method(printlnF).method(printlnL).method(printlnD).method(printlnO)

          // TODO make facade objects for both reflection and constructed
          // classes
          .method("this", "main", Void.class, (new String[] {}).getClass());

      // main method code
      // Hello.main(String[] args)
      CodeAttributeBuilder main = mpb
          .beginMethod()
          .signature(ConstantPool.uniqueNTName("this", "main"), 3, 1,
              AccessFlag.PUBLIC, AccessFlag.STATIC, AccessFlag.SYNTHETIC)

          .beginCode()
          // System.out.println("Hello World");
          .getstatic(out).ldc("s").invokevirtual(printlnS)
          // System.out.println(integer);
          .getstatic(out).ldc("i").invokevirtual(printlnI)
          // System.out.println(float);
          .getstatic(out).ldc("f").invokevirtual(printlnF)
          // System.out.println(long);
          .getstatic(out).ldc2_w("l").invokevirtual(printlnL)
          // System.out.println(double);
          .getstatic(out).ldc2_w("d").invokevirtual(printlnD)
          // System.out.println(true);
          .getstatic(out).iconst_1().invokevirtual(printlnB)
          // System.out.println(false);
          .getstatic(out).iconst_0().invokevirtual(printlnB)
          // System.out.println('c');
          .getstatic(out).bipush('c').invokevirtual(printlnC)
          // System.out.println(new Object());
          .getstatic(out).nnew(Object.class).dup()
          .invokespecial(Object.class.getConstructor()).invokevirtual(printlnO)
          // return;
          .vreturn();

      // build constant pool
      cpb
      // finally generate indices
      .index()
      // finally return the constant pool
          .build();

      // build methods
      init
      // patches addresses and adds the code attribute to the method info
      .build()
      // adds it to the method pool finally
          .endMethod();

      main
      // patches addresses and adds the code attribute to the method info
      .build()
      // adds it to the method pool finally
          .endMethod();

      mpb.build();

      // save the file
      f.save();

    } catch (Exception e) {
      return Either.right(e);
    }
    
    return Either.left(null);
  }
}
