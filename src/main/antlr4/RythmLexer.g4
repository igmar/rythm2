/*
 * Rythm engine grammar
 * (c) 2016 Igmar Palsenberg <igmar@palsenberg.com>
 *
*/

lexer grammar RythmLexer;

@lexer::header {
package org.rythmengine.internal.parser;
import org.rythmengine.internal.debug.AntlrDebug;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
}

@lexer::members {
    int block_nesting = 0;
    int curly_nesting = 0;
    int parenthesis_nesting = 0;
    boolean coe_started = false;
    boolean return_if_started = false;
    boolean args_started = false;
    List<Token> tokens = new ArrayList<>();

    public void emit(Token token) {
        super.emit(token);

        if (token.getChannel() == Token.DEFAULT_CHANNEL) {
            tokens.add(token);
        }
    }

    private boolean previous_is_parenthesis_close() {
        if (tokens.size() == 0) {
            return false;
        }
        for (int i = tokens.size() - 1; i >= 0; i--) {
            Token t = tokens.get(i);
            if (t.getType() == RythmLexer.CONTENT) {
                if (!StringUtils.isWhitespace(t.getText())) {
                    return false;
                }
                continue;
            }
            if (t.getType() == RythmLexer.CURLY_CLOSE) {
                return true;
            } else {
                break;
            }
        }
        return false;
    }

    private boolean previous_is_else() {
        if (tokens.size() == 0) {
            return false;
        }

        Token t = tokens.get(tokens.size() - 1);
        if (t.getType() == RythmLexer.ELSE) {
            return true;
        }
        return false;
    }
}

channels { TemplateComment, JavaCode }

DOUBLE_AT:                  '@@'                        ;
AT:                         '@'                         -> mode(OPERATOR);
CURLY_CLOSE:                '}'                         { block_nesting > 0 && block_nesting >= curly_nesting }? { curly_nesting--; block_nesting--; };
NORMAL_ELSE:                'else'                      { previous_is_parenthesis_close() }? { block_nesting++; tokens.clear(); setType(ELSE); } -> mode(RYTHM);
CONTENT:                    .                           ;

mode OPERATOR;
COE_START:                  '('                         { coe_started = true; } -> mode(RYTHM);
COMMENT_LINE_START:         '//'                        -> mode(LINE_COMMENT);
COMMENT_ML_START:           '*'                         -> mode(MULTILINE_COMMENT);
ARGS_START:                 'args'                      { args_started = true; } -> mode(RYTHM);
IF_BLOCK_START:             'if'                        { block_nesting++; } -> mode(RYTHM);
FOR_BLOCK_START:            'for'                       { block_nesting++; } -> mode(RYTHM);
INCLUDE_START:              'include'                   -> mode(RYTHM);
MACRO_BLOCK_START:          'macro'                     { block_nesting++; } -> mode(RYTHM);
RETURN_START:               'return'                    -> mode(DEFAULT_MODE);
RETURN_IF_START:            'returnIf('                 { return_if_started = true; } -> mode(RYTHM);
OE_START:                   [a-zA-Z$_][a-zA-Z0-9$_]+    { setType(IDENTIFIER); } -> mode(OUTPUT_EXPRESSION);
UOE_START:                  '_'                         -> mode(OUTPUT_EXPRESSION);

mode RYTHM;
COLON:                      ':'                         ;
SEMICOLON:                  ';'                         ;
CURLY_OPEN:                 '{'                         { curly_nesting++; } -> mode(DEFAULT_MODE);
COE_END:                    ')'                         { coe_started && parenthesis_nesting == 0 }? { coe_started = false; } -> mode(DEFAULT_MODE);
RETURN_IF_END:              ')'                         { return_if_started && parenthesis_nesting == 0}? { return_if_started = false; } -> mode(DEFAULT_MODE);
PARENTHESIS_OPEN:           '('                         { parenthesis_nesting++; };
PARENTHESIS_CLOSE:          ')'                         { parenthesis_nesting--; };
BRACKET_OPEN:               '['                         ;
BRACKET_CLOSE:              ']'                         ;
DOUBLE_QUOTE:               '"'                         ;
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
ARGS_END:                   [\r\n]                      { args_started }? { args_started = false; }-> mode(DEFAULT_MODE);
EOL:                        [\r\n]                      -> channel(HIDDEN);
WS:                         [\t ]                       -> channel(HIDDEN);
IDENTIFIER:                 [a-zA-Z$_][a-zA-Z0-9$_]*    ;

mode OUTPUT_EXPRESSION;
OE_ARGS_PARENTHESIS_OPEN:   '('                         { setType(PARENTHESIS_OPEN); } -> pushMode(OUTPUT_EXPRESSION_ARGS);
OE_COMMA:                   ','                         { setType(COMMA); };
OE_DOT:                     '.'                         { setType(DOT); };
OE_IDENTIFIER:              [a-zA-Z$_][a-zA-Z0-9$_]*    { setType(IDENTIFIER); };
OE_END:                     .                           -> mode(DEFAULT_MODE);

mode OUTPUT_EXPRESSION_ARGS;
OE_ARGS_IDENTIFIER:         [a-zA-Z$_][a-zA-Z0-9$_]*    { setType(IDENTIFIER); };
OE_ARGS_COMMA:              ','                         { setType(COMMA); };
OE_ARGS_WS:                 [ \t\r\n]+                  -> channel(HIDDEN);
OE_ARGS_DOUBLE_QUOTE:       '"'                         { setType(DOUBLE_QUOTE); };
OE_ARGS_PARENTHESIS_CLOSE:  ')'                         { setType(PARENTHESIS_CLOSE); } -> popMode;

mode LINE_COMMENT;
LINE_COMMENT_END:           [\r\n]                      -> mode(DEFAULT_MODE);
COMMENT_LINE:               .                           -> channel(TemplateComment);

mode MULTILINE_COMMENT;
MULTILINE_COMMENT_END:      '*@'                        -> mode(DEFAULT_MODE);
COMMENT_MULTI_LINE:         .                           -> channel(TemplateComment);

mode JAVA_BLOCK;
JAVA_BLOCK_CODE:            .                           -> channel(JavaCode);
