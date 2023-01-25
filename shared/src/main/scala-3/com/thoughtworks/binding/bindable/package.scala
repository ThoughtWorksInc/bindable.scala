package com.thoughtworks.binding.bindable
import com.thoughtworks.binding._
import com.thoughtworks.binding.Binding._
import scala.collection.immutable.ArraySeq
import scala.language.implicitConversions

given [From, Value](using
    bindable: Bindable.Lt[From, Value]
): Conversion[From, Value] with
  def apply(from: From) = bindable.toBinding(from)
end given

extension [From](from: From)(using bindable: Bindable[From])
  def toBinding: Binding[bindable.Value] = bindable.toBinding(from)
end extension

extension [From](from: From)(using bindableSeq: BindableSeq[From])
  def bindSeq: BindingSeq[bindableSeq.Value] = bindableSeq.toBindingSeq(from)
  def toBindingSeq: BindingSeq[bindableSeq.Value] =
    bindableSeq.toBindingSeq(from)
end extension
