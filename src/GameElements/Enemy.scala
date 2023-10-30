package GameElements
import LevelDescription.Waypoint
import processing.core.PApplet.dist

sealed abstract class Enemy(val x : Int , val y : Int,val  dir : Direction,val speed : Int, val color : Color, val health : Int)
{
  val healthToColors: Map[Int, Color] = Map(
    7 -> Color.Black,
    6 -> Color.Purple,
    5 -> Color.DarkGreen,
    4 -> Color.Green,
    3 -> Color.Yellow,
    2 -> Color.Orange,
    1 -> Color.Red)

  def move(dir: Direction, waypoints: List[Waypoint]): Enemy
  def changeHealthCol: Enemy
  def hasCollided(bullets: List[Bullet]): Option[Bullet]

  def clamp(value: Float, min: Float, max: Float): Float = {
    if (value < min) min
    else if (value > max) max
    else value
  }

}


case class RegularEnemy(override val x : Int,
                        override val y: Int,
                        override val dir : Direction,
                        override val speed : Int,
                        override val color: Color = Color.DarkGreen,
                        override val health : Int = 3) extends Enemy(x, y, dir,speed, color, health)
{
  override def move(dir: Direction, waypoints: List[Waypoint]): RegularEnemy = {
    var newDir = dir
    if (waypoints.exists(w => w.isInProximity(this.x, this.y, this.speed))) {
      newDir = waypoints.collect { case w if w.isInProximity(this.x, this.y, this.speed) => w.dirAfter }.head
    }
    this.dir match {
      case north() => RegularEnemy(x, y - speed, newDir, this.speed, this.color, this.health)
      case south() => RegularEnemy(x, y + speed, newDir, this.speed, this.color, this.health)
      case west() => RegularEnemy(x - speed, y, newDir, this.speed, this.color, this.health)
      case east() => RegularEnemy(x + speed, y, newDir, this.speed, this.color, this.health)
    }

  }

  override def hasCollided(bullets: List[Bullet]): Option[Bullet] = {
    bullets.find { bullet =>
      val bulletRadius = 6
      val sideCircleRadius = 15
      val bulletCenterX = bullet.x
      val bulletCenterY = bullet.y
      val closestX = clamp(bulletCenterX, this.x, this.x + 20)
      val closestY = clamp(bulletCenterY, this.y, this.y + 20)
      val distance = dist(bulletCenterX, bulletCenterY, closestX, closestY)
      distance < bulletRadius + sideCircleRadius
    }
  }

  override def changeHealthCol : RegularEnemy = {
    RegularEnemy(x, y, dir, speed, healthToColors(health - 1), health - 1)
  }
}



case class HeavyEnemy(override val x : Int,
                      override val y: Int,
                      override val dir : Direction,
                      override val speed : Int,
                      override val color: Color = Color.Black,
                      override val health : Int = 7) extends Enemy(x, y, dir, speed, color, health) {
  override def move(dir: Direction, waypoints: List[Waypoint]): HeavyEnemy = {
    var newDir = dir
    if (waypoints.exists(w => w.isInProximity(this.x, this.y, this.speed))) {
      newDir = waypoints.collect { case w if w.isInProximity(this.x, this.y, this.speed) => w.dirAfter }.head
    }
    this.dir match {
      case north() => HeavyEnemy(x, y - speed, newDir, this.speed, this.color, this.health)
      case south() => HeavyEnemy(x, y + speed, newDir, this.speed, this.color, this.health)
      case west() => HeavyEnemy(x - speed, y, newDir, this.speed, this.color, this.health)
      case east() => HeavyEnemy(x + speed, y, newDir, this.speed, this.color, this.health)
    }

  }

  override def changeHealthCol: HeavyEnemy = {
    HeavyEnemy(x, y, dir, speed, healthToColors(health - 1), health - 1)
  }


    override def hasCollided(bullets: List[Bullet]): Option[Bullet] = {
      bullets.find { bullet =>
        val bulletRadius = 6
        val closestX = clamp(bullet.x, x, x+25)
        val closestY = clamp(bullet.y, y, y+25)

        val distance = dist(bullet.x, bullet.y, closestX, closestY)

        distance < bulletRadius
      }
    }


}


case class Boss(override val x : Int,
                      override val y: Int,
                      override val dir : Direction,
                      override val speed : Int,
                      override val color: Color = Color.Black,
                      override val health : Int = 25) extends Enemy(x, y, dir, speed, color, health) {
  override def move(dir: Direction, waypoints: List[Waypoint]): Boss = {
    var newDir = dir
    if (waypoints.exists(w => w.isInProximity(this.x, this.y, this.speed))) {
      newDir = waypoints.collect { case w if w.isInProximity(this.x, this.y, this.speed) => w.dirAfter }.head
    }
    this.dir match {
      case north() => Boss(x, y - speed, newDir, this.speed, this.color, this.health)
      case south() => Boss(x, y + speed, newDir, this.speed, this.color, this.health)
      case west() => Boss(x - speed, y, newDir, this.speed, this.color, this.health)
      case east() => Boss(x + speed, y, newDir, this.speed, this.color, this.health)
    }

  }

  override def changeHealthCol: Boss = {
    Boss(x, y, dir, speed, Color.Black, health - 1)
  }

  override def hasCollided(bullets: List[Bullet]): Option[Bullet] = {
    bullets.find { bullet =>
      val bulletCenterX = bullet.x
      val bulletCenterY = bullet.y
      val bulletRadius = 6

      val leftEyeX = x + 20
      val leftEyeY = y + 35
      val leftEyeRadius = 15

      val rightEyeX = x + 60
      val rightEyeY = y + 35
      val rightEyeRadius = 15

      val chestX = x + 40
      val chestY = y + 55
      val chestRadius = 25

      val backX = x + 40
      val backY = y + 15
      val backRadius = 25

      val distLeftEye = dist(bulletCenterX, bulletCenterY, leftEyeX, leftEyeY)
      val distRightEye = dist(bulletCenterX, bulletCenterY, rightEyeX, rightEyeY)
      val distChest = dist(bulletCenterX, bulletCenterY, chestX, chestY)
      val distBack = dist(bulletCenterX, bulletCenterY, backX, backY)

      distLeftEye < leftEyeRadius + bulletRadius || distRightEye < rightEyeRadius + bulletRadius ||
        distChest < chestRadius + bulletRadius || distBack < backRadius + bulletRadius

    }
  }



}