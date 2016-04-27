prospecto-jackson
=================

This module contains the necessary adapter classes to allow the Prospecto `View` 
type in Jackson-annotated Java objects. This provides a migration strategy for
applications that make use of Jackson to create domain model views using JSON. 
With this adapter support, an application that uses Jackson to marshal and 
unmarshal  _data transfer objects_ (DTOs) can gradually introduce Prospecto 
views to replace DTO classes.  
