package LevelDescription

import GameElements.Direction

case class Waypoint(x : Int, y : Int, dirAfter : Direction){
def isInProximity(x : Int, y : Int, factor : Int) : Boolean = {
  x >= this.x - factor && x <= this.x + factor && y <= this.y + factor && y >= this.y - factor
}

}
