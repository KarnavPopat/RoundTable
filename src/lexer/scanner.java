package lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static lexer.TokenType.*;

class Scanner {
	private final String source;

	private final List<Token> tokens = new ArrayList<>();
	private int start = 0;
	private int current = 0;
	private int line = 1;

	private static final Map<String, TokenType> keywords;

	static {
		keywords = new HashMap<>();
		keywords.put("and",    AND);
		keywords.put("class",  CLASS);
		keywords.put("else",   ELSE);
		keywords.put("false",  FALSE);
		keywords.put("for",    FOR);
		keywords.put("fun",    FUN);
		keywords.put("if",     IF);
		keywords.put("nil",    NIL);
		keywords.put("or",     OR);
		keywords.put("print",  PRINT);
		keywords.put("return", RETURN);
		keywords.put("super",  SUPER);
		keywords.put("this",   THIS);
		keywords.put("true",   TRUE);
		keywords.put("let",    LET);
		keywords.put("while",  WHILE);
	}

	Scanner(String source) {
		this.source = source;
	}

	List<Token> scanTokens() {
		while (!isAtEnd()) {
			start = current;
			scanToken();
		}

		tokens.add(new Token(EOF, "", null, line));
		return tokens;
	}

	private void scanToken() {
		char c = advance();
		switch (c) {
			case '(': addToken(LEFT_PAR); break;
			case ')': addToken(RIGHT_PAR); break;
			case '{': addToken(LEFT_BRACE); break;
			case '}': addToken(RIGHT_BRACE); break;
			case ',': addToken(COMMA); break;
			case '.': addToken(DOT); break;
			case '-': addToken(MINUS); break;
			case '+': addToken(PLUS); break;
			case ';': addToken(SEMICOLON); break;
			case '*': addToken(STAR); break;

			case '!':
				addToken(match('=') ? BANG_EQUAL : BANG);
				break;
			case '=':
				addToken(match('=') ? EQUAL_EQUAL : EQUAL);
				break;
			case '<':
				addToken(match('=') ? LESS_EQUAL : LESS);
				break;
			case '>':
				addToken(match('=') ? GREATER_EQUAL : GREATER);
				break;

			case '/':
				if (match('/')) {
					while (peek() != '\n' && !isAtEnd()) { advance(); }
				} else if (match('*')) {

					while (peek() != '*' && peekNext() != '/' && !isAtEnd()) {
						advance();
					}
					if (peek() == '*' && peekNext() == '/') {
						advance();
						advance();
					}

				} else {
					addToken(SLASH);
				}
				break;

			case '"': string(); break;

			case ' ':
			case '\r':
			case '\t':
				break;

			case '\n': line++; break;

			default:

				if (isDigit(c)) {
					number();
				} else if (isAlpha(c)) {
					identifier();
				} else {
					RoundTable.error(line, "Unexpected character.");
				}

		}
	}

	private boolean isAtEnd() {
		return current >= source.length();
	}

	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	private boolean isAlpha(char c) {
		return (c >= 'a' && c <= 'z') ||
				(c >= 'A' && c <= 'Z') ||
				c == '_';
	}

	private boolean isAlphaNumeric(char c) {
		return isAlpha(c) || isDigit(c);
	}

	private char advance() {
		current++;
		return source.charAt(current - 1);
	}

	private void addToken(TokenType type) {
		addToken(type, null);
	}

	private void addToken(TokenType type, Object literal) {
		String text = source.substring(start, current);
		tokens.add(new Token(type, text, literal, line));
	}

	private boolean match(char expected) {
		if (isAtEnd()) {return false;}
		if (source.charAt(current) != expected) {return false;}

		current++;
		return true;
	}

	private char peek() {
		if (isAtEnd()) {return '\0';}
		return source.charAt(current);
	}

	private char peekNext() {
		if (current + 1 >= source.length()) return '\0';
		return source.charAt(current + 1);
	}

	private void string() {
		while (peek() != '"' && !isAtEnd()) {
			if (peek() == '\n') line++;
			advance();
		}

		if (isAtEnd()) {
			RoundTable.error(line, "Unterminated string.");
			return;
		}

		// The closing ".
		advance();

		// Trim the surrounding quotes.
		String value = source.substring(start + 1, current - 1);
		addToken(STRING, value);
	}

	private void number() {
		while (isDigit(peek())) advance();

		// Look for a fractional part.
		if (peek() == '.' && isDigit(peekNext())) {
			// Consume the "."
			advance();

			while (isDigit(peek())) { advance(); }
		}

		addToken(NUMBER,
				Double.parseDouble(source.substring(start, current)));
	}

	private void identifier() {
		while (isAlphaNumeric(peek())) { advance(); }

		String text = source.substring(start, current);
		TokenType type = keywords.get(text);
		if (type == null) { type = IDENTIFIER; }
		addToken(type);
	}
}
