libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.7" % Test

libraryDependencies += "com.thoughtworks.binding" %%% "binding" % "11.7.0"

libraryDependencies += "com.github.mpilquist" %%% "simulacrum" % "0.15.0"

libraryDependencies += "com.thoughtworks.enableIf" %% "enableif" % "1.1.6"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
