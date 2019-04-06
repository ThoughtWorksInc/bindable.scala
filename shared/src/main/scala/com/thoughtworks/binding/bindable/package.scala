package com.thoughtworks.binding

import com.thoughtworks.binding.Binding._
import simulacrum._
import scala.language.implicitConversions

package object bindable extends Bindable.ToBindableOps with BindableSeq.ToBindableSeqOps {
  implicit def bindableToBinding[From, Value](from: From)(implicit bindable: Bindable.Lt[From, Value]): Binding[Value] =
    bindable.toBinding(from)
}

package bindable {
  import com.thoughtworks.enableMembersIf
  @enableMembersIf(c => !c.compilerSettings.exists(_.matches("""^-Xplugin:.*scalajs-compiler_[0-9\.\-]*\.jar$""")))
  private[bindable] object Jvm {
    trait LowPriorityJsBindableSeq2 extends LowPriorityBindableSeq2
    trait LowPriorityJsBindableSeq0 extends LowPriorityBindableSeq0
  }

  @enableMembersIf(c => c.compilerSettings.exists(_.matches("""^-Xplugin:.*scalajs-compiler_[0-9\.\-]*\.jar$""")))
  private[bindable] object Js {
    import scala.scalajs.js

    trait LowPriorityJsBindableSeq2 extends LowPriorityBindableSeq2 {

      implicit def jsArrayBindableSeq[Value0]: BindableSeq.Aux[js.Array[Value0], Value0] =
        new BindableSeq[js.Array[Value0]] {
          type Value = Value0
          def toBindingSeq(from: js.Array[Value0]): BindingSeq[Value] = Constants(from: _*)
        }
    }

    trait LowPriorityJsBindableSeq0 extends LowPriorityBindableSeq0 {

      implicit def bindingJsArrayBindableSeq[Value0]: BindableSeq.Aux[Binding[js.Array[Value0]], Value0] =
        new BindableSeq[Binding[js.Array[Value0]]] {
          type Value = Value0
          def toBindingSeq(from: Binding[js.Array[Value0]]): BindingSeq[Value] =
            Constants(from).flatMap { from =>
              Constants(from.bind: _*)
            }
        }
    }

  }

  import Jvm._
  import Js._

  private[bindable] trait LowPriorityBindable0 {

    implicit def constantBindable[Value0]: Bindable.Aux[Value0, Value0] = new Bindable[Value0] {
      type Value = Value0
      def toBinding(from: Value): Binding[Value] = Constant(from)
    }

  }

  object Bindable extends LowPriorityBindable0 {
    type Aux[-From, Value0] = Bindable[From] {
      type Value = Value0
    }
    type Lt[-From, +Value0] = Bindable[From] {
      type Value <: Value0
    }

    implicit def bindingBindable[Value0]: Bindable.Aux[Binding[Value0], Value0] = new Bindable[Binding[Value0]] {
      type Value = Value0
      def toBinding(from: Binding[Value0]): Binding[Value] = from
    }

  }

  /** A dependent type class that witnesses a type that can be converted to a `Binding[Value]`.
    *
    * @example The implicit conversion to `Binding` can be enabled by the following `import` statement:
    *
    *          {{{
    *          import com.thoughtworks.binding.bindable._
    *          }}}
    *
    *          Then, a `@dom` XHTML template can establish data-binding on any `parameter`
    *          as long as a [[Bindable]] type class for the `parameter` type is available.
    *
    *          {{{
    *          @dom
    *          def mySection[A: Bindable.Lt[?, String]](parameter: A) = {
    *            <img class={parameter.bind}/>
    *          }
    *          }}}
    *
    *          Note that the `?` syntax requires the Scala plug-in
    *          [[https://github.com/non/kind-projector kind-projector]],
    *          which can be enabled by adding the following setting into your `build.sbt`:
    *
    *          <pre>addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.10")</pre>
    *
    *          The `mySection` method accepts any parameter who is a subtype of `Binding[String]`.
    *
    *          {{{
    *          import com.thoughtworks.binding._, Binding._
    *          Binding {
    *            mySection(Binding("my-class-1")).bind.className should be("my-class-1")
    *            mySection(Constant("my-class-2")).bind.className should be("my-class-2")
    *          }.watch()
    *          }}}
    *
    *          And the `mySection` method also accepts [[java.lang.String String]] parameter.
    *          {{{
    *          Binding {
    *            mySection("my-class-3").bind.className should be("my-class-3")
    *          }.watch()
    *          }}}
    *
    *          `mySection` should not accept irrelevant parameter types like [[scala.List]] or [[scala.Int]].
    *
    *          {{{
    *          "mySection(List.empty)" shouldNot compile
    *          "mySection(42)" shouldNot compile
    *          }}}
    */
  @typeclass
  trait Bindable[-From] {
    type Value
    def toBinding(from: From): Binding[Value]
  }

  private[bindable] trait LowPriorityBindableSeq2 {
    @deprecated("Potential naming conflict with `Bindable.constantBindable`.", "1.0.2")
    private[bindable] def constantBindable[Value] = constantsBindableSeq[Value]

    implicit def constantsBindableSeq[Value0]: BindableSeq.Aux[Value0, Value0] = new BindableSeq[Value0] {
      type Value = Value0
      def toBindingSeq(from: Value): BindingSeq[Value] = Constants(from)
    }
  }

  private[bindable] trait LowPriorityBindableSeq1 extends LowPriorityJsBindableSeq2 {
    implicit def bindingBindableSeq[Value0]: BindableSeq.Aux[Binding[Value0], Value0] =
      new BindableSeq[Binding[Value0]] {
        type Value = Value0
        def toBindingSeq(from: Binding[Value0]): BindingSeq[Value] = SingletonBindingSeq(from)
      }

    implicit def scalaSeqBindableSeq[Value0]: BindableSeq.Aux[Seq[Value0], Value0] =
      new BindableSeq[Seq[Value0]] {
        type Value = Value0
        def toBindingSeq(from: Seq[Value0]): BindingSeq[Value] = Constants(from: _*)
      }

    implicit def scalaArrayBindableSeq[Value0]: BindableSeq.Aux[Array[Value0], Value0] =
      new BindableSeq[Array[Value0]] {
        type Value = Value0
        def toBindingSeq(from: Array[Value0]): BindingSeq[Value] = Constants(from: _*)
      }
  }

  private[bindable] trait LowPriorityBindableSeq0 extends LowPriorityBindableSeq1 {

    implicit def bindingSeqBindableSeq[Value0]: BindableSeq.Aux[BindingSeq[Value0], Value0] =
      new BindableSeq[BindingSeq[Value0]] {
        type Value = Value0
        def toBindingSeq(from: BindingSeq[Value0]): BindingSeq[Value] = from
      }
  }

  object BindableSeq extends LowPriorityJsBindableSeq0 {
    type Aux[-From, Value0] = BindableSeq[From] {
      type Value = Value0
    }

    type Lt[-From, +Value0] = BindableSeq[From] {
      type Value <: Value0
    }

    implicit def bindingbindingSeqBindableSeq[Value0]: Aux[Binding[BindingSeq[Value0]], Value0] =
      new BindableSeq[Binding[BindingSeq[Value0]]] {
        type Value = Value0
        def toBindingSeq(from: Binding[BindingSeq[Value0]]): BindingSeq[Value] =
          Constants(from).flatMapBinding(identity)
      }

    implicit def bindingScalaArrayBindableSeq[Value0]: Aux[Binding[Array[Value0]], Value0] =
      new BindableSeq[Binding[Array[Value0]]] {
        type Value = Value0
        def toBindingSeq(from: Binding[Array[Value0]]): BindingSeq[Value] =
          Constants(from).flatMap { from =>
            Constants(from.bind: _*)
          }
      }

    implicit def bindingScalaSeqBindableSeq[Value0]: Aux[Binding[Seq[Value0]], Value0] =
      new BindableSeq[Binding[Seq[Value0]]] {
        type Value = Value0
        def toBindingSeq(from: Binding[Seq[Value0]]): BindingSeq[Value] =
          Constants(from).flatMap { from =>
            Constants(from.bind: _*)
          }
      }

  }

  /** A dependent type class that witnesses a type that can be converted to a `BindingSeq[Value]`.
    *
    * @example The [[com.thoughtworks.binding.bindable.BindableSeq.Ops.bindSeq bindSeq]]
    *          can be enabled by the following `import` statement:
    *
    *          {{{
    *          import com.thoughtworks.binding.bindable._
    *          }}}
    *
    *          Then, a `@dom` XHTML template can establish data-binding on any `parameter`
    *          as long as a [[BindableSeq]] type class for the `parameter` type is available.
    *
    *          {{{
    *          import org.scalajs.dom.raw._
    *
    *          @dom
    *          def mySection[A: BindableSeq.Lt[?, Node]](parameter: A) = {
    *            <section>{parameter.bindSeq}</section>
    *          }
    *          }}}
    *
    *          Note that the `?` syntax requires the Scala plug-in
    *          [[https://github.com/non/kind-projector kind-projector]],
    *          which can be enabled by adding the following setting into your `build.sbt`:
    *
    *          <pre>addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.10")</pre>
    *
    *          The `mySection` method accepts any parameter who is a subtype of `Binding[Node]`
    *          or `Binding[BindingSeq[Node]]`.
    *
    *          {{{
    *          import com.thoughtworks.binding._, Binding._
    *          @dom def myButton: Binding[HTMLButtonElement] = <button type="button">My Button 0</button>
    *          @dom def myButtons: Binding[BindingSeq[Node]] = <button type="button">My Button 1</button><button type="button">My Button 2</button>
    *          Binding {
    *            mySection(myButton).bind.innerHTML should be("<button type=\"button\">My Button 0</button>")
    *            mySection(myButtons).bind.innerHTML should be("<button type=\"button\">My Button 1</button><button type=\"button\">My Button 2</button>")
    *          }.watch()
    *          }}}
    *
    *          And the `mySection` method also accepts `Node`, `BindingSeq[Node]`, `List[Node]`, `Array[Node]`, `js.Array[Node]`, or `Binding[js.Array[Node]]` parameter.
    *          {{{
    *          import scala.scalajs.js
    *          @dom def test = {
    *            mySection(<button type="button">My Button 3</button>).bind.innerHTML should be("<button type=\"button\">My Button 3</button>")
    *            mySection(<button type="button">My Button 4</button><button type="button">My Button 5</button>).bind.innerHTML should be(
    *              "<button type=\"button\">My Button 4</button><button type=\"button\">My Button 5</button>"
    *            )
    *            mySection(List(<button type="button">My Button 6</button>, <button type="button">My Button 7</button>)).bind.innerHTML should be("<button type=\"button\">My Button 6</button><button type=\"button\">My Button 7</button>")
    *            mySection(Array(<button type="button">My Button 8</button>, <button type="button">My Button 9</button>)).bind.innerHTML should be("<button type=\"button\">My Button 8</button><button type=\"button\">My Button 9</button>")
    *            mySection(js.Array(<button type="button">My Button 10</button>, <button type="button">My Button 11</button>)).bind.innerHTML should be("<button type=\"button\">My Button 10</button><button type=\"button\">My Button 11</button>")
    *            mySection(Constant(js.Array(<button type="button">My Button 12</button>, <button type="button">My Button 13</button>))).bind.innerHTML should be("<button type=\"button\">My Button 12</button><button type=\"button\">My Button 13</button>")
    *          }
    *
    *          test.watch()
    *          }}}
    *
    *          `mySection` should not accept irrelevant parameter types like [[scala.collection.immutable.Set]] or [[scala.Int]].
    *
    *          {{{
    *          "mySection(Set.empty)" shouldNot compile
    *          "mySection(42)" shouldNot compile
    *          }}}
    */
  @typeclass
  trait BindableSeq[-From] {
    type Value
    @op("bindSeq", alias = true)
    def toBindingSeq(from: From): BindingSeq[Value]
  }
}
