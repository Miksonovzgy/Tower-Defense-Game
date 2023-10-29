package GameElements

sealed class Direction {
}

case class north() extends Direction
case class south() extends Direction
case class west() extends Direction
case class east() extends Direction

