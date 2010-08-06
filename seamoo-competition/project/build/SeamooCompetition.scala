import sbt._

class SeamooCompetitionProject(info: ProjectInfo) extends DefaultProject(info) {

  println(Path.userHome)
  val mavenLocal = "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"

}
