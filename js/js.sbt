enablePlugins(ScalaJSBundlerPlugin)

enablePlugins(Example)

libraryDependencies += "com.thoughtworks.binding" %%% "jspromisebinding" % "12.0.0+76-4d35b9b5"

libraryDependencies ++= PartialFunction.condOpt(scalaBinaryVersion.value) { case "2.13" =>
  "com.yang-bo" %%% "html" % "2.0.0" % Optional
}

libraryDependencies ++= PartialFunction.condOpt(scalaBinaryVersion.value) { case "2.13" =>
  compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3")
}

requireJsDomEnv in Test := true

Test / scalacOptions ++= PartialFunction.condOpt(scalaBinaryVersion.value) { case "2.13" =>
  "-Ymacro-annotations"
}
