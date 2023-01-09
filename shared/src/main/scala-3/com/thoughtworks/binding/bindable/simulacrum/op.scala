package com.thoughtworks.binding.bindable.simulacrum

import scala.annotation.StaticAnnotation
import scala.annotation.Annotation

private[bindable] final class op(name: String, alias: Boolean = false) extends Annotation with StaticAnnotation
