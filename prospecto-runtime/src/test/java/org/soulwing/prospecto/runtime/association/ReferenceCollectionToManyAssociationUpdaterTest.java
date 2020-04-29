/*
 * File created on Apr 5, 2016
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
package org.soulwing.prospecto.runtime.association;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.soulwing.prospecto.runtime.entity.InjectableViewEntity;
import org.soulwing.prospecto.runtime.reference.ReferenceResolverService;

/**
 * Unit tests for {@link ReferenceCollectionToManyAssociationUpdater}.
 *
 * @author Carl Harris
 */
public class ReferenceCollectionToManyAssociationUpdaterTest
    extends ValueCollectionToManyAssociationUpdaterTest {

  @Mock
  MockModel associate;

  @Mock
  InjectableViewEntity associateEntity;

  @Mock
  ReferenceResolverService resolvers;

  @Override
  protected ToManyAssociationUpdater newUpdater() {
    return new ReferenceCollectionToManyAssociationUpdater(
        descriptorFactory, managerLocator);
  }

  @Override
  protected Object value() {
    return associateEntity;
  }

  @Override
  protected Object resolvedValue() {
    return associate;
  }

  @Override
  protected Expectations resolveExpectations() throws Exception {
    return new Expectations() {
      {
        oneOf(associateEntity).getType();
        will(returnValue(MockModel.class));
        oneOf(viewContext).getReferenceResolvers();
        will(returnValue(resolvers));
        oneOf(resolvers).resolve(MockModel.class, associateEntity);
        will(returnValue(associate));
      }
    };
  }

  private interface MockModel {}

}
