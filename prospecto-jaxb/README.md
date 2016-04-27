prospecto-jaxb
==============

This module contains the necessary adapter classes to allow the Prospecto `View` 
type in JAXB-annotated Java objects. This provides a migration strategy for
applications that make use of JAXB to create domain model views using XML. With 
this adapter support, an application that uses JAXB to marshal and unmarshal 
_data transfer objects_ (DTOs) can gradually introduce Prospecto views to 
replace DTO classes.  
