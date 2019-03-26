enablePlugins(Example)

libraryDependencies += "com.thoughtworks.binding" %%% "dom" % "11.6.1" % Test

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.9")

jsDependencies in Test += RuntimeDOM

inConfig(Test) {
  jsEnv := RhinoJSEnv().value
}
