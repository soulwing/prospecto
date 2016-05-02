/*
 * File created on Apr 4, 2016
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
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewTemplate;

/**
 * Tests for an object with multiple subtypes.
 * @author Carl Harris
 */
public class ObjectSubtypeTest extends ViewApplicatorTestBase {

  @Test
  public void testWithPropertyAccess() throws Exception {
    validate(newTemplate(AccessType.PROPERTY));
  }

  @Test
  public void testWithFieldAccess() throws Exception {
    validate(newTemplate(AccessType.FIELD));
  }

  protected ViewTemplate newTemplate(AccessType accessType) {
    return ViewTemplateBuilderProducer
        .object("objectSubtypeTest", ParentModel.class)
            .accessType(accessType)
            .object("vehicle", newVehicleTemplate(accessType))
            .end()
        .build();
  }

  protected ViewTemplate newVehicleTemplate(AccessType accessType) {
    return ViewTemplateBuilderProducer
        .object("vehicle", Vehicle.class)
            .accessType(accessType)
            .discriminator()
            .value("passengerCapacity")
            .subtype(Car.class)
                .value("color")
                .value("driveTrain")
                .end()
            .subtype(Plane.class)
                .value("wingspan")
                .value("range")
                .value("topSpeed")
                .end()
            .end()
        .build();
  }

  @SuppressWarnings("unused")
  public static class ParentModel {
    private Vehicle vehicle;

    public Vehicle getVehicle() {
      return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
      this.vehicle = vehicle;
    }
  }

  @SuppressWarnings("unused")
  public static class Vehicle {
    private int passengerCapacity;

    public int getPassengerCapacity() {
      return passengerCapacity;
    }

    public void setPassengerCapacity(int passengerCapacity) {
      this.passengerCapacity = passengerCapacity;
    }

  }

  @SuppressWarnings("unused")
  public static class Car extends Vehicle {
    private String color;
    private String driveTrain;

    public String getColor() {
      return color;
    }

    public void setColor(String color) {
      this.color = color;
    }

    public String getDriveTrain() {
      return driveTrain;
    }

    public void setDriveTrain(String driveTrain) {
      this.driveTrain = driveTrain;
    }

  }

  @SuppressWarnings("unused")
  public static class Plane extends Vehicle {
    private int wingspan;
    private int range;
    private int topSpeed;

    public int getWingspan() {
      return wingspan;
    }

    public void setWingspan(int wingspan) {
      this.wingspan = wingspan;
    }

    public int getRange() {
      return range;
    }

    public void setRange(int range) {
      this.range = range;
    }

    public int getTopSpeed() {
      return topSpeed;
    }

    public void setTopSpeed(int topSpeed) {
      this.topSpeed = topSpeed;
    }

  }


}
