libraryDependencies += "org.scalatest" %%% "scalatest" % "3.2.15" % Test

libraryDependencies += "com.thoughtworks.binding" %%% "futurebinding" % "12.1.0"

libraryDependencies += "com.thoughtworks.binding" %%% "binding" % "12.1.0+110-53fd3428"

libraryDependencies += {
  import Ordering.Implicits._
  if (VersionNumber(scalaVersion.value).numbers < Seq(3L)) {
    Some("org.typelevel" %% "simulacrum" % "1.0.1")
  } else {
    None
  }
}
scalacOptions += "-Ymacro-annotations"
