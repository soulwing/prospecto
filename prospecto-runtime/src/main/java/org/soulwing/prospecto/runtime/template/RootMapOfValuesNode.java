package org.soulwing.prospecto.runtime.template;

/**
 * A root view node that represents a map of values.
 *
 * @author Carl Harris
 */
public class RootMapOfValuesNode extends ConcreteMapOfValuesNode {

  public RootMapOfValuesNode(String name, String namespace, Class<?> keyType) {
    super(name, namespace, keyType);
  }

}
