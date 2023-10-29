package GameElements

case class Bullet(x: Float, y: Float, speed: Float, directionX: Float = 0, directionY: Float = 0) {

  def update(): Bullet = {
    Bullet(x + speed * directionX, y + speed * directionY, this.speed, this.directionX, this.directionY)
  }

}

