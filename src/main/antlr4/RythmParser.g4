/*
 * Rythm engine grammar
 * (c) 2016 Igmar Palsenberg <igmar@palsenberg.com>
 *
*/
parser grammar RythmParser;

@header {
package org.rythmengine.internal.parser;
import org.rythmengine.internal.IResourceLoader;
}

options { tokenVocab=RythmLexer; }

@parser::members {
    private IResourceLoader resourceLoader;
    private List<String> args = new ArrayList<>();
    private List<String> macros = new ArrayList<>();

    public void setResourceLoader(IResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}

template
    : elements*
    ;

elements
    : DOUBLE_AT
    | CONTENT
    | comment | javaBlock | args | flow_if | flow_for | outputExpression | flow_return | macro | include
    ;

flow_if
    : AT IF_BLOCK_START boolExpression block (ELSE block)?
    ;

flow_for
    : AT FOR_BLOCK_START forExpression block
    ;

outputExpression
    : AT OE_START DOT canonicalName OE_END
    | AT OE_START DOT canonicalName PARENTHESIS_OPEN methodArguments* PARENTHESIS_CLOSE OE_END
    | AT COE_START IDENTIFIER DOT canonicalName PARENTHESIS_OPEN methodArguments* PARENTHESIS_CLOSE COE_END
    ;

canonicalName
    : IDENTIFIER (DOT IDENTIFIER)*
    ;

flow_return
    : AT RETURN_START
    | AT RETURN_IF_START expression RETURN_IF_END
    ;

macro
    : AT MACRO_BLOCK_START PARENTHESIS_OPEN DOUBLE_QUOTE? canonicalName DOUBLE_QUOTE? PARENTHESIS_CLOSE block
    ;

include
    : AT INCLUDE_START PARENTHESIS_OPEN DOUBLE_QUOTE? canonicalName DOUBLE_QUOTE? PARENTHESIS_CLOSE
    ;

methodArguments
    : IDENTIFIER (COMMA IDENTIFIER)*
    ;

block
    : CURLY_OPEN elements* CURLY_CLOSE
    ;

forExpression
    : PARENTHESIS_OPEN qualifiedName IDENTIFIER COLON IDENTIFIER PARENTHESIS_CLOSE
    | PARENTHESIS_OPEN integralType variableDeclarator SEMICOLON expression SEMICOLON expression PARENTHESIS_CLOSE
    ;


variableDeclarator
    : IDENTIFIER EQUALS expression
    ;

integralType
    : LONG
    | INTEGER
    ;

boolExpression
    : PARENTHESIS_OPEN expression PARENTHESIS_CLOSE
    ;

expression
    : primary
    | expression incDecOperator
    | prefixOperator expression
    | expression ( LT LT | GT GT GT | GT GT ) expression
    | expression ( LTE | GTE | LT | GT ) expression
    | expression ( EQUALTO | NOTEQUALTO ) expression
    | expression AND expression
    | expression OR expression
    | expression BAND expression
    | expression BOR expression
    | <assoc=right> expression (
       EQUALS
       | ADDASSIGN
       | MINUSASSIGN
       | MULTASSIGN
       | DIVASSIGN
       | ANDASSIGN
       | ORASSIGN
       | XORASSIGN
       | SHLASSIGN
       | SHRASSIGN
       | SHRZASSIGN
    ) expression
    ;

incDecOperator
    : INCREMENT
    | DECREMENT
    ;

prefixOperator
    : PLUS
    | MINUS
    | INCREMENT
    | DECREMENT
    | NEGATE
    | COMPLEMENT
    ;

primary
    : literal
    | IDENTIFIER
    ;

literal
    : integerLiteral
    | booleanLiteral
    | NULL
    ;

integerLiteral
    : decimalIntegerLiteral
    | hexIntegerLiteral
    ;

booleanLiteral
    : BOOL_TRUE
    | BOOL_FALSE
    ;

decimalIntegerLiteral
    : decimalNumeral integerTypeSuffix?
    ;

decimalNumeral
    : ZERO
    | NONZERO (digits? | underScores digits)
    ;

digits
    : digit (digitOrUnderscore* digit)?
    ;

digitOrUnderscore
    : digit
    | UNDERSCORE
    ;

digit
    : ZERO
    | NONZERO
    ;

underScores
    : UNDERSCORE+
    ;

hexIntegerLiteral
    : hexNumeral integerTypeSuffix?
    ;

hexNumeral
    : ZERO HEXPREFIX hexDigits
    ;

hexDigits
    :
    ;

integerTypeSuffix
    : INTEGER_TYPE_SUFFIX
    ;

comment
    : AT COMMENT_LINE_START COMMENT_LINE* LINE_COMMENT_END
    | AT COMMENT_ML_START COMMENT_MULTI_LINE* MULTILINE_COMMENT_END
    ;

javaBlock
    : AT CURLY_OPEN JAVA_BLOCK_CODE* CURLY_CLOSE
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
