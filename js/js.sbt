enablePlugins(Example)

libraryDependencies += "com.thoughtworks.binding" %%% "jspromisebinding" % "11.7.0+150-e57da363"

libraryDependencies += "com.thoughtworks.binding" %%% "dom" % "11.7.0" % Test

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.10")

jsDependencies in Test += RuntimeDOM

inConfig(Test) {
  jsEnv := RhinoJSEnv().value
}
