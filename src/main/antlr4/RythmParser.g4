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
    : comment | javablock
    ;

comment
    : COMMENT_LINE_START COMMENT_LINE* LINE_COMMENT_END
    | COMMENT_ML_START COMMENT_MULTI_LINE* MULTILINE_COMMENT_END
    ;

javablock
    : JAVA_BLOCK_START JAVA_BLOCK_CODE* JAVA_BLOCK_END
    ;



