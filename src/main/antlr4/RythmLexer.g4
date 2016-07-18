/*
 * Rythm engine grammar
 * (c) 2016 Igmar Palsenberg <igmar@palsenberg.com>
 *
*/

lexer grammar RythmLexer;

@lexer::header {
package org.rythmengine.internal.parser;
}

channels { TemplateComment, TemplateData, JavaCode }

AT:                         '@'           -> mode(RYTHM);
DOUBLE_AT:                  '@@'          ;
CURLY_OPEN:                 '{'           ;
CURLY_CLOSE:                '}'           ;
CONTENT:                    .             -> channel(TemplateData);

mode RYTHM;
COMMENT_LINE_START:         '//'          -> mode(LINE_COMMENT);
COMMENT_ML_START:           '*'           -> mode(MULTILINE_COMMENT);
ARGS_START:                 'args'        ;
JAVA_BLOCK_START:           '{'           -> mode(JAVA_BLOCK);
IF_BLOCK_START:             'if'          ;
FOR_BLOCK_START:            'for'         ;
PARENTHESIS_OPEN:           '('           ;
PARENTHESIS_CLOSE:          ')'           ;
ELSE:                       'else'        ;
RETURN:                     'return'      ;
EOL:                        [\r\n]        -> channel(HIDDEN);
WS:                         [\t ]         -> channel(HIDDEN);
COMMA:                      ','           ;
DOT:                        '.'           ;
IDENTIFIER:                 [a-zA-Z$_][a-zA-Z0-9$_]*;

mode LINE_COMMENT;
LINE_COMMENT_END:           [\r\n]        -> mode(DEFAULT_MODE);
COMMENT_LINE:               .             -> channel(TemplateComment);

mode MULTILINE_COMMENT;
MULTILINE_COMMENT_END:      '*@'          -> mode(DEFAULT_MODE);
COMMENT_MULTI_LINE:         .             -> channel(TemplateComment);

mode JAVA_BLOCK;
JAVA_BLOCK_END:             '}'           -> mode(DEFAULT_MODE);
JAVA_BLOCK_CODE:            .             -> channel(JavaCode);
