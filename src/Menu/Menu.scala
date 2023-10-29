package Menu
import processing.core._
import processing.core.PConstants
import LevelDescription._
import ddf.minim.{Minim, AudioPlayer, AudioSample}

object DifficultySingleton {
  var selectedDifficulty: Difficulty = LevelDescription.easy()
}
class Menu extends PApplet {
  var menuButtons: List[Button] = List.empty
  var optionsButtons : List[Button] = List.empty
  var instructionsButtons : List[Button] = List.empty
  var instructionsShowed = false
  var optionsShowed = false
  var skul : PImage = null
  val WIDTH = 640
  val HEIGHT = 480
  val backButton = new Button("Back", WIDTH / 2, 400)
  val minim = new Minim(this)
  var music : AudioPlayer = null
  var menuHit : AudioSample = null



  private val INSTRUCTIONS_STRING = "The purpose of the game is to defend the right side of the screen from the very evil enemies. \n " +
                            "If any of the enemies touches the right border of the screen, \n Your repl_simplify will fall into an infinite recursive spiral of simplification!!! \n (Meaning, You die).\n " +
                            "There are 4 levels of difficulty, which You can set in the Options.\n Also after a certain amount of waves, the bosses will start showing up. \n Although it might be difficult to reach this point" +
                            "\n Each level changes the composition of enemy waves, making them substantially harder. \nThere are 10 waves for each difficulty,"+
                            "and if You manage\n to keep the right side of the map clear, You win! \n Have fun!"

  private def diffString : String = DifficultySingleton.selectedDifficulty match{
    case e : easy => "Easy"
    case m : medium => "Mid"
    case h : hard => "Hard"
    case o : OOFPExam => "Bruh"
    case _ => "No such difficulty"
  }


  override def settings(): Unit = {
    size(WIDTH,HEIGHT)
  }


  override def setup(): Unit = {

    val startButton = new Button("Start", width / 2, height / 2 - 120)
    val instructionsButton = new Button("Instructions", width / 2, height / 2 - 60)
    val optionsButton = new Button("Options", width / 2, height / 2)
    val quitButton = new Button("Quit", width / 2, height / 2 + 60)

    val easyButton = new Button("Easy", width / 2, height / 2 - 120)
    val mediumButton = new Button("Medium", width / 2, height / 2 - 60)
    val hardButton = new Button("Hard", width / 2, height / 2)
    val impButton = new Button("OOFP Exam", width / 2, height / 2 + 60)

     optionsButtons = List(easyButton, mediumButton, hardButton, impButton, backButton)
     menuButtons = List(startButton, instructionsButton,optionsButton, quitButton)
     instructionsButtons = List(backButton)
    music = minim.loadFile("../resources/music/menu.mp3")
    menuHit = minim.loadSample("../resources/music/menuHit.mp3")
    music.loop()


    skul = loadImage("../skul/skul.png")
  }

  override def draw(): Unit = {
   // music.loop()
    if(instructionsShowed) {
      background(0)
      textAlign(PConstants.CENTER, PConstants.CENTER)
      textSize(14)
      text(INSTRUCTIONS_STRING, width/2, height/2)
      displayButtons(instructionsButtons)
    }
    else if(optionsShowed){
      background(30, 30, 30)
      displayButtons(optionsButtons)
      skul.resize(100,100)
      image(skul,optionsButtons(2).x + 35, optionsButtons(2).y+10)
      textSize(20)
      textAlign(PConstants.CENTER)
      text("Selected difficulty: " + diffString, width/2, 30)



      }
    else {
      background(30, 30, 30)
      displayButtons(menuButtons)
    }
  }

  private def drawButton(b: Button): Unit = {
    stroke(255)
    fill(if (b.highlighted) color(255, 0, 0) else color(150))
    rectMode(PConstants.CENTER)
    rect(b.x, b.y, b.width, b.height)
    fill(255)
    textAlign(PConstants.CENTER, PConstants.CENTER)
    textSize(20)
    text(b.label, b.x, b.y)
  }

  private def buttonClickedReaction(b : Button) : Unit = {
    menuHit.trigger()
    b.label match {
      case "Start" => {music.close(); PApplet.main("Game.TowerDefense")}
      case "Instructions" => instructionsShowed = true
      case "Options" => optionsShowed = true
      case "Quit" => exit()
      case "Back" => {instructionsShowed = false; optionsShowed = false}
      case "Easy" => DifficultySingleton.selectedDifficulty = LevelDescription.easy()
      case "Medium" =>  DifficultySingleton.selectedDifficulty = LevelDescription.medium()
      case "Hard" =>  DifficultySingleton.selectedDifficulty = LevelDescription.hard()
      case "OOFP Exam" =>  DifficultySingleton.selectedDifficulty = LevelDescription.OOFPExam()
      case _ => println("No such button")

    }
  }

  def displayButtons(buttons : List[Button]) : Unit = {
    buttons.foreach(button => {
      drawButton(button)
      if (button.isHovered(mouseX, mouseY)) {
        button.highlight()
      } else {
        button.noHighlight()
      }
    })

  }

  override def mousePressed(): Unit = {
    (optionsShowed,instructionsShowed) match{
      case (true,false) => checkForButtonClick(optionsButtons)
      case (false,true) => checkForButtonClick(instructionsButtons)
      case _ => checkForButtonClick(menuButtons)
  }

  def checkForButtonClick(buttons : List[Button]) : Unit = {
    buttons.foreach(button => {
      if (button.isHovered(mouseX, mouseY)) {
        buttonClickedReaction(button)
      }
    })
  }


}
  class Button(val label: String, val x: Float, val y: Float) {
    val width = 200
    val height = 60
    var highlighted = false

    def isHovered(mx: Float, my: Float): Boolean = {
      mx >= x - width / 2 && mx <= x + width / 2 && my >= y - height / 2 && my <= y + height / 2
    }

    def highlight(): Unit = {
      highlighted = true
    }

    def noHighlight(): Unit = {
      highlighted = false
    }
  }
}

object Menu
{
  def main(args:Array[String]): Unit = {
    PApplet.main("Menu.Menu")
  }
}
