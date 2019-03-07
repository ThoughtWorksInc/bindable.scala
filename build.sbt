publishArtifact := false

sonatypeProfileName := "com.thoughtworks.binding"

organization in ThisBuild := "com.thoughtworks.binding.bindable"

crossScalaVersions in ThisBuild := Seq("2.10.7", "2.11.12", "2.12.8")

lazy val bindable = crossProject in file(".")
lazy val bindableJVM =bindable.jvm
lazy val bindableJS =bindable.js
