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
    : elements*
    ;

elements
    : DOUBLE_AT
    | CONTENT
    | comment | javaBlock | args
    ;

comment
    : AT COMMENT_LINE_START COMMENT_LINE* LINE_COMMENT_END
    | AT COMMENT_ML_START COMMENT_MULTI_LINE* MULTILINE_COMMENT_END
    ;

javaBlock
    : AT JAVA_BLOCK_START JAVA_BLOCK_CODE* JAVA_BLOCK_END
    ;

args
    : AT ARGS_START templateArgument (COMMA templateArgument)*
    ;

qualifiedName
    : IDENTIFIER (DOT IDENTIFIER)*
    ;

templateArgument
    : qualifiedName IDENTIFIER
    ;
