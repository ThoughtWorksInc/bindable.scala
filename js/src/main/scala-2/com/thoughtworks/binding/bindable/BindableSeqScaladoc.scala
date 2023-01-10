package com.thoughtworks.binding.bindable

/** @example
  *   This type class is internally used in the [[org.lrng.binding.html]] annotation, automatically converting any
  *   compatible values into [[com.thoughtworks.binding.Binding.BindingSeq]], injecting into a HTML template.
  *   {{{
  *          import org.lrng.binding.html
  *          import org.scalajs.dom._
  *          @html
  *          def myBinding = <span>Single Element</span>
  *
  *          @html
  *          def myBindingSeq = <span>Element 1</span><span>Element 2</span>
  *
  *          @html
  *          def myBindingOrBindingSeq(singleElement: Boolean) = {
  *            if (singleElement) {
  *              <span>Single Element</span>
  *            } else {
  *              <span>Element 1</span><span>Element 2</span>
  *            }
  *          }
  *
  *          @html
  *          def mySection = <section>
  *            {myBinding.bind}
  *            {myBinding}
  *            {myBindingSeq}
  *            {Binding{myBindingSeq.all.bind.toSeq}}
  *            {myBindingSeq.all.bind.toSeq}
  *            {myBindingOrBindingSeq(true)}
  *            {myBindingOrBindingSeq(false)}
  *          </section>
  *
  *          val root = document.createElement("span")
  *          html.render(root, mySection)
  *
  *          root.innerHTML should be(
  *            """<section>
  *              <span>Single Element</span>
  *              <span>Single Element</span>
  *              <span>Element 1</span><span>Element 2</span>
  *              <span>Element 1</span><span>Element 2</span>
  *              <span>Element 1</span><span>Element 2</span>
  *              <span>Single Element</span>
  *              <span>Element 1</span><span>Element 2</span>
  *            </section>"""
  *          )
  *   }}}
  */
private[bindable] trait BindableSeqScaladoc
