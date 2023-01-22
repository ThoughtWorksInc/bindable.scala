package com.thoughtworks.binding.bindable

import scala.scalajs.js
import com.thoughtworks.binding._
import com.thoughtworks.binding.Binding._

private[bindable] object JvmOrJs {

  trait LowPriorityJsBindableSeq2 extends LowPriorityBindableSeq2 {

    implicit def jsArrayBindableSeq[Value0]
        : BindableSeq.Aux[js.Array[Value0], Value0] =
      new BindableSeq[js.Array[Value0]] {
        type Value = Value0
        def toBindingSeq(from: js.Array[Value0]): BindingSeq[Value] =
          Binding.Constants(
            scalajs.runtime.toScalaVarArgs(from): _*
          )
      }
  }

  trait LowPriorityJsBindableSeq0 extends LowPriorityBindableSeq0 {

    implicit def bindingJsArrayBindableSeq[Value0]
        : BindableSeq.Aux[Binding[js.Array[Value0]], Value0] =
      new BindableSeq[Binding[js.Array[Value0]]] {
        type Value = Value0
        def toBindingSeq(from: Binding[js.Array[Value0]]): BindingSeq[Value] =
          Constants(from).flatMapBinding { from =>
            Binding {
              Constants(scalajs.runtime.toScalaVarArgs(from.bind): _*)
            }
          }
      }
  }

  trait BindableJS {

    implicit def thenableBindable[Value0]
        : Bindable.Aux[scala.scalajs.js.Thenable[Value0], Option[
          Either[Any, Value0]
        ]] =
      new Bindable[scala.scalajs.js.Thenable[Value0]] {
        type Value = Option[Either[Any, Value0]]
        def toBinding(from: scala.scalajs.js.Thenable[Value0]): Binding[Value] =
          JsPromiseBinding(from)
      }

  }

}
