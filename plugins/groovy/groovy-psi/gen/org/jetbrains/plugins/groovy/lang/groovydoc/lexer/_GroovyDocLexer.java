/* The following code was generated by JFlex 1.7.0-SNAPSHOT tweaked for IntelliJ platform */

/*
 * Copyright 2000-2008 JetBrains s.r.o.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.plugins.groovy.lang.groovydoc.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.7.0-SNAPSHOT
 * from the specification file <tt>/ideadev/tools/lexer/../../community/plugins/groovy/groovy-psi/src/org/jetbrains/plugins/groovy/lang/groovydoc/lexer/gdoc.flex</tt>
 */
public class _GroovyDocLexer implements FlexLexer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int COMMENT_DATA_START = 2;
  public static final int COMMENT_DATA = 4;
  public static final int TAG_DOC_SPACE = 6;
  public static final int PARAM_TAG_SPACE = 8;
  public static final int DOC_TAG_VALUE = 10;
  public static final int DOC_TAG_VALUE_IN_PAREN = 12;
  public static final int DOC_TAG_VALUE_IN_LTGT = 14;
  public static final int INLINE_TAG_NAME = 16;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1,  1,  2,  2,  3,  3,  4,  4,  5,  5,  6,  6,  7,  7, 
     8, 8
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\2\1\1\1\26\1\2\1\1\22\0\1\2\2\0\1\13"+
    "\1\4\3\0\1\11\1\12\1\7\1\0\1\14\1\5\1\3\1\6"+
    "\12\3\1\5\1\0\1\22\1\0\1\23\1\0\1\15\32\4\1\10"+
    "\1\0\1\10\1\0\1\4\1\0\1\17\13\4\1\21\2\4\1\16"+
    "\1\4\1\20\10\4\1\24\1\0\1\25\7\0\1\26\34\0\4\4"+
    "\4\0\1\4\12\0\1\4\4\0\1\4\5\0\27\4\1\0\37\4"+
    "\1\0\u01ca\4\4\0\14\4\16\0\5\4\7\0\1\4\1\0\1\4"+
    "\201\0\5\4\1\0\2\4\2\0\4\4\10\0\1\4\1\0\3\4"+
    "\1\0\1\4\1\0\24\4\1\0\123\4\1\0\213\4\10\0\236\4"+
    "\11\0\46\4\2\0\1\4\7\0\47\4\7\0\1\4\100\0\33\4"+
    "\5\0\3\4\30\0\1\4\24\0\53\4\43\0\2\4\1\0\143\4"+
    "\1\0\1\4\17\0\2\4\7\0\2\4\12\0\3\4\2\0\1\4"+
    "\20\0\1\4\1\0\36\4\35\0\131\4\13\0\1\4\30\0\41\4"+
    "\11\0\2\4\4\0\1\4\5\0\26\4\4\0\1\4\11\0\1\4"+
    "\3\0\1\4\27\0\31\4\107\0\1\4\1\0\13\4\127\0\66\4"+
    "\3\0\1\4\22\0\1\4\7\0\12\4\17\0\7\4\1\0\7\4"+
    "\5\0\10\4\2\0\2\4\2\0\26\4\1\0\7\4\1\0\1\4"+
    "\3\0\4\4\3\0\1\4\20\0\1\4\15\0\2\4\1\0\3\4"+
    "\16\0\4\4\7\0\1\4\11\0\6\4\4\0\2\4\2\0\26\4"+
    "\1\0\7\4\1\0\2\4\1\0\2\4\1\0\2\4\37\0\4\4"+
    "\1\0\1\4\23\0\3\4\20\0\11\4\1\0\3\4\1\0\26\4"+
    "\1\0\7\4\1\0\2\4\1\0\5\4\3\0\1\4\22\0\1\4"+
    "\17\0\2\4\17\0\1\4\23\0\10\4\2\0\2\4\2\0\26\4"+
    "\1\0\7\4\1\0\2\4\1\0\5\4\3\0\1\4\36\0\2\4"+
    "\1\0\3\4\17\0\1\4\21\0\1\4\1\0\6\4\3\0\3\4"+
    "\1\0\4\4\3\0\2\4\1\0\1\4\1\0\2\4\3\0\2\4"+
    "\3\0\3\4\3\0\14\4\26\0\1\4\50\0\1\4\13\0\10\4"+
    "\1\0\3\4\1\0\27\4\1\0\12\4\1\0\5\4\3\0\1\4"+
    "\32\0\2\4\6\0\2\4\43\0\10\4\1\0\3\4\1\0\27\4"+
    "\1\0\12\4\1\0\5\4\3\0\1\4\40\0\1\4\1\0\2\4"+
    "\17\0\2\4\22\0\10\4\1\0\3\4\1\0\51\4\2\0\1\4"+
    "\20\0\1\4\21\0\2\4\30\0\6\4\5\0\22\4\3\0\30\4"+
    "\1\0\11\4\1\0\1\4\2\0\7\4\72\0\60\4\1\0\2\4"+
    "\13\0\10\4\72\0\2\4\1\0\1\4\2\0\2\4\1\0\1\4"+
    "\2\0\1\4\6\0\4\4\1\0\7\4\1\0\3\4\1\0\1\4"+
    "\1\0\1\4\2\0\2\4\1\0\4\4\1\0\2\4\11\0\1\4"+
    "\2\0\5\4\1\0\1\4\25\0\4\4\40\0\1\4\77\0\10\4"+
    "\1\0\44\4\33\0\5\4\163\0\53\4\24\0\1\4\20\0\6\4"+
    "\4\0\4\4\3\0\1\4\3\0\2\4\7\0\3\4\4\0\15\4"+
    "\14\0\1\4\21\0\46\4\1\0\1\4\5\0\1\4\2\0\53\4"+
    "\1\0\u014d\4\1\0\4\4\2\0\7\4\1\0\1\4\1\0\4\4"+
    "\2\0\51\4\1\0\4\4\2\0\41\4\1\0\4\4\2\0\7\4"+
    "\1\0\1\4\1\0\4\4\2\0\17\4\1\0\71\4\1\0\4\4"+
    "\2\0\103\4\45\0\20\4\20\0\125\4\14\0\u026c\4\2\0\21\4"+
    "\1\0\32\4\5\0\113\4\3\0\3\4\17\0\15\4\1\0\4\4"+
    "\16\0\22\4\16\0\22\4\16\0\15\4\1\0\3\4\17\0\64\4"+
    "\43\0\1\4\3\0\2\4\103\0\130\4\10\0\51\4\1\0\1\4"+
    "\5\0\106\4\12\0\35\4\63\0\36\4\2\0\5\4\13\0\54\4"+
    "\25\0\7\4\70\0\27\4\11\0\65\4\122\0\1\4\135\0\57\4"+
    "\21\0\7\4\67\0\36\4\15\0\2\4\12\0\54\4\32\0\44\4"+
    "\51\0\3\4\12\0\44\4\153\0\4\4\1\0\4\4\3\0\2\4"+
    "\11\0\300\4\100\0\u0116\4\2\0\6\4\2\0\46\4\2\0\6\4"+
    "\2\0\10\4\1\0\1\4\1\0\1\4\1\0\1\4\1\0\37\4"+
    "\2\0\65\4\1\0\7\4\1\0\1\4\3\0\3\4\1\0\7\4"+
    "\3\0\4\4\2\0\6\4\4\0\15\4\5\0\3\4\1\0\7\4"+
    "\53\0\1\26\1\26\25\0\2\4\23\0\1\4\34\0\1\4\15\0"+
    "\1\4\20\0\15\4\3\0\33\4\107\0\1\4\4\0\1\4\2\0"+
    "\12\4\1\0\1\4\3\0\5\4\6\0\1\4\1\0\1\4\1\0"+
    "\1\4\1\0\4\4\1\0\13\4\2\0\4\4\5\0\5\4\4\0"+
    "\1\4\21\0\51\4\u0a77\0\57\4\1\0\57\4\1\0\205\4\6\0"+
    "\4\4\3\0\2\4\14\0\46\4\1\0\1\4\5\0\1\4\2\0"+
    "\70\4\7\0\1\4\20\0\27\4\11\0\7\4\1\0\7\4\1\0"+
    "\7\4\1\0\7\4\1\0\7\4\1\0\7\4\1\0\7\4\1\0"+
    "\7\4\120\0\1\4\u01d5\0\3\4\31\0\11\4\7\0\5\4\2\0"+
    "\5\4\4\0\126\4\6\0\3\4\1\0\132\4\1\0\4\4\5\0"+
    "\51\4\3\0\136\4\21\0\33\4\65\0\20\4\u0200\0\u19b6\4\112\0"+
    "\u51cd\4\63\0\u048d\4\103\0\56\4\2\0\u010d\4\3\0\20\4\12\0"+
    "\2\4\24\0\57\4\20\0\31\4\10\0\120\4\47\0\11\4\2\0"+
    "\147\4\2\0\4\4\1\0\4\4\14\0\13\4\115\0\12\4\1\0"+
    "\3\4\1\0\4\4\1\0\27\4\25\0\1\4\7\0\64\4\16\0"+
    "\62\4\76\0\6\4\3\0\1\4\16\0\34\4\12\0\27\4\31\0"+
    "\35\4\7\0\57\4\34\0\1\4\60\0\51\4\27\0\3\4\1\0"+
    "\10\4\24\0\27\4\3\0\1\4\5\0\60\4\1\0\1\4\3\0"+
    "\2\4\2\0\5\4\2\0\1\4\1\0\1\4\30\0\3\4\2\0"+
    "\13\4\7\0\3\4\14\0\6\4\2\0\6\4\2\0\6\4\11\0"+
    "\7\4\1\0\7\4\221\0\43\4\35\0\u2ba4\4\14\0\27\4\4\0"+
    "\61\4\u2104\0\u016e\4\2\0\152\4\46\0\7\4\14\0\5\4\5\0"+
    "\1\4\1\0\12\4\1\0\15\4\1\0\5\4\1\0\1\4\1\0"+
    "\2\4\1\0\2\4\1\0\154\4\41\0\u016b\4\22\0\100\4\2\0"+
    "\66\4\50\0\15\4\66\0\2\4\30\0\3\4\31\0\1\4\6\0"+
    "\5\4\1\0\207\4\7\0\1\4\34\0\32\4\4\0\1\4\1\0"+
    "\32\4\13\0\131\4\3\0\6\4\2\0\6\4\2\0\6\4\2\0"+
    "\3\4\3\0\2\4\3\0\2\4\31\0\14\4\1\0\32\4\1\0"+
    "\23\4\1\0\2\4\1\0\17\4\2\0\16\4\42\0\173\4\105\0"+
    "\65\4\u010b\0\35\4\3\0\61\4\57\0\37\4\21\0\33\4\65\0"+
    "\36\4\2\0\44\4\4\0\10\4\1\0\5\4\52\0\236\4\u0362\0"+
    "\6\4\2\0\1\4\1\0\54\4\1\0\2\4\3\0\1\4\2\0"+
    "\27\4\252\0\26\4\12\0\32\4\106\0\70\4\6\0\2\4\100\0"+
    "\1\4\17\0\4\4\1\0\3\4\1\0\33\4\54\0\35\4\203\0"+
    "\66\4\12\0\26\4\12\0\23\4\215\0\111\4\u03ba\0\65\4\113\0"+
    "\55\4\40\0\31\4\32\0\44\4\134\0\60\4\16\0\4\4\u04bb\0"+
    "\53\4\u0955\0\u036f\4\221\0\143\4\u0b9d\0\u042f\4\u33d1\0\u0239\4\u04c7\0"+
    "\105\4\13\0\1\4\102\0\15\4\u4060\0\2\4\u23fe\0\125\4\1\0"+
    "\107\4\1\0\2\4\2\0\1\4\2\0\2\4\2\0\4\4\1\0"+
    "\14\4\1\0\1\4\1\0\7\4\1\0\101\4\1\0\4\4\2\0"+
    "\10\4\1\0\7\4\1\0\34\4\1\0\4\4\1\0\5\4\1\0"+
    "\1\4\3\0\7\4\1\0\u0154\4\2\0\31\4\1\0\31\4\1\0"+
    "\37\4\1\0\31\4\1\0\37\4\1\0\31\4\1\0\37\4\1\0"+
    "\31\4\1\0\37\4\1\0\31\4\1\0\10\4\u1634\0\4\4\1\0"+
    "\33\4\1\0\2\4\1\0\1\4\2\0\1\4\1\0\12\4\1\0"+
    "\4\4\1\0\1\4\1\0\1\4\6\0\1\4\4\0\1\4\1\0"+
    "\1\4\1\0\1\4\1\0\3\4\1\0\2\4\1\0\1\4\2\0"+
    "\1\4\1\0\1\4\1\0\1\4\1\0\1\4\1\0\1\4\1\0"+
    "\2\4\1\0\1\4\2\0\4\4\1\0\7\4\1\0\4\4\1\0"+
    "\4\4\1\0\1\4\1\0\12\4\1\0\21\4\5\0\3\4\1\0"+
    "\5\4\1\0\21\4\u1144\0\ua6d7\4\51\0\u1035\4\13\0\336\4\u3fe2\0"+
    "\u021e\4\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\u05f0\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\11\0\3\1\1\2\1\3\2\2\1\4\1\5\1\6"+
    "\1\7\1\10\1\11\1\12\1\13\1\14\1\15\1\16"+
    "\1\17\1\12\1\20\1\1\1\0\1\21\1\0\2\22"+
    "\1\23\3\22\1\24";

  private static int [] zzUnpackAction() {
    int [] result = new int[41];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\27\0\56\0\105\0\134\0\163\0\212\0\241"+
    "\0\270\0\317\0\346\0\375\0\317\0\u0114\0\375\0\u012b"+
    "\0\317\0\317\0\u0142\0\u0159\0\u0170\0\u0187\0\u019e\0\317"+
    "\0\317\0\317\0\317\0\317\0\u01b5\0\317\0\u012b\0\u01cc"+
    "\0\317\0\375\0\u01e3\0\u01fa\0\317\0\u0211\0\u0228\0\u023f"+
    "\0\u01e3";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[41];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\6\12\1\13\1\14\17\12\1\15\2\16\4\15\1\17"+
    "\5\15\1\20\6\15\1\21\1\22\1\12\1\15\1\16"+
    "\1\23\4\15\1\17\14\15\1\21\1\22\2\12\2\24"+
    "\4\12\1\14\15\12\1\22\2\12\2\25\4\12\1\14"+
    "\17\12\1\15\2\26\2\27\2\15\1\17\1\27\1\30"+
    "\1\15\1\31\1\32\1\15\4\27\1\33\2\15\1\22"+
    "\2\12\2\16\2\27\2\12\1\14\1\27\1\12\1\34"+
    "\1\12\1\32\1\12\4\27\11\12\1\35\2\12\1\14"+
    "\6\12\4\35\1\12\1\36\12\12\1\14\5\12\1\37"+
    "\11\12\36\0\1\40\25\0\1\41\1\42\20\0\2\16"+
    "\30\0\1\43\11\0\1\44\3\43\7\0\1\23\25\0"+
    "\2\24\25\0\2\25\25\0\2\26\27\0\2\27\3\0"+
    "\1\27\5\0\4\27\10\0\3\35\10\0\4\35\14\0"+
    "\1\45\22\0\3\43\10\0\4\43\10\0\3\43\10\0"+
    "\1\43\1\46\2\43\10\0\3\43\10\0\2\43\1\47"+
    "\1\43\10\0\3\43\10\0\1\43\1\50\2\43\10\0"+
    "\3\43\10\0\3\43\1\51\5\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[598];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String[] ZZ_ERROR_MSG = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\11\0\1\11\2\1\1\11\3\1\2\11\5\1\5\11"+
    "\1\1\1\11\1\1\1\0\1\11\1\0\2\1\1\11"+
    "\4\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[41];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private CharSequence zzBuffer = "";

  /** this buffer may contains the current text array to be matched when it is cheap to acquire it */
  private char[] zzBufferArray;

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the textposition at the last state to be included in yytext */
  private int zzPushbackPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /**
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */

  public _GroovyDocLexer() {
    this((java.io.Reader)null);
  }

  public boolean checkAhead(char c) {
     if (zzMarkedPos >= zzBuffer.length()) return false;
     return zzBuffer.charAt(zzMarkedPos) == c;
  }

  public void goTo(int offset) {
    zzCurrentPos = zzMarkedPos = zzStartRead = offset;
    zzPushbackPos = 0;
    zzAtEOF = false;
  }




  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public _GroovyDocLexer(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x110000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 2152) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }

  public final int getTokenStart(){
    return zzStartRead;
  }

  public final int getTokenEnd(){
    return getTokenStart() + yylength();
  }

  public void reset(CharSequence buffer, int start, int end,int initialState){
    zzBuffer = buffer;
    zzBufferArray = com.intellij.util.text.CharArrayUtil.fromSequenceWithoutCopying(buffer);
    zzCurrentPos = zzMarkedPos = zzStartRead = start;
    zzPushbackPos = 0;
    zzAtEOF  = false;
    zzAtBOL = true;
    zzEndRead = end;
    yybegin(initialState);
  }

  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   *
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {
    return true;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final CharSequence yytext() {
    return zzBuffer.subSequence(zzStartRead, zzMarkedPos);
  }


  /**
   * Returns the character at position <tt>pos</tt> from the
   * matched text.
   *
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch.
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBufferArray != null ? zzBufferArray[zzStartRead+pos]:zzBuffer.charAt(zzStartRead+pos);
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of
   * yypushback(int) and a match-all fallback rule) this method
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  }


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() {
    if (!zzEOFDone) {
      zzEOFDone = true;
    
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public IElementType advance() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    CharSequence zzBufferL = zzBuffer;
    char[] zzBufferArrayL = zzBufferArray;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {

          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
        zzDoEOF();
        return null;
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1: 
            { return GroovyDocTokenTypes.mGDOC_COMMENT_BAD_CHARACTER;
            }
          case 21: break;
          case 2: 
            { yybegin(COMMENT_DATA);
                                                                        return GroovyDocTokenTypes.mGDOC_COMMENT_DATA;
            }
          case 22: break;
          case 3: 
            { return TokenType.WHITE_SPACE;
            }
          case 23: break;
          case 4: 
            { if (checkAhead('@')){
                                                                          yybegin(INLINE_TAG_NAME);
                                                                        }
                                                                        else{
                                                                          yybegin(COMMENT_DATA);
                                                                        }
                                                                        return GroovyDocTokenTypes.mGDOC_INLINE_TAG_START;
            }
          case 24: break;
          case 5: 
            { yybegin(COMMENT_DATA);
                                                                        return GroovyDocTokenTypes.mGDOC_INLINE_TAG_END;
            }
          case 25: break;
          case 6: 
            { return GroovyDocTokenTypes.mGDOC_COMMENT_DATA;
            }
          case 26: break;
          case 7: 
            { if (checkAhead('<') || checkAhead('\"')) {
                                                                          yybegin(COMMENT_DATA);
                                                                        }
                                                                        else if (checkAhead('\u007b') ) {
                                                                          yybegin(COMMENT_DATA); //lbrace -  there's some error in JLex when typing lbrace directly
                                                                        }
                                                                        else {
                                                                          yybegin(DOC_TAG_VALUE);
                                                                        }
                                                                        return TokenType.WHITE_SPACE;
            }
          case 27: break;
          case 8: 
            { yybegin(DOC_TAG_VALUE);
                                                                        return TokenType.WHITE_SPACE;
            }
          case 28: break;
          case 9: 
            { yybegin(COMMENT_DATA);
                                                                        return TokenType.WHITE_SPACE;
            }
          case 29: break;
          case 10: 
            { return GroovyDocTokenTypes.mGDOC_TAG_VALUE_TOKEN;
            }
          case 30: break;
          case 11: 
            { yybegin(DOC_TAG_VALUE_IN_PAREN);
                                                                        return GroovyDocTokenTypes.mGDOC_TAG_VALUE_LPAREN;
            }
          case 31: break;
          case 12: 
            { return GroovyDocTokenTypes.mGDOC_TAG_VALUE_SHARP_TOKEN;
            }
          case 32: break;
          case 13: 
            { return GroovyDocTokenTypes.mGDOC_TAG_VALUE_COMMA;
            }
          case 33: break;
          case 14: 
            { yybegin(DOC_TAG_VALUE_IN_LTGT);
                                                                        return GroovyDocTokenTypes.mGDOC_TAG_VALUE_LT;
            }
          case 34: break;
          case 15: 
            { yybegin(DOC_TAG_VALUE);
                                                                        return GroovyDocTokenTypes.mGDOC_TAG_VALUE_RPAREN;
            }
          case 35: break;
          case 16: 
            { yybegin(COMMENT_DATA);
                                                                        return GroovyDocTokenTypes.mGDOC_TAG_VALUE_GT;
            }
          case 36: break;
          case 17: 
            { return GroovyDocTokenTypes.mGDOC_COMMENT_END;
            }
          case 37: break;
          case 18: 
            { yybegin(TAG_DOC_SPACE);
                                                                        return GroovyDocTokenTypes.mGDOC_TAG_NAME;
            }
          case 38: break;
          case 19: 
            { yybegin(COMMENT_DATA_START);
                                                                        return GroovyDocTokenTypes.mGDOC_COMMENT_START;
            }
          case 39: break;
          case 20: 
            { yybegin(PARAM_TAG_SPACE);
                                                                        return GroovyDocTokenTypes.mGDOC_TAG_NAME;
            }
          case 40: break;
          default:
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
