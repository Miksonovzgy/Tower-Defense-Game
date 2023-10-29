package LevelDescription

import GameElements.{east, north, south, west}

class PossiblePaths {

  val possiblePaths : List[List[Waypoint]] = List(
    List(
      LevelDescription.Waypoint(150, 250, north()),
      LevelDescription.Waypoint(150, 100, east()),
      LevelDescription.Waypoint(300, 100, south()),
      LevelDescription.Waypoint(300, 400, east()),
      LevelDescription.Waypoint(370, 400, north()),
      LevelDescription.Waypoint(370, 200, east()),
      LevelDescription.Waypoint(450, 200, south()),
      LevelDescription.Waypoint(450, 430, east())),
    List(
      LevelDescription.Waypoint(150, 250, north()),
      LevelDescription.Waypoint(150, 110, east()),
      LevelDescription.Waypoint(290, 110, south()),
      LevelDescription.Waypoint(290, 400, east()),
      LevelDescription.Waypoint(350, 400, north()),
      LevelDescription.Waypoint(350, 200, east()),
      LevelDescription.Waypoint(450, 200, north()),
      LevelDescription.Waypoint(450, 50, east())),
    List(
      LevelDescription.Waypoint(100, 290, east()),
      LevelDescription.Waypoint(390, 290, north()),
      LevelDescription.Waypoint(390, 90, west()),
      LevelDescription.Waypoint(190, 90, south()),
      LevelDescription.Waypoint(190, 390, east()),
      LevelDescription.Waypoint(450, 390, north()),
      LevelDescription.Waypoint(450,150, east())),
    List(
      LevelDescription.Waypoint(100, 90, east()),
      LevelDescription.Waypoint(190, 90, south()),
      LevelDescription.Waypoint(190, 390, west()),
      LevelDescription.Waypoint(70, 390, north()),
      LevelDescription.Waypoint(70, 185, east()),
      LevelDescription.Waypoint(330, 185, south()),
      LevelDescription.Waypoint(330, 430, east()),
      LevelDescription.Waypoint(490, 430, north()),
      LevelDescription.Waypoint(490, 250, west()),
      LevelDescription.Waypoint(430, 250, north()),
      LevelDescription.Waypoint(430, 100, east()))
  )

  def getPossiblePaths(index : Int) : List[Waypoint] = {
    possiblePaths(index)
  }

}
