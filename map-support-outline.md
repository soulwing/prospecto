## `mapOfValues`
In the template, this works like `arrayOfValues`. It doesn't support children, and the resulting view is structure containing the key value pairs from the map. The maps and arrays in the map are recursively evaluated; other objects are converted to simple value types.

> The current implementation of `arrayOfValues` should also modified such that maps and arrays are recursively evaluated in the same manner.

Suppose
* Registered `ValueTypeConverter` objects should be used to convert value types in a map, just as they are currently used to convert value types in an array.

## `mapOfObjects`
In the template, this works like `arrayOfObjects` in that you can add children under it. The resulting view is is a structure of structures, where each nested structure contains the specified children. All of the existing children that are supported for `object` or `arrayOfObjects` could be used under `mapOfObjects`. The `mapOfObjects` template builder should allow a `ValueTypeConverter` to be specified to convert the keys of the map to simple value types. Additionally, key conversion could be handled using by-type conversion by a converter registered on the view context.

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

This can be easily represented in the event stream of the view

```
BEGIN_OBJECT "channels"
BEGIN_OBJECT "general"
VALUE "description" "General discussion"
BEGIN_ARRAY "members"
VALUE "machelle"
VALUE "carl"
VALUE "lisa"
END_ARRAY "members"
END_OBJECT "general"
BEGIN_OBJECT "random"
...
END_OBJECT "random"
END_OBJECT "channels"
```

## Value type conversion

