enablePlugins(Example)

libraryDependencies += "com.thoughtworks.binding" %%% "jspromisebinding" % "12.0.0-M0"

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3")

jsDependencies in Test += RuntimeDOM

inConfig(Test) {
  jsEnv := RhinoJSEnv().value
}
