package LevelDescription

class Waves {
  val easyWaves: List[Wave] = List(
    Wave(1, 4, 0, 0),
    Wave(2, 6, 0, 0),
    Wave(3, 8, 0, 0),
    Wave(4, 6, 2, 0),
    Wave(5, 8, 2, 0),
    Wave(6, 4, 2, 1),
    Wave(7, 10, 3, 1),
    Wave(8, 8, 4, 0),
    Wave(9, 12, 5, 0),
    Wave(10, 12, 5, 1)
  )

  val mediumWaves: List[Wave] = List(
    Wave(1, 4, 0, 0),
    Wave(2, 8, 0, 0),
    Wave(3, 10, 0, 0),
    Wave(4, 8, 3, 0),
    Wave(5, 12, 3, 0),
    Wave(6, 8, 5, 1),
    Wave(7, 12, 6, 1),
    Wave(8, 14, 7, 0),
    Wave(9, 16, 8, 1),
    Wave(10, 20, 8, 1)
  )

  val hardWaves: List[Wave] = List(
    Wave(1, 6, 0, 0),
    Wave(2, 10, 0, 0),
    Wave(3, 12, 0, 0),
    Wave(4, 12, 3, 0),
    Wave(5, 16, 3, 0),
    Wave(6, 14, 5, 1),
    Wave(7, 20, 6, 1),
    Wave(8, 20, 7, 0),
    Wave(9, 24, 8, 1),
    Wave(10, 28, 12, 1)
  )

  val oofpWaves: List[Wave] = List(
    Wave(1, 8, 0, 0),
    Wave(2, 12, 0, 0),
    Wave(3, 16, 0, 0),
    Wave(4, 16, 4, 0),
    Wave(5, 20, 6, 0),
    Wave(6, 20, 8, 1),
    Wave(7, 24, 10, 1),
    Wave(8, 28, 12, 0),
    Wave(9, 32, 14, 1),
    Wave(10, 36, 16, 1)
  )


}
