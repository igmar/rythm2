/*
 * Rythm engine grammar
 * (c) 2016 Igmar Palsenberg <igmar@palsenberg.com>
 *
*/

lexer grammar RythmLexer;

@lexer::header {
package org.rythmengine.internal.parser;
}

@lexer::members {
    int block_nesting = 0;
    int curly_nesting = 0;
    int parenthesis_nesting = 0;
    boolean coe_started = false;
}

channels { TemplateComment, TemplateData, JavaCode }

AT:                         '@'                         -> mode(OPERATOR);
DOUBLE_AT:                  '@@'                        -> channel(TemplateData);
CURLY_CLOSE:                '}'                         { block_nesting > 0 && block_nesting >= curly_nesting }? { curly_nesting--; block_nesting--; };
CONTENT:                    .                           -> channel(TemplateData);

mode OPERATOR;
COE_START:                  '('                         { coe_started = true; } -> mode(RYTHM);
COMMENT_LINE_START:         '//'                        -> mode(LINE_COMMENT);
COMMENT_ML_START:           '*'                         -> mode(MULTILINE_COMMENT);
ARGS_START:                 'args'                      -> mode(RYTHM);
IF_BLOCK_START:             'if'                        { block_nesting++; } -> mode(RYTHM);
FOR_BLOCK_START:            'for'                       { block_nesting++; } -> mode(RYTHM);
OE_START:                   [a-zA-Z][a-zA-Z0-9$_]*      -> mode(OUTPUT_EXPRESSION);

mode RYTHM;
COLON:                      ':'                         ;
SEMICOLON:                  ';'                         ;
CURLY_OPEN:                 '{'                         { curly_nesting++; } -> mode(DEFAULT_MODE);
COE_END:                    ')'                         { coe_started && parenthesis_nesting == 0 }? { coe_started = false; } -> mode(DEFAULT_MODE);
PARENTHESIS_OPEN:           '('                         { parenthesis_nesting++; };
PARENTHESIS_CLOSE:          ')'                         { parenthesis_nesting--; };
ELSE:                       'else'                      ;
RETURN:                     'return'                    ;
BOOL_TRUE:                  'true'                      ;
BOOL_FALSE:                 'false'                     ;
INTEGER:                    'int'                       ;
LONG:                       'long'                      ;
EQUALS:                     '='                         ;
NULL:                       'null'                      ;
INCREMENT:                  '++'                        ;
DECREMENT:                  '--'                        ;
INTEGER_TYPE_SUFFIX:        [lL]                        ;
PLUS:                       '+'                         ;
MINUS:                      '-'                         ;
NEGATE:                     '!'                         ;
COMPLEMENT:                 '~'                         ;
COMMA:                      ','                         ;
DOT:                        '.'                         ;
GT:                         '>'                         ;
LT:                         '<'                         ;
GTE:                        '>='                        ;
LTE:                        '<='                        ;
AND:                        '&&'                        ;
OR:                         '||'                        ;
BAND:                       '&'                         ;
BOR:                        '|'                         ;
XOR:                        '^'                         ;
EQUALTO:                    '=='                        ;
NOTEQUALTO:                 '!='                        ;
ADDASSIGN:                  '+='                        ;
MINUSASSIGN:                '-='                        ;
MULTASSIGN:                 '*='                        ;
DIVASSIGN:                  '/='                        ;
ANDASSIGN:                  '&='                        ;
ORASSIGN:                   '|='                        ;
XORASSIGN:                  '^='                        ;
SHLASSIGN:                  '<<='                       ;
SHRASSIGN:                  '>>='                       ;
SHRZASSIGN:                 '>>>='                      ;
MODASSIGN:                  '%='                        ;
ZERO:                       '0'                         ;
NONZERO:                    [1-9]                       ;
HEXPREFIX:                  [xX]                        ;
HEXDIGIT:                   [0-9a-fA-F]                 ;
UNDERSCORE:                 '_'                         ;
EOL:                        [\r\n]                      -> channel(HIDDEN);
WS:                         [\t ]                       -> channel(HIDDEN);
IDENTIFIER:                 [a-zA-Z$_][a-zA-Z0-9$_]*    ;

mode OUTPUT_EXPRESSION;
OE_PARENTHESIS_OPEN:        '('                         ;
OE_PARENTHESIS_CLOSE:       ')'                         ;
OE_COMMA:                   ','                         ;
OE_DOT:                     '.'                         ;
OE_IDENTIFIER:              [a-zA-Z$_][a-zA-Z0-9$_]*    ;
OE_END:                     [ \t\r\n]+                  -> mode(DEFAULT_MODE);

mode LINE_COMMENT;
LINE_COMMENT_END:           [\r\n]                      -> mode(DEFAULT_MODE);
COMMENT_LINE:               .                           -> channel(TemplateComment);

mode MULTILINE_COMMENT;
MULTILINE_COMMENT_END:      '*@'                        -> mode(DEFAULT_MODE);
COMMENT_MULTI_LINE:         .                           -> channel(TemplateComment);

mode JAVA_BLOCK;
JAVA_BLOCK_CODE:            .                           -> channel(JavaCode);
