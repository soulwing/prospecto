package org.soulwing.prospecto.runtime.accessor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * An adapter that allows a value type containing an array to be treated as
 * an array.
 *
 * @author Carl Harris
 */
class ArrayCoercion {

  private interface FactoryMethod {
    Object newInstance(Object[] array) throws Exception;
  }

  private final Method coerceMethod;
  private final FactoryMethod factoryMethod;

  private ArrayCoercion(Method coerceMethod, FactoryMethod factoryMethod)
      throws NoSuchMethodException {
    this.coerceMethod = coerceMethod;
    this.factoryMethod = factoryMethod;
  }

  public Object[] toArray(Object source) throws Exception {
    return (Object[]) coerceMethod.invoke(source);
  }

  public Object fromArray(Object[] array) throws Exception {
    return factoryMethod.newInstance(array);
  }

  static ArrayCoercion newInstance(Class<?> targetType,
      Class<?> arrayType) throws NoSuchMethodException {
    return new ArrayCoercion(coerceMethod(targetType, arrayType),
        factoryMethod(targetType, arrayType));
  }

  private static Method coerceMethod(Class<?> targetType, Class<?> arrayType)
      throws NoSuchMethodException {
    final Method method = targetType.getMethod("toArray");
    if (!arrayType.isAssignableFrom(method.getReturnType())) {
      throw new NoSuchMethodException("incompatible return type");
    }
    return method;
  }

  private static FactoryMethod factoryMethod(final Class<?> targetType,
      Class<?> arrayType) throws NoSuchMethodException {

    final Method[] methods = targetType.getMethods();
    for (final Method method : methods) {
      if ((method.getModifiers() & Modifier.STATIC) == 0) continue;
      if (method.getParameterTypes().length != 1) continue;
      if (!method.getParameterTypes()[0].isAssignableFrom(arrayType)) continue;
      if (!targetType.isAssignableFrom(method.getReturnType())) continue;
      return new FactoryMethod() {
        @Override
        public Object newInstance(Object[] array) throws Exception {
          return method.invoke(null, new Object[] { array });
        }
      };
    }

    final Constructor arrayConstructor = targetType.getConstructor(arrayType);
    return new FactoryMethod() {
      @Override
      public Object newInstance(Object[] array) throws Exception {
        return arrayConstructor.newInstance(array);
      }
    };

  }

}
