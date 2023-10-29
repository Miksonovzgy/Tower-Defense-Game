package LevelDescription

abstract class Difficulty {
}

case class easy() extends Difficulty
case class medium() extends Difficulty
case class hard() extends Difficulty
case class OOFPExam(description : String = "Just dont do it") extends Difficulty
