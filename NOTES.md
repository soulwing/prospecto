Notes
-----

What follows are notes about what motivates this thing and how it works. It'll
be turned into more reasonable prose soon.  :-)

* Many Java-based RESTful APIs are implemented using a rich domain model.
  * Domain model types annotated with JPA annotations

* Designed to reliably convey state in a way that supports both serialization and deserialization

* Marshalling domain model types
  * Difficult to control
  * Tends to expose too much knowledge of domain model -- breaks encapsulation
  * Generally doesn't leverage JPA annotations; need a whole additional layer of annotations/configuration
  * Difficult to produce different representations for different clients or different situations

* Leads to the use of DTO pattern
  * Anemic types
  * nearly parallel class hiearchies
  * expensive to maintain


Applying MVC to a web client API
--------------------------------

* The model is the domain model
* The control is the HTTP resource methods and the services they invoke
* Whats's the view?
  * It is what we deliver to the client in response to a resource request

### What is Client View?

* Related to the model, but it is an abstraction that is appropriate for the client
* Hierarchical in structure; composed of simple types
  * value types: string, number, boolean
  * structure types
    * object: name-value pairs; each name identifies a value or a structure
    * array of object: sequence of objects
    * array of values: sequence of values
* Has a textual representation; e.g. JSON, XML, YAML
  * but the text itself is *not* the view, it is just a representation of the view that is 
    convenient for transmission to/from an HTTP client
* Has unique requirements not usually present in the domain model itself
  * data conversion and other kinds of transformation
  * access control and other forms of filtering
  * multiple views of a domain model subgraph -- different aspects for different needs
* It is a data type, but the type is *not* implemented in the application

### What is a View Template

* Instructions for producing a view from a (partial) graph of domain model objects
* Has knowledge of the domain model types and how to extract properties from them
* Specified by the application developer
  * using a fluent builder API
  * using annotations  

### Generating a View from a Template

* A template can be used with an instance of the domain model type to produce a view
* The domain model graph is navigated by the generator based on instructions in the template
* In the resulting view the structures and values specified by the template are composed 
  using actual values from the graph of model objects
* After the view has been generated it no longer needs access to the model graph
  on which it is based
* The resulting view can be used to produce any supported textual representation


### Technical Considerations

* Want to do most of the heavy lifting within the context of a transaction (entity manager) 
  while entities can still be lazily fetched as needed
* View needs to be something that can be easily passed around between the layers of the
  application and transformed into text only when necessary
