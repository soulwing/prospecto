/*
 * File created on Mar 17, 2016
 *
 * Copyright (c) 2016 Carl Harris, Jr
 * and others as noted
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.soulwing.prospecto.api.discriminator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

/**
 * Unit tests for {@link SimpleClassNameDiscriminatorStrategy}.
 *
 * @author Carl Harris
 */
public class SimpleClassNameDiscriminatorStrategyTest {

  private SimpleClassNameDiscriminatorStrategy strategy =
      new SimpleClassNameDiscriminatorStrategy();


  @Test
  public void testDefaultConfiguration() throws Exception {
    final Discriminator discriminator = strategy.toDiscriminator(
        MockModel.class, MockModel.class);
    assertThat(strategy.toSubtype(MockModel.class, discriminator)
            .equals(MockModel.class), is(true));
  }

  @Test
  public void testDecapitalizeWhenLowerCase() throws Exception {
    strategy.setDecapitalize(true);
    final Discriminator discriminator = strategy.toDiscriminator(
        MockModel.class, MockModel.class);
    assertThat(discriminator.getValue().toString(), is(equalTo("mockModel")));
    assertThat(strategy.toSubtype(MockModel.class, discriminator)
        .equals(MockModel.class), is(true));
  }

  @Test
  public void testDecapitalizeWhenNotLowerCase() throws Exception {
    strategy.setDecapitalize(true);
    final Discriminator discriminator = strategy.toDiscriminator(
        OTHERModel.class, OTHERModel.class);
    assertThat(discriminator.getValue().toString(), is(equalTo("OTHERModel")));
    assertThat(strategy.toSubtype(OTHERModel.class, discriminator)
        .equals(OTHERModel.class), is(true));
  }

  @Test
  public void testStripPrefix() throws Exception {
    strategy.setPrefix("Prefix");
    final Discriminator discriminator = strategy.toDiscriminator(
        PrefixMockModelSuffix.class, PrefixMockModelSuffix.class);
    assertThat(discriminator.getValue().toString(), is(equalTo("MockModelSuffix")));
    assertThat(strategy.toSubtype(PrefixMockModelSuffix.class, discriminator)
        .equals(PrefixMockModelSuffix.class), is(true));
  }

  @Test
  public void testStripSuffix() throws Exception {
    strategy.setSuffix("Suffix");
    final Discriminator discriminator = strategy.toDiscriminator(
        PrefixMockModelSuffix.class, PrefixMockModelSuffix.class);
    assertThat(discriminator.getValue().toString(), is(equalTo("PrefixMockModel")));
    assertThat(strategy.toSubtype(PrefixMockModelSuffix.class, discriminator)
        .equals(PrefixMockModelSuffix.class), is(true));
  }

  @Test
  public void testKitchenSink() throws Exception {
    strategy.setPrefix("Prefix");
    strategy.setSuffix("Suffix");
    strategy.setDecapitalize(true);
    final Discriminator discriminator = strategy.toDiscriminator(
        PrefixMockModelSuffix.class, PrefixMockModelSuffix.class);
    assertThat(discriminator.getValue().toString(), is(equalTo("mockModel")));
    assertThat(strategy.toSubtype(PrefixMockModelSuffix.class, discriminator)
        .equals(PrefixMockModelSuffix.class), is(true));
  }

  @Test(expected =  IllegalArgumentException.class)
  public void testToDiscriminatorWhenNotSubtype() throws Exception {
    strategy.toDiscriminator(
        MockModel.class, Object.class);
  }

  @Test(expected =  IllegalArgumentException.class)
  public void testToSubtypeWhenNotSubtype() throws Exception {
    strategy.toSubtype(MockModel.class,
        strategy.toDiscriminator(OTHERModel.class, OTHERModel.class));
  }


}

