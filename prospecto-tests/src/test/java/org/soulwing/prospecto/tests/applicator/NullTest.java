/*
 * File created on Apr 11, 2016
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

import org.junit.Test;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.api.ViewTemplate;

/**
 * Tests of various cases of null nested objects.
 *
 * @author Carl Harris
 */
public class NullTest extends ViewApplicatorTestBase {

  @Test
  public void test() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object("nullTest", Player.class)
            .object("medicalInfo", MedicalInfo.class)
                .object("insuranceInfo", InsuranceInfo.class)
                    .value("provider")
                    .value("policyNumber")
                    .end()
                .end()
            .end()
        .build();

    validate(template);
  }

  public static class Player {
    private MedicalInfo medicalInfo = new MedicalInfo();

    public MedicalInfo getMedicalInfo() {
      return medicalInfo;
    }

    public void setMedicalInfo(MedicalInfo medicalInfo) {
      this.medicalInfo = medicalInfo;
    }
  }

  public static class MedicalInfo {
    private InsuranceInfo insuranceInfo = new InsuranceInfo();

    public InsuranceInfo getInsuranceInfo() {
      return insuranceInfo;
    }

    public void setInsuranceInfo(InsuranceInfo insuranceInfo) {
      this.insuranceInfo = insuranceInfo;
    }
  }

  public static class InsuranceInfo {
    private String provider;
    private String policyNumber;

    public String getProvider() {
      return provider;
    }

    public void setProvider(String provider) {
      this.provider = provider;
    }

    public String getPolicyNumber() {
      return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
      this.policyNumber = policyNumber;
    }
  }

}
