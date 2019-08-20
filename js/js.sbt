enablePlugins(Example)

libraryDependencies += "com.thoughtworks.binding" %%% "jspromisebinding" % "11.7.0+150-e57da363"

libraryDependencies += "com.thoughtworks.binding" %%% "dom" % "11.8.1" % Test

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.0")

jsDependencies in Test += RuntimeDOM

inConfig(Test) {
  jsEnv := RhinoJSEnv().value
}
