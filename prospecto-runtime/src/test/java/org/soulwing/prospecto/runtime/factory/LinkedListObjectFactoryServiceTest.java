package org.soulwing.prospecto.runtime.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.factory.ObjectFactory;

/**
 * Unit tests for {@link LinkedListObjectFactoryService}.
 *
 * @author Carl Harris
 */
public class LinkedListObjectFactoryServiceTest {

  private static final Object FACTORY_REF = new Object();

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  ObjectFactory strategy;

  private LinkedListObjectFactoryService service =
      new LinkedListObjectFactoryService();

  @Before
  public void setUp() throws Exception {
    service.append(strategy);
  }

  @Test
  public void testWhenStrategySupports() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(strategy).newInstance(Object.class);
        will(returnValue(FACTORY_REF));
      }
    });

    assertThat(service.newInstance(Object.class),
        is(sameInstance(FACTORY_REF)));
  }

  @Test
  public void testWhenStrategyDoesNotSupport() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(strategy).newInstance(Object.class);
        will(returnValue(null));
      }
    });

    final Object actual = service.newInstance(Object.class);
    assertThat(actual, is(not(nullValue())));
    assertThat(actual, is((not(sameInstance(FACTORY_REF)))));
  }

}
