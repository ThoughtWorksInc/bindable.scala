enablePlugins(ScalaJSBundlerPlugin)

enablePlugins(Example)

libraryDependencies += "com.thoughtworks.binding" %%% "jspromisebinding" % "12.1.1"

libraryDependencies ++= PartialFunction.condOpt(scalaBinaryVersion.value) {
  case "2.13" =>
    "com.yang-bo" %%% "html" % "2.0.1" % Optional
}

libraryDependencies ++= PartialFunction.condOpt(scalaBinaryVersion.value) {
  case "2.13" =>
    compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3")
}

requireJsDomEnv in Test := true

Test / scalacOptions ++= PartialFunction.condOpt(scalaBinaryVersion.value) {
  case "2.13" =>
    "-Ymacro-annotations"
}

// Workaround for https://github.com/scalacenter/scalajs-bundler/issues/438
version in webpack := if (VersionNumber(scalaJSVersion).numbers >= Seq(1)) "5.88.1" else "4.46.0"
