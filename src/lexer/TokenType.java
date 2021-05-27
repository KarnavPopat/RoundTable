package lexer;

enum TokenType {
	// Single-character tokens.
	LEFT_PAR, RIGHT_PAR, LEFT_BRACE, RIGHT_BRACE,
	COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,

	// One or two character tokens.
	BANG, BANG_EQUAL,
	EQUAL, EQUAL_EQUAL,
	GREATER, GREATER_EQUAL,
	LESS, LESS_EQUAL,

	// Literals.
	IDENTIFIER, STRING, NUMBER,

	// Keywords.
	AND, CLASS, ELSE, FALSE, FUN, FOR, EXTENDS, IF, NIL, OR,
	PRINT, RETURN, SUPER, SELF, TRUE, LET, WHILE,

	EOF
}

/* package statement: i was born in the package, molded by the package
* class start statement: classname, i believe this is the beginning of a beautiful friendship
* variable init: RELEASE THE var name
* index overflow error: AND JESUS WEPT, FOR THERE WERE NO MORE INDICES TO CONQUER
* class end:  no mr classname, i expect you to DIE */
