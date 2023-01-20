package com.thoughtworks.binding

import com.thoughtworks.binding._
import com.thoughtworks.binding.Binding._
import simulacrum._
import scala.language.implicitConversions
import scala.collection.immutable.ArraySeq

package object bindable
    extends Bindable.ToBindableOps
    with BindableSeq.ToBindableSeqOps {
  implicit def bindableToBinding[From, Value](from: From)(implicit
      bindable: Bindable.Lt[From, Value]
  ): Binding[Value] =
    bindable.toBinding(from)
}
