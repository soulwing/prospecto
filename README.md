Prospecto
=========

[![Build Status](https://travis-ci.org/soulwing/prospecto.svg?branch=master)](https://travis-ci.org/soulwing/prospecto)

Prospecto is a toolkit for producing client views of a domain model in an
Java application that provides a RESTful web API.

Demo
----

Until I can write a more reasonable README, the best way to learn what
Prospecto can do is to look at a demo.

### Running the Demo

Open a shell and make the base `prosepecto` folder the shell's current 
folder.  Then run these commands.

```
mvn clean install
mvn wildfly:run
```

This will build Prospecto and will run it's JAX-RS demo module 
`prospecto-demo-jaxrs` in a Wildfly container. After the demo is running you
can use `curl` to access its resources.

```
curl -H "Accept: application/json" http://localhost:8080/prospecto-demo/api/orders
```

This should produce some JSON.

If you look carefully at the data returned by `curl` you'll see that it 
contains some `href` elements for other resources. For example, `/api/orders/9`.
If you append this path to the base URL for the application, you can fetch the
corresponding resource.  For example:

```
curl -H "Accept: application/json" http://localhost:8080/prospecto-demo/api/orders/9
```

Prospecto has built-in support for producing views using JSON or XML. You can
ask for XML instead of JSON as follows.

```
curl -H "Accept: application/xml" http://localhost:8080/prospecto-demo/api/orders
```


### Understanding the Demo

Investigate the source code for the demo at  `prospecto-demo-jaxrs/src/main/java`.

Start in the `domain` package. You'll see a simple domain model that represents
purchase orders, with JPA annotations for persistence.

The `startup` package contains a bean that loads some demo data into the 
database when the application starts.

In the `service` package you'll find a `PurchaseOrderService` and
`VendorService` that are used to access instances of the entities in the domain
model. Notice that the return values for the methods on the service are of type
`View`.  You also see a `UserContextService` in this package. We'll get to that 
later.

Before we look at how the service interfaces are implemented, take a look in
the `views` package. There are several interfaces with `...Views` as a naming
pattern. These interfaces define _view templates_. If you look at them carefully,
and compare them to the domain model types, you'll probably have a pretty good
idea of what they do. Each view template can be used to produce a `View`
from some model data. Some templates are referenced in other templates; 
templates can be composed to promote use and stay DRY. 

Now let's see how a template is used in `PurchaseOrderServiceBean`.  In each
of the methods in this bean, we use an entity manager to load some entities 
from the database. We then use a view template to generate a `View` instance
that contains the information in the retrieved entities. The template is used
to extract the various properties from the model data and put it into a form
that an be easily rendered as JSON, XML, YAML, or practically any other 
structured textual representation.  You'll see a similar implementation in
`VendorServiceBean`.

When we generate a view from a template, we provide a _view context_. The
context object provides access to a powerful API that can be used to instrument
and/or manipulate the view generation process. You can use it to implement
features such as filtering, data type conversion, and other transformations.
In the demo the view context comes from a `ViewContextProducerBean`; a CDI
bean that produces a `ViewContext` and injects it wherever it is needed. The
demo bean shows just a few of the things you can do with view context.

As you can see from the fact that the demo templates are defined as interface
constants, a template is completely static and immutable after it is created.
This makes view templates fully thread safe, so they can be used safely 
in application-scoped service beans.

When a view is generated, the model content for the view is extracted from the
model object instances (usually entity types) and put into a format that 
facilitates producing a textual representation. The extraction happens 
immediately. So by generating our views inside of service tier, it happens in
the context of JPA entity manager, avoiding any potential for lazy loading
exceptions when the view is ultimately transformed into a textual 
representation.

N.B. still need to discuss how URLs are generated; i.e. the relationship between
the `@ReferencedBy` and `@TemplateResolver` annotations and the view templates,
and how the `UrlResolver` is set up.


  











