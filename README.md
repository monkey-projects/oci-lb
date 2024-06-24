# OCI Load Balancer

This is a Clojure library that provides functionality to access the OCI Load balancer
API in a native fashion.

## Why?

Why not use the [existing Java libs](https://docs.oracle.com/en-us/iaas/Content/API/SDKDocs/javasdk.htm#SDK_for_Java)
provided by Oracle?  It's up to you of course, but I don't like having to marshal
to-and-from Java POJO's all the time.  I also didn't like all the additional dependencies
that it brings.  I prefer having more control over what is happening.  This library uses
[Martian](https://github.com/oliyh/martian) to directly access the API over HTTP, without
having to build intermediate Java objects.

## Compiling/Testing

We use [Clojure CLI](https://clojure.org/reference/deps_and_cli) to build this lib.
Run the tests like so:

```bash
clj -X:test
```

See [deps.edn](deps.edn) for more aliases you can use.

## CI/CD

We use [MonkeyCI](https://monkeyci.com) to build this library.  It's being deployed to
[Clojars](https://clojars.org).

## License

[MIT license](LICENSE)

Copyright (c) 2024 by [Monkey Projects BV](https://www.monkey-projects.be).