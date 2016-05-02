/*
 * File created on Apr 28, 2016
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
package org.soulwing.prospecto.tests.applicator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.ViewInputException;
import org.soulwing.prospecto.api.reference.ReferenceResolver;

/**
 * A test for the applicator's resolve method.
 *
 * @author Carl Harris
 */
public class ReferenceTest extends ViewApplicatorTestBase {


  private static final Object REFERENCE = new Object();

  @Test
  @Ignore
  public void test() throws Exception {
    Assert.fail("not implemented");
  }

  private static class MockReferenceResolver implements ReferenceResolver {

    @Override
    public boolean supports(Class<?> type) {
      return false;
    }

    @Override
    public Object resolve(Class<?> type, ViewEntity reference)
        throws ViewInputException {
      assertThat(reference.get("id", String.class), is(equalTo("mockId")));
      return REFERENCE;
    }

  }

}
