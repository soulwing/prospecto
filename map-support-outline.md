## Template Support for Maps

### `mapOfValues`
In the template, this works like `arrayOfValues`, except that the corresponding model type is a Map instead of an array or other Collection type. The template builder should allow a `ValueTypeConverter` to be specified to convert the keys of the map to simple value types. Additionally, key conversion could be handled using by-type conversion by a converter registered on the view context.

The `mapOfValues` method doesn't have descendant view nodes, and the resulting view is structure containing the key-value pairs from the map. Maps and arrays uses as map values are recursively evaluated; other objects are converted to simple value types. Registered `ValueTypeConverter` objects should be used to convert value types in a map, just as they are currently used to convert value types in an array.

> The current implementation of `arrayOfValues` should also modified such that maps and arrays are recursively evaluated in the same manner.

### `mapOfObjects`

In the template, this works like `arrayOfObjects` in that you can add children under it. The resulting view is a structure of structures, where each nested structure contains the specified children. All of the existing children that are supported for `object` or `arrayOfObjects` could be used under `mapOfObjects`. Additionally, the `mapOfObjects` template builder should allow a `ValueTypeConverter` to be specified to convert the keys of the map to simple value types. Additionally, key conversion could be handled via by-type conversion using a converter registered on the view context.


## Generator Support for Maps

### `mapOfValues`

Suppose we have a `Preference` object that is a POJO with properties that identify an owner for the object (a person) and a map property named `prefs` of name-value pairs representing the preferences themselves. Suppose the preferences map contains the following

```
{ "colorScheme": "darksun", "lastCheck":1478961257102, "favorites":[ "general", "random" ] }
```

A segment of a view template for the `Preference` object might be written as follows.

```
builder.object("preference", Preference.class)
  .value("id")
  .object("owner", Person.class)
     .value("id")
     .value("username")
     .toStringValue("displayName")
     .end()
  .mapOfValues("prefs")
  .end()
```

A resulting JSON representation of this view segment might look as follows.

```
"preference": {
  "id": 307,
  "owner": {
    "id":1143195,
    "username": "megan",
    "displayName": "Megan Marshall"
  },
  "prefs": { 
    "colorScheme": "darksun", 
    "lastCheck": 1478961257102, 
    "favorites":[ 
       "general", 
       "random" 
    ] 
  }
}
```

Suppose we had a map of `Channel` objects, each of which is a POJO with some properties describing a Slack channel. A segment of a view template for such a map might be written as follows.

```
builder.mapOfObjects("channels", Channel.class)
  .value("description")
  .arrayOfValues("members")
  .end()
```

Assuming that the map is keyed by channel name and contains a couple of channels named _general_ and _random_, a resulting JSON representation of this view segment might look as follows.

```
"channels": {
  "general": {
    "description": "General discussion",
    "members": [ "machelle", "carl", "lisa" ]
  },
  "random": {
    "description": "Random chatter",
    "members": [ "megan", "trevor", "kim", "michael" ]
  }
}
```

## Applicator Support for Maps

### `mapOfValues`

When applying view content to a model at a `mapOfValues` node, it should be a matter of implementing a `MapOfValuesApplicator` (in
the runtime `applicator` package) that is very similar to the `ArrayOfValuesApplicator`. Instead of building a `List` in the `onToModelValue` method, it will instead build a `Map`.

The body of `ArrayOfValuesApplicator.onToModelValue` can probably be factored out into a common class that can be shared by both the `ArrayOfValuesApplicator` and `MapOfValuesApplicator`, since much of the necessary behavior will be identical. This will also provide the single place to implement the recursive evaluation of `BEGIN_OBJECT` and `BEGIN_ARRAY` events that appear in the stream for an array or a map.


### `mapOfObjects`

When applying view content to a model at a `mapOfObjects` node, the necessary support should be similar to the existing support for to-many associations. Probably want some sort of `KeyedToManyAssociationManager` that is similar to the `IndexedToManyAssociationManager`. Howver, instead of finding associates via an `indexOf` position, it provides methods to get and set map entries by key. Reconciling the differences between the map contents received in the view and the map contents of the target model should mostly be a straightforward matter of adding new entries, replacing existing entries, and removing those entries in the model that do not appear in the view.

