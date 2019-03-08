# bindable.scala
[![Latest version](https://index.scala-lang.org/thoughtworksinc/bindable.scala/bindable/latest.svg)](https://index.scala-lang.org/thoughtworksinc/bindable.scala/bindable)
[![Scaladoc](https://javadoc.io/badge/com.thoughtworks.binding/bindable_sjs0.6_2.12.svg?label=scaladoc)](https://javadoc.io/page/com.thoughtworks.binding/bindable_sjs0.6_2.12/latest/com/thoughtworks/binding/bindable/index.html)
[![Build Status](https://travis-ci.org/ThoughtWorksInc/bindable.scala.svg)](https://travis-ci.org/ThoughtWorksInc/bindable.scala)

**bindable.scala** is a library of type classes for creating [Binding.scala](https://github.com/ThoughtWorksInc/Binding.scala)\-based reusable components.

## Motivation

When creating a component that accepts parameters or “holes”, it is difficult to determine the types of those parameters.
 
For example, the following component accepts two `Binding` as parameters:

```scala
@dom def myComponent1(title: Binding[String], children: Binding[BindingSeq[Node]]) = {
  <div title={title.bind}>
    {children.bind}
  </div>
}
```

This component allows partial rendering when the value of `title` or `children` is changed. Unfortunately, it is too verbose to use `myComponent1` for simple use cases.

```scala
// Does not compile
@dom def myUseCases1 = myComponent1("My Title", <img/>).bind

// Compiles, but too verbose
@dom def myUseCases2 = myComponent1(Constant("My Title"), Constant(Constants(<img/>))).bind
``` 

In this library, we introduced two type classes, `Bindable` and `BindableSeq`, to allow heterogeneous types of parameters for a component.

## Usage

### Step 1: adding the following settings into your `build.sbt`

```sbt
addCompilerPlugin("org.spire-math" %% "kind-projector" % "latest.release")

libraryDependencies += "com.thoughtworks.binding" %%% "bindable" % "latest.release"
```

### Step 2: creating a component that accepts bindable parameters

```scala
import com.thoughtworks.binding.bindable._
import org.scalajs.dom.raw._
@dom def myComponent2[Title: Bindable.Lt[?, String], Children: BindableSeq.Lt[?, Node]](title: Title, children: Children) = {
  <div title={title.bind}>
    {children.bindSeq}
  </div>
}
```

### Step 3: using the component with any parameters that can be converted to `Binding` or `BindingSeq`


```scala
import com.thoughtworks.binding._, Binding._
@dom def myUseCases3 = myComponent2("My Title", <img/>).bind
@dom def myUseCases4 = myComponent2(Constant("My Title"), Constant(Constants(<img/>))).bind
@dom def myUseCases5 = myComponent2("My Title", <img/><img/>).bind
@dom def myUseCases6 = myComponent2("My Title", Binding(<img/><img/>)).bind
```

Unlike use cases of `myComponent1`, all the above use cases of `myComponent2` compile now, because of the `Bindable` and `BindableSeq` type classes.

## Links

* [Documentation](https://javadoc.io/page/com.thoughtworks.binding/bindable_sjs0.6_2.12/latest/com/thoughtworks/binding/bindable/index.html)
* [Binding.scala](https://github.com/ThoughtWorksInc/Binding.scala)
