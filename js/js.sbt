enablePlugins(ScalaJSBundlerPlugin)

enablePlugins(Example)

libraryDependencies += "com.thoughtworks.binding" %%% "jspromisebinding" % "12.1.0"

libraryDependencies += "org.lrng.binding" %%% "html" % "1.0.3" % Optional

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3")

requireJsDomEnv in Test := true
