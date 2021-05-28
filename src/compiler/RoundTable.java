package compiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/* TODO: add error handling functions to display erroneous lines */
/* TODO: turn print statement into print function */
/* TODO: implement ternary operators */
/* TODO: allow hexadec, oct, and sci notation numbers */
/* TODO: implement a for each loop */
/* TODO: implement lambda & anonymous functions */
/* TODO: implement do while loops */
/* TODO: string operations */
/* TODO: implement file I/O */

public class RoundTable {

	private static final Interpreter interpreter = new Interpreter();

	public static void main(String[] args) throws IOException {

		if (args.length > 1) {
			System.out.println("Usage: roundtable [script]");
			System.exit(64);
		} else if (args.length == 1) {
			runFile(args[0]);
		} else {
			runPrompt();
		}
	}

	private static void runFile(String path) throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get(path));
		run(new String(bytes, Charset.defaultCharset()));

		// Indicate an error in the exit code.
		if (ErrorHandler.hadError) System.exit(65);
		if (ErrorHandler.hadRuntimeError) System.exit(70);
	}

	private static void runPrompt() throws IOException {
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);

		for (;;) {
			System.out.print("> ");
			String line = reader.readLine();
			if (line == null) {
				break;
			}
			run(line);
			ErrorHandler.hadError = false;
		}
	}

	private static void run(String source) {

		// Scan the source code into tokens with the scanner
		Scanner scanner = new Scanner(source);
		List<Token> tokens = scanner.scanTokens();

		// Parse and compile the source code with the parser
		Parser parser = new Parser(tokens);
		List<Stmt> statements = parser.parse();

		// Stop if there was a syntax error.
		if (ErrorHandler.hadError) return;

		// Resolve errors with the resolver
		Resolver resolver = new Resolver(interpreter);
		resolver.resolve(statements);

		// Stop if there was a resolution error.
		if (ErrorHandler.hadError) return;

		// Interpret the code with the interpreter (at runtime)
		interpreter.interpret(statements);
	}
}
