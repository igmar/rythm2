/*
 * Rythm engine grammar
 * (c) 2016 Igmar Palsenberg <igmar@palsenberg.com>
 *
*/
parser grammar RythmParser;

@header {
package org.rythmengine.internal.parser;
}

options { tokenVocab=RythmLexer; }

template
    : content* EOF
    ;

content
    : comment | javaBlock | args
    ;

comment
    : COMMENT_LINE_START COMMENT_LINE* LINE_COMMENT_END
    | COMMENT_ML_START COMMENT_MULTI_LINE* MULTILINE_COMMENT_END
    ;

javaBlock
    : JAVA_BLOCK_START JAVA_BLOCK_CODE* JAVA_BLOCK_END
    ;

args
    : ARGS_START templateArgument (COMMA templateArgument)*
    ;

identifier
    : JAVALETTER JAVALETTERORDIGIT*
    ;

qualifiedName
    : identifier (DOT identifier)*
    ;

templateArgument
    : qualifiedName identifier
    ;



