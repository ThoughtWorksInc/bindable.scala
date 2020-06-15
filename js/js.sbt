enablePlugins(ScalaJSBundlerPlugin)

enablePlugins(Example)

libraryDependencies += "com.thoughtworks.binding" %%% "jspromisebinding" % "12.0.0"

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3")

requireJsDomEnv in Test := true
