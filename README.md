JLoadApp
==============
version: 0.1.1

Resources for loading application resources and settings:
* `Locations` - a singleton for tracking an application's location (code & resources), includes methods for resolving application relative paths
* `Twg2Ioc` - a dependency injection container, use `register()`, `registerResolver()`, and `registerValue()` to add dependencies, and `resolve()` to create an instance

Take a look at the 'twg2.io.test' package for some examples of how the API can be used.
