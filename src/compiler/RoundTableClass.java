package compiler;

import java.util.List;
import java.util.Map;

class RoundTableClass implements RoundTableCallable {
	final String name;
	final RoundTableClass superclass;

	private final Map<String, RoundTableFunction> methods;

	RoundTableClass(String name, RoundTableClass superclass,
	         Map<String, RoundTableFunction> methods) {
		this.superclass = superclass;
		this.name = name;
		this.methods = methods;
	}

	RoundTableFunction findMethod(String name) {
		if (methods.containsKey(name)) {
			return methods.get(name);
		}

		if (superclass != null) {
			return superclass.findMethod(name);
		}

		return null;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public Object call(Interpreter interpreter,
	                   List<Object> arguments) {
		RoundTableInstance instance = new RoundTableInstance(this);
		RoundTableFunction initializer = findMethod("init");
		if (initializer != null) {
			initializer.bind(instance).call(interpreter, arguments);
		}
		return instance;
	}

	@Override
	public int arity() {
		RoundTableFunction initializer = findMethod("init");
		if (initializer == null) return 0;
		return initializer.arity();
	}
}
