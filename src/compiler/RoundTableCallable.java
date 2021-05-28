package compiler;

import java.util.List;

interface RoundTableCallable {
	int arity();
	Object call(Interpreter interpreter, List<Object> arguments);
}
