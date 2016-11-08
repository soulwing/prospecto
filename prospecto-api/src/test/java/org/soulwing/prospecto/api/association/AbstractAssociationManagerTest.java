package org.soulwing.prospecto.api.association;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.factory.ObjectFactory;

/**
 * Unit tests for {@link AbstractAssociationManager}.
 *
 * @author Carl Harris
 */
public class AbstractAssociationManagerTest {

  private static final Object OWNER = new Object();

  private static final Object INSTANCE = new Object();

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  ObjectFactory objectFactory;

  @Mock
  ViewEntity viewEntity;

  private MockAssociationManager associationManager =
      new MockAssociationManager();

  @Test
  public void testNewInstance() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(viewEntity).getType();
        will(returnValue(Object.class));
        oneOf(objectFactory).newInstance(Object.class);
        will(returnValue(INSTANCE));
        oneOf(viewEntity).inject(INSTANCE);
      }
    });

    assertThat(
        associationManager.newAssociate(OWNER, viewEntity, objectFactory),
        is(sameInstance(INSTANCE)));
  }

  private static class MockAssociationManager
      extends AbstractAssociationManager<Object, Object> {

    @Override
    public boolean supports(AssociationDescriptor descriptor) {
      return false;
    }

  }

}
