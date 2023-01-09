package com.thoughtworks.binding.bindable {

  import com.thoughtworks.binding._
  import com.thoughtworks.binding.Binding._
  import scala.collection.immutable.ArraySeq
  import simulacrum._

  import JvmOrJs._

  import scala.concurrent.{ExecutionContext, Future}
  import scala.util.Try

  private[bindable] trait LowPriorityBindable0 {

    implicit def constantBindable[Value0]: Bindable.Aux[Value0, Value0] = new Bindable[Value0] {
      type Value = Value0
      def toBinding(from: Value): Binding[Value] = Constant(from)
    }

  }

  object Bindable extends BindableJS with LowPriorityBindable0 {
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

    implicit def futureBindable[Value0](implicit
        executionContext: ExecutionContext
    ): Bindable.Aux[Future[Value0], Option[Try[Value0]]] =
      new Bindable[Future[Value0]] {
        type Value = Option[Try[Value0]]
        def toBinding(from: Future[Value0]): Binding[Value] = FutureBinding(from)
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

    private[bindable] def bindingSeqBindableSeq[Value0]: BindableSeq.Aux[BindingSeq[Value0], Value0] =
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
          Constants(from).flatMapBinding { from =>
            Binding {
              Constants(ArraySeq.unsafeWrapArray(from.bind): _*)
            }
          }
      }

    implicit def bindingScalaSeqBindableSeq[Value0]: Aux[Binding[Seq[Value0]], Value0] =
      new BindableSeq[Binding[Seq[Value0]]] {
        type Value = Value0
        def toBindingSeq(from: Binding[Seq[Value0]]): BindingSeq[Value] =
          Constants(from).flatMapBinding { from =>
            Binding {
              Constants(from.bind: _*)
            }
          }
      }

  }

  /** A dependent type class that witnesses a type that can be converted to a `BindingSeq[Value]`.
    *
    * @inheritdoc
    */
  @typeclass
  trait BindableSeq[-From] extends BindableSeqScaladoc {
    type Value
    @op("bindSeq", alias = true)
    def toBindingSeq(from: From): BindingSeq[Value]
  }
}
