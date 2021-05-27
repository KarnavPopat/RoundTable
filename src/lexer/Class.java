package lexer;

import java.util.List;
import java.util.Map;

class RoTalClass implements RoTalCallable {
	final String name;
	final RoTalClass superclass;

	private final Map<String, RoTalFunction> methods;

	RoTalClass(String name, RoTalClass superclass,
	         Map<String, RoTalFunction> methods) {
		this.superclass = superclass;
		this.name = name;
		this.methods = methods;
	}

	RoTalFunction findMethod(String name) {
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
		RoTalInstance instance = new RoTalInstance(this);
		RoTalFunction initializer = findMethod("init");
		if (initializer != null) {
			initializer.bind(instance).call(interpreter, arguments);
		}
		return instance;
	}

	@Override
	public int arity() {
		RoTalFunction initializer = findMethod("init");
		if (initializer == null) return 0;
		return initializer.arity();
	}
}
