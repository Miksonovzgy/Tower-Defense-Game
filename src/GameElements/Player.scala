package GameElements
import processing.core.PApplet

class Player(var x: Int,
             var y: Int,
             var size: Int,
             var rateOfFire : Int,
            )
extends PApplet{

  var lastShotTime: Int = 0

  def canFire() : Boolean  = {
    val currentTime = millis()

    val timeElapsed = currentTime - lastShotTime

    if (timeElapsed >= rateOfFire) {
      lastShotTime = currentTime
      true
    } else {
      false
    }
  }
}

