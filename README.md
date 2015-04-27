# Flow navigation

Flow navigation is an alternative to flow-path from Flow.  


### Why

Flow-path was extracted from flow as standalone additional library, in order to provide convenient classes to work with screens and navigation. However flow-path does not preserve the paths in flow history.  

For instance, if we go from path A to path B, the scope associated to the path A will be destroyed once we're in path B. And when we go back to path A, the scope associated to A is recreated (and everything within, like the presenter).

With flow-navigation, path A and its scope are not destroyed when we reach path B. It represents a more natural and logical logic of navigation.

During FORWARD navigation, flow navigation preserves only the scope, the view is still destroyed. And in BACKWARD and REPLACE navigation, everything is destroyed like it should.


### Worflow with flow-path

![Flow path](./doc/flow_path.png "Flow path")


### Workflow with flow navigation

![Flow navigation](./doc/flow_navigation.png "Flow navigation")


## Usage

Structure of flow navigation is very similar to flow-path. You basically need to replace the package import from flow.path to flownavigation. Classes like `Path` and `PathContainer` still exists and work in the same way than the ones from flow-path.

In addition, flow-navigation provides some classes from Flow sample that are often reused in all projects. Like @Layout annotation (recently moved out from Flow library), SimplePathContainer and FramePathContainerView.

Checkout the sample in order to see how it works, but it's basically the same as flow-path except the scope isn't destroyed during FORWARD navigation.


## Status

It's more an experiment than a library so far.  
It works fine in my first tests, but probably many edges cases non-covered.  
All feedback welcomed.


## Installation

Flow-navigation is available on OSS sonatype.

```groovy
repositories {
	maven { url "https://oss.sonatype.org/content/repositories/snapshots" }
}

dependencies {
    compile 'com.github.lukaspili:flow-navigation:0.1-SNAPSHOT@aar'
}
```

Flow-navigation uses an alternative dependency for Flow, which is up-to-date with the latest Flow commit, also hosted on sonatype ([https://github.com/lukaspili/flow](https://github.com/lukaspili/flow)).

```groovy
repositories {
	maven { url "https://oss.sonatype.org/content/repositories/snapshots" }
}

dependencies {
	compile 'com.github.lukaspili:flow:0.9-SNAPSHOT'
}
```

## Mortar MVP

Mortar MVP is another experiment around Mortar and Flow.  
It focuses on removing the boilerplate code that requires the MVP pattern with Mortar / Flow / Dagger 2.  
Check it out here: [https://github.com/lukaspili/mortar-mvp](https://github.com/lukaspili/mortar-mvp)



## Author

- Lukasz Piliszczuk ([@lukaspili](https://twitter.com/lukaspili))


## License

Mortar MVP is released under the MIT license. See the LICENSE file for more info.
