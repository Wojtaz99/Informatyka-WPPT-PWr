import ply.lex as lex


tokens = (
        'PLUS', 'MINUS', 'TIMES', 'DIVIDE', 'MODULO',
        'LPAREN', 'RPAREN', 'NUMBER', 'SEMICOLON', 'COLON', 'COMMA',
        'IF', 'THEN', 'ELSE', 'ENDIF', 'WHILE', 'DO', 'ENDWHILE', 'REPEAT', 'UNTIL',
        'FOR', 'FROM', 'TO', 'ENDFOR', 'DOWNTO', 'READ', 'WRITE', 'DECLARE', 'BEGIN', 'END',
        'ASSIGN', 'EQ', 'NEQ', 'LESS', 'MORE', 'LEQ', 'MEQ', 'ID', 'COMMENT_START', 'COMMENT_END'
    )


precedence = (
    ('nonassoc', 'LEQ', 'MEQ', 'MORE', 'LESS'),
    ('left', 'PLUS', 'MINUS'),
    ('left', 'TIMES', 'DIVIDE'),
)


t_ignore = '\t\r '


def t_NEWLINE(t):
    r'\n'
    t.lexer.lineno += 1


def t_error(t):
    if t.lexer.comment is True:
        t.lexer.skip(1)
        return
    else:
        print("Nierozpoznany znak: '" + str(t.value[0]) + "', w linii " + str(t.lexer.lineno))


def t_DECLARE(t):
    r'DECLARE'
    if not t.lexer.comment:
        return t


def t_BEGIN(t):
    r'BEGIN'
    if not t.lexer.comment:
        return t


def t_IF(t):
    r'IF'
    if not t.lexer.comment:
        return t


def t_THEN(t):
    r'THEN'
    if not t.lexer.comment:
        return t


def t_ELSE(t):
    r'ELSE'
    if not t.lexer.comment:
        return t


def t_ENDIF(t):
    r'ENDIF'
    if not t.lexer.comment:
        return t


def t_WHILE(t):
    r'WHILE'
    if not t.lexer.comment:
        return t


def t_DOWNTO(t):
    r'DOWNTO'
    if not t.lexer.comment:
        return t

def t_DO(t):
    r'DO'
    if not t.lexer.comment:
        return t


def t_ENDWHILE(t):
    r'ENDWHILE'
    if not t.lexer.comment:
        return t


def t_FOR(t):
    r'FOR'
    if not t.lexer.comment:
        return t


def t_FROM(t):
    r'FROM'
    if not t.lexer.comment:
        return t


def t_TO(t):
    r'TO'
    if not t.lexer.comment:
        return t


def t_ENDFOR(t):
    r'ENDFOR'
    if not t.lexer.comment:
        return t


def t_READ(t):
    r'READ'
    if not t.lexer.comment:
        return t

def t_REPEAT(t):
    r'REPEAT'
    if not t.lexer.comment:
        return t

def t_UNTIL(t):
    r'UNTIL'
    if not t.lexer.comment:
        return t


def t_WRITE(t):
    r'WRITE'
    if not t.lexer.comment:
        return t


def t_ASSIGN(t):
    r':='
    if not t.lexer.comment:
        return t


def t_PLUS(t):
    r'\+'
    if not t.lexer.comment:
        return t


def t_MINUS(t):
    r'\-'
    if not t.lexer.comment:
        return t


def t_TIMES(t):
    r'\*'
    if not t.lexer.comment:
        return t


def t_DIVIDE(t):
    r'\/'
    if not t.lexer.comment:
        return t


def t_MODULO(t):
    r'\%'
    if not t.lexer.comment:
        return t


def t_NEQ(t):
    r'!='
    if not t.lexer.comment:
        return t


def t_LEQ(t):
    r'<='
    if not t.lexer.comment:
        return t


def t_MEQ(t):
    r'>='
    if not t.lexer.comment:
        return t


def t_EQ(t):
    r'='
    if not t.lexer.comment:
        return t


def t_LESS(t):
    r'<'
    if not t.lexer.comment:
        return t


def t_MORE(t):
    r'>'
    if not t.lexer.comment:
        return t


def t_COLON(t):
    r':'
    if not t.lexer.comment:
        return t


def t_SEMICOLON(t):
    r';'
    if not t.lexer.comment:
        return t

def t_COMMA(t):
    r','
    if not t.lexer.comment:
        return t

def t_LPAREN(t):
    r'\('
    if not t.lexer.comment:
        return t


def t_RPAREN(t):
    r'\)'
    if not t.lexer.comment:
        return t


def t_ID(t):
    r'[_a-z]+'
    if not t.lexer.comment:
        return t


def t_NUMBER(t):
    r'[0-9]+'
    if not t.lexer.comment:
        return t


def t_COMMENT_START(t):
    r'\['
    t.lexer.comment = True


def t_COMMENT_END(t):
    r'\]'
    t.lexer.comment = False


def t_END(t):
    r'END'
    if not t.lexer.comment:
        return t


def get_lexer():
    lexer = lex.lex()
    lexer.comment = False
    return lexer


if __name__ == "__main__":
    with open("test.txt", "r") as file:
        data = file.read()
    lexer = get_lexer()
    lexer.input(data)
    while True:
        tok = lexer.token()
        if not tok:
            break
        print(tok)
