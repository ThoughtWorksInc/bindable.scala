libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.8" % Test

libraryDependencies += "com.thoughtworks.binding" %%% "binding" % "11.8.1"

libraryDependencies += "com.thoughtworks.binding" %%% "futurebinding" % "11.8.1"

libraryDependencies += "org.typelevel" %%% "simulacrum" % "1.0.0"

libraryDependencies += "com.thoughtworks.enableIf" %% "enableif" % "1.1.7"

// Enable macro annotation by scalac flags for Scala 2.13
scalacOptions ++= {
  import Ordering.Implicits._
  if (VersionNumber(scalaVersion.value).numbers >= Seq(2L, 13L)) {
    Some("-Ymacro-annotations")
  } else {
    None
  }
}

// Enable macro annotation by compiler plugins for Scala 2.12
libraryDependencies ++= {
  import Ordering.Implicits._
  if (VersionNumber(scalaVersion.value).numbers >= Seq(2L, 13L)) {
    None
  } else {
    Some(compilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full))
  }
}
