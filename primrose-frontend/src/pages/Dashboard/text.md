# Live demo

Changes are automatically rendered as you type.

* Implements [GitHub Flavored Markdown](https://github.github.com/gfm/)
* Renders actual, "native" React DOM elements
* Allows you to escape or skip HTML (try toggling the checkboxes above)
* If you escape or skip the HTML, no `dangerouslySetInnerHTML` is used! Yay!

## HTML block below

<blockquote>
  This blockquote will change based on the HTML settings above.
</blockquote>

## How about some code?
```jsx
var React = require('react');
var Markdown = require('react-markdown');

React.render(
  <Markdown source="# Your markdown here" />,
  document.getElementById('content')
);
```

```java
@Override
  public long count(Pagination pagination) {

    Condition searchCondition = buildSearchCondition(pagination);

    return create
      .selectCount()
      .from(CUSTOMERS)
      .innerJoin(CUSTOMER_DATA).on(
        CUSTOMER_DATA.CUSTOMER.eq(CUSTOMERS.ID),
        containes(tstzrange(CUSTOMER_DATA.VALID_FROM, CUSTOMER_DATA.VALID_TO, value("[)")), currentOffsetDateTime()))
      .innerJoin(CUSTOMER_TYPES).on(CUSTOMER_TYPES.ID.eq(CUSTOMER_DATA.CUSTOMER_TYPE))
      .innerJoin(CUSTOMER_RELATION_TYPES).on(CUSTOMER_RELATION_TYPES.ID.eq(CUSTOMER_DATA.CUSTOMER_RELATION_TYPE))
      .where(searchCondition)
      .fetchOne()
      .value1();
  }
```

```ruby
=begin
This program will
print "Hello, world!".
=end
 
puts 'Hello, world!'
```

Pretty neat, eh?

## Tables?

| Feature | Support |
| ------ | ----------- |
| tables | ✔ |
| alignment | ✔ |
| wewt | ✔ |

## More info?

Read usage information and more on [GitHub](//github.com/rexxars/react-markdown)

---------------

A component by [VaffelNinja](http://vaffel.ninja) / Espen Hovlandsdal