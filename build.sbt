import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType} 

Global / parallelExecution := false

publish / skip := true

organization in ThisBuild := "com.thoughtworks.binding"

lazy val bindable = crossProject(JVMPlatform, JSPlatform) in file(".")
