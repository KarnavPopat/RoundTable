package lexer;

import java.util.List;

interface RoTalCallable {
	int arity();
	Object call(Interpreter interpreter, List<Object> arguments);
}
