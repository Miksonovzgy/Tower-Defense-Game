package GameElements
case class Color(red: Float, green: Float, blue: Float, alpha: Float) {

  def this(red: Float, green: Float, blue: Float) = this(red, green, blue, 255)

}

object Color {

  def apply(red: Float, green: Float, blue: Float): Color = new Color(red, green, blue)

  val LawnGreen = Color(124, 252,   0)
  val DarkGreen = Color(  0, 100,   0)
  val Black     = Color(  0,   0,   0)
  val Gray      = Color(100, 100, 100)
  val Red       = Color(255,   0,   0)
  val White     = Color(255, 255, 255)
  val LightBlue = Color(173, 216, 230)
  val Yellow    = Color(255, 255,   0)
  val Orange    = Color(255, 165,   0)
  val Blue      = Color(  0,   0, 255)
  val Green     = Color(  0, 255,   0)
  val Purple    = Color(128,   0, 128)

}