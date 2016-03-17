/*
 * File created on Mar 16, 2016
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
package org.soulwing.prospecto.demo;

/**
 * DESCRIBE THE TYPE HERE
 * @author Carl Harris
 */
public class DepartmentPurchaser implements Purchaser {

  private Long id;

  private String departmentId;
  private String name;

  /**
   * Gets the {@code id} property.
   * @return property value
   */
  @Override
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Gets the {@code departmentId} property.
   * @return property value
   */
  public String getDepartmentId() {
    return departmentId;
  }

  /**
   * Sets the {@code departmentId} property.
   * @param departmentId the property value to set
   */
  public void setDepartmentId(String departmentId) {
    this.departmentId = departmentId;
  }

  /**
   * Gets the {@code name} property.
   * @return property value
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the {@code name} property.
   * @param name the property value to set
   */
  public void setName(String name) {
    this.name = name;
  }

}
