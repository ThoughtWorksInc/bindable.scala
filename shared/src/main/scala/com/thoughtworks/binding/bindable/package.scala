package com.thoughtworks.binding

import com.thoughtworks.binding._
import com.thoughtworks.binding.Binding._
import com.thoughtworks.enableIf
import simulacrum._
import scala.language.implicitConversions
import scala.collection.immutable.ArraySeq

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
          def toBindingSeq(from: js.Array[Value0]): BindingSeq[Value] = Constants(scalajs.runtime.toScalaVarArgs(from): _*)
        }
    }

    trait LowPriorityJsBindableSeq0 extends LowPriorityBindableSeq0 {

      implicit def bindingJsArrayBindableSeq[Value0]: BindableSeq.Aux[Binding[js.Array[Value0]], Value0] =
        new BindableSeq[Binding[js.Array[Value0]]] {
          type Value = Value0
          def toBindingSeq(from: Binding[js.Array[Value0]]): BindingSeq[Value] =
            Constants(from).flatMap { from =>
              Constants(scalajs.runtime.toScalaVarArgs(from.bind): _*)
            }
        }
    }

  }

  import Jvm._
  import Js._

  import scala.concurrent.{ExecutionContext, Future}
  import scala.util.Try

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

    implicit def futureBindable[Value0](
        implicit executionContext: ExecutionContext): Bindable.Aux[Future[Value0], Option[Try[Value0]]] =
      new Bindable[Future[Value0]] {
        type Value = Option[Try[Value0]]
        def toBinding(from: Future[Value0]): Binding[Value] = FutureBinding(from)
      }

    @enableIf(c => c.compilerSettings.exists(_.matches("""^-Xplugin:.*scalajs-compiler_[0-9\.\-]*\.jar$""")))
    implicit def thenableBindable[Value0]
      : Bindable.Aux[scala.scalajs.js.Thenable[Value0], Option[Either[Any, Value0]]] =
      new Bindable[scala.scalajs.js.Thenable[Value0]] {
        type Value = Option[Either[Any, Value0]]
        def toBinding(from: scala.scalajs.js.Thenable[Value0]): Binding[Value] = JsPromiseBinding(from)
      }

  }

  /** A dependent type class that witnesses a type that can be converted to a `Binding[Value]`. */
  @typeclass
  trait Bindable[-From] {
    type Value
    def toBinding(from: From): Binding[Value]
  }

  private[bindable] trait LowPriorityBindableSeq3 {
    @deprecated("Potential naming conflict with `Bindable.constantBindable`.", "1.0.2")
    private[bindable] def constantBindable[Value] = constantsBindableSeq[Value]

    implicit def constantsBindableSeq[Value0]: BindableSeq.Aux[Value0, Value0] = new BindableSeq[Value0] {
      type Value = Value0
      def toBindingSeq(from: Value): BindingSeq[Value] = Constants(from)
    }
  }

  private[bindable] trait LowPriorityBindableSeq2 extends LowPriorityBindableSeq3 {
    implicit def watchableBindableSeq[Value0]: BindableSeq.Aux[Watchable[Value0], Value0] =
      new BindableSeq[Watchable[Value0]] {
        type Value = Value0
        def toBindingSeq(from: Watchable[Value]): BindingSeq[Value] = {
          from match {
            case binding: Binding[Value] =>
              SingletonBindingSeq(binding)
            case bindingSeq: BindingSeq[Value] =>
              bindingSeq
          }
        }
      }
  }

  private[bindable] trait LowPriorityBindableSeq1 extends LowPriorityJsBindableSeq2 {
    @deprecated("Resulting ambiguous implicit values with watchableBindableSeq", "2.1.1")
    private[bindable] def bindingBindableSeq[Value0]: BindableSeq.Aux[Binding[Value0], Value0] =
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
        def toBindingSeq(from: Array[Value0]): BindingSeq[Value] = Constants(ArraySeq.unsafeWrapArray(from): _*)
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
            Constants(ArraySeq.unsafeWrapArray(from.bind): _*)
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

  /** A dependent type class that witnesses a type that can be converted to a `BindingSeq[Value]`. */
  @typeclass
  trait BindableSeq[-From] {
    type Value
    @op("bindSeq", alias = true)
    def toBindingSeq(from: From): BindingSeq[Value]
  }
}
