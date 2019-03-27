publish / skip := true

organization in ThisBuild := "com.thoughtworks.binding"

lazy val bindable = crossProject in file(".")
lazy val bindableJVM = bindable.jvm
lazy val bindableJS = bindable.js
