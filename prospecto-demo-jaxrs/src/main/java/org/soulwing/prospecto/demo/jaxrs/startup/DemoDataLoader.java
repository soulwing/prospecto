/*
 * File created on Mar 12, 2016
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
package org.soulwing.prospecto.demo.jaxrs.startup;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.soulwing.prospecto.demo.jaxrs.domain.Contact;
import org.soulwing.prospecto.demo.jaxrs.domain.Division;
import org.soulwing.prospecto.demo.jaxrs.domain.Gender;
import org.soulwing.prospecto.demo.jaxrs.domain.League;
import org.soulwing.prospecto.demo.jaxrs.domain.Parent;
import org.soulwing.prospecto.demo.jaxrs.domain.Player;
import org.soulwing.prospecto.demo.jaxrs.domain.Team;
import org.soulwing.prospecto.demo.jaxrs.domain.Token;
import org.soulwing.prospecto.demo.jaxrs.domain.TokenList;

/**
 * A startup bean that populates the database with some demo data.
 * @author Carl Harris
 */
@Startup
@Singleton
public class DemoDataLoader {

  @PersistenceContext
  private EntityManager entityManager;

  @PostConstruct
  public void init() {

    League league = new League();
    league.setName("Blacksburg Dixie Softball");

    Division division = new Division();
    division.setName("Ponytails");
    division.setAgeLimit(12);
    division.setGender(Gender.FEMALE);

    league.addDivision(division);

    Contact manager = newContact("Marshall", "Megan");

    Team team = new Team();
    team.setName("Bulldogs");
    team.setManager(manager);

    division.addTeam(team);


    Player player = newPlayer("Martin", "Cherylanne Bailey Wyche",
        "Squirrel", "2003-07-30",
        newParent(Parent.Relationship.FATHER, "Martin", "Josh"),
        newParent(Parent.Relationship.MOTHER, "Martin", "Nadine Bennett"));

    division.addPlayer(player);

    entityManager.persist(manager);
    entityManager.persist(league);
    entityManager.flush();
  }



  private static Player newPlayer(String surname, String givenNames,
      String preferredName, String birthDate, Parent... parents) {
    final Player player = new Player();
    System.out.println(player.getMedicalInfo());
    player.setSurname(Token.valueOf(surname));
    player.setGivenNames(TokenList.valueOf(givenNames));
    player.setPreferredName(preferredName == null ?
        player.getGivenNames().toList().get(0) : Token.valueOf(preferredName));
    player.setGender(Gender.FEMALE);
    player.setBirthDate(stringToDate(birthDate));
    for (final Parent parent : parents) {
      player.addParent(parent);
    }
    return player;
  }

  private static Parent newParent(Parent.Relationship relationship,
      String surname, String givenNames) {
    final Parent parent = new Parent();
    parent.setRelationship(relationship);
    parent.setSurname(Token.valueOf(surname));
    parent.setGivenNames(TokenList.valueOf(givenNames));
    switch (relationship) {
      case MOTHER:
        parent.setGender(Gender.FEMALE);
        break;
      case FATHER:
        parent.setGender(Gender.MALE);
        break;
    }
    return parent;
  }

  private static Contact newContact(String surname, String givenNames) {
    final Contact contact = new Contact();
    contact.setSurname(Token.valueOf(surname));
    contact.setGivenNames(TokenList.valueOf(givenNames));
    return contact;
  }

  private static Date stringToDate(String text) {
    final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    try {
      return df.parse(text);
    }
    catch (ParseException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

}
