package web.controlevacinacao.validation.util;

import java.util.Map;

public class ReflectUtil {

	private static final Map<Class<?>, Class<?>> primitiveWrapperMap = Map.of(boolean.class, Boolean.class, byte.class, Byte.class, char.class, Character.class, double.class, Double.class, float.class, Float.class, int.class, Integer.class, long.class, Long.class, short.class, Short.class);

	public static boolean isPrimitiveWrapperOf(Class<?> targetClass, Class<?> primitive) {
		if (!primitive.isPrimitive()) {
			throw new IllegalArgumentException("First argument has to be primitive type");
		}
		return primitiveWrapperMap.get(primitive) == targetClass;
	}

	public static boolean isAssignableTo(Class<?> from, Class<?> to) {
		if (to.isAssignableFrom(from)) {
			return true;
		}
		if (from.isPrimitive()) {
			return isPrimitiveWrapperOf(to, from);
		}
		if (to.isPrimitive()) {
			return isPrimitiveWrapperOf(from, to);
		}
		return false;
	}

}
