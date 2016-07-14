/*
 * Rythm engine grammar
 * (c) 2016 Igmar Palsenberg <igmar@palsenberg.com>
 *
*/

lexer grammar RythmLexer;

@lexer::header {
package org.rythmengine.internal.parser;
}

channels { TemplateComment, TemplateData }

COMMENT_LINE_START:         '@//'         -> pushMode(LINE_COMMENT);
COMMENT_ML_START:           '@*'          -> pushMode(MULTILINE_COMMENT);
WS:                         [ \t\r\n]+    -> channel(HIDDEN);
DOUBLE_AT:                  '@@'          ;
ARGS_START:                 '@args'       ;
JAVA_BLOCK_START:           '@{'          -> pushMode(JAVA_BLOCK);
DATA:                       .             -> channel(TemplateData);
COMMA:                      ',';
DOT:                        '.';
JAVALETTER:                 [a-zA-Z$_];
JAVALETTERORDIGIT:          [a-zA-Z0-9$_];


mode LINE_COMMENT;
LINE_COMMENT_END:           [ \t\r\n]+    -> popMode;
COMMENT_LINE:               .             -> channel(TemplateComment);

mode MULTILINE_COMMENT;
MULTILINE_COMMENT_END:      '*@'          -> popMode;
COMMENT_MULTI_LINE:         .             -> channel(TemplateComment);

mode JAVA_BLOCK;
JAVA_BLOCK_END:             '}'           -> popMode;
JAVA_BLOCK_CODE:            .;






