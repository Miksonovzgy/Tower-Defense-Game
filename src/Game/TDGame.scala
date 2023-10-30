package Game
import processing.core._
import GameElements._
import LevelDescription._
import Menu.DifficultySingleton
import ddf.minim.{Minim, AudioPlayer, AudioSample}

import scala.collection.immutable.Queue
import scala.util.Random

object TDGame
{
  def main(args:Array[String]): Unit = {
    PApplet.main("Game.TowerDefense")
  }
}


class TowerDefense extends PApplet {
  private val WIDTH = 640
  private val HEIGHT = 480
  private val BULLET_SIZE = 12
  private val BULLET_SPEED = 10
  private val player = new Player(560, 220, 50, 250)
  private var enemies : List[Enemy] = List.empty
  private var bullets : List[Bullet] = List.empty
  private val possiblePaths = new PossiblePaths()
  private val waves : Waves = new Waves
  private val wavesList : List[Wave] = getWavesByDiff(DifficultySingleton.selectedDifficulty)

  private var currWave = wavesList.head
  private var bgImage : PImage = null
  private val randomMapSeed: Int = Random.between(0,4)
  var waypoints : List[Waypoint] = possiblePaths.possiblePaths(randomMapSeed)


  private val regEnemySpeed = 2
  private val heavyEnemySpeed = 2
  private val bossSpeed = 1
  private var waveNum : String = currWave.numWave.toString

  private var totalRegulars = currWave.numRegs
  private var totalHeavies = currWave.numHeavies
  private var totalBosses = currWave.numBosses
  private var enemiesLeftToSpawn = totalHeavies + totalRegulars + totalBosses
  private val rateInSeconds = 0.5
  private var lastSpawnTime = 0L
  private var enemiesRatio = getEnemyRatio(totalHeavies, totalRegulars)
  private val moreCommonType: Enemy = getMoreCommonType(totalHeavies, totalRegulars)
  private val lessCommonType: Enemy = getLessCommonType(moreCommonType)
  private var spawningQueue : Queue[Enemy] = prepareSpawningQueue(moreCommonType, lessCommonType)

  var f : PFont = null
  val minim = new Minim(this)
  var music : AudioPlayer = null
  var shot : AudioSample = null
  var newWave : AudioSample = null
  var gameOverSound : AudioSample = null
  var enemyHitSound : AudioSample = null
  var gameOverTimes : Int = 0

  override def settings(): Unit = {
    size(WIDTH, HEIGHT)
  }

  override def setup(): Unit = {
    noCursor()
    frameRate(60)
    f = createFont("Arial",16,false)
    bgImage = loadImage("../resources/maps/pngs/map" + randomMapSeed +".png")
    music = minim.loadFile("../resources/music/game.mp3")
    shot = minim.loadSample("../resources/music/shot.mp3")
    newWave = minim.loadSample("../resources/music/newWave.mp3")
    gameOverSound = minim.loadSample("../resources/music/gameOver.mp3")
    enemyHitSound = minim.loadSample("../resources/music/enemyHurt.mp3")
    music.loop()


  }

  override def draw(): Unit = {
    if(!isGameOver) {
      background(bgImage)
      fill(0)
      drawPlayer(player)

      if (enemies.isEmpty && spawningQueue.nonEmpty) {
          spawnFirstEnemy()
      }
      if(spawningQueue.nonEmpty) enemies = spawnEnemy(spawningQueue)

      drawAndMoveEnemies()

      drawMoveAndHitDetectBullets()

      drawCrosshair()

      drawWaveText()

      if(enemies.isEmpty && currWave.numWave != 10) {
        handleNewWave()
      }
    }
    else{
      if(gameOverTimes == 0) gameOverSound.trigger(); gameOverTimes += 1
      displayGameOver()
      music.close()
    }

  }

  private def drawBossHealth(boss : Boss) : Unit = {
    fill(0, 255, 0)
    val healthBarWidth = boss.health * 16
    textFont(f, 20)
    text("Why do I hear Boss music?", 200,30)
    rect(120, 40, healthBarWidth, 20)
  }
  private def spawnEnemy(enQueue : Queue[Enemy]) : List[Enemy] = {
    if (frameCount - lastSpawnTime >= rateInSeconds * frameRate) {
      val (newEnemy, leftoverQueue) = enQueue.dequeue
      spawningQueue = leftoverQueue
      lastSpawnTime = frameCount
      return enemies.appended(newEnemy)
    }
    enemies
  }
  private def prepareSpawningQueue(moreCommonType : Enemy, lessCommonType : Enemy) : Queue[Enemy]  = {
    var moreCommonSpawnWindow = enemiesRatio
    var lessCommonCount = (totalRegulars min totalHeavies)

    var spawningQueue : Queue[Enemy] = Queue.empty[Enemy]

    while (enemiesLeftToSpawn > 0) {

      if(enemiesLeftToSpawn == 1 && totalBosses != 0) {spawningQueue = spawningQueue.enqueue(Boss(50,waypoints.head.y, east(), bossSpeed)); enemiesLeftToSpawn-=1}
      else if (moreCommonSpawnWindow > 0) {
        spawningQueue = moreCommonType match{
          case re : RegularEnemy => spawningQueue.enqueue(RegularEnemy(50, waypoints.head.y, east(), regEnemySpeed))
          case he : HeavyEnemy => spawningQueue.enqueue(HeavyEnemy(50, waypoints.head.y, east(), heavyEnemySpeed))
        }
        moreCommonSpawnWindow -= 1
        enemiesLeftToSpawn -=1
      }

      else if(lessCommonCount > 0){
        spawningQueue = lessCommonType match {
          case re: RegularEnemy => spawningQueue.enqueue(RegularEnemy(50, waypoints.head.y, east(), regEnemySpeed))
          case he: HeavyEnemy => spawningQueue.enqueue(HeavyEnemy(50, waypoints.head.y, east(), heavyEnemySpeed))
        }
        moreCommonSpawnWindow = enemiesRatio
        lessCommonCount -= 1
        enemiesLeftToSpawn-=1
      }
      else{
        spawningQueue = moreCommonType match{
          case re: RegularEnemy => spawningQueue.enqueue(RegularEnemy(50, waypoints.head.y, east(), regEnemySpeed))
          case he: HeavyEnemy => spawningQueue.enqueue(HeavyEnemy(50, waypoints.head.y, east(), heavyEnemySpeed))
        }
        enemiesLeftToSpawn-=1
      }
    }
    spawningQueue
  }
  private def getLessCommonType(moreCommonType : Enemy) : Enemy = {
    moreCommonType match{
    case r : RegularEnemy => HeavyEnemy(1,1,east(),1)
    case h : HeavyEnemy => RegularEnemy(1,1,east(),1)
    }
  }
  private def getMoreCommonType(totalHeavies : Int, totalRegs : Int) : Enemy = {
    if (totalRegs > totalHeavies) RegularEnemy(1, 1, east(), 1)
    else HeavyEnemy(1, 1, east(), 1)

  }
  private def getEnemyRatio(totalHeavies : Int, totalRegs : Int) : Int = {
    if(totalHeavies > 0) (totalRegulars max totalHeavies) / (totalRegulars min totalHeavies)
    else totalRegulars
  }
  private def spawnFirstEnemy() : Unit = {
    val (firstEnemy, leftoverQueue) = spawningQueue.dequeue
    spawningQueue = leftoverQueue
    enemies = enemies.appended(firstEnemy)
    lastSpawnTime = frameCount
  }
 private def handleNewWave(): Unit = {
   getNewWave(currWave.numWave)
   enemiesRatio = getEnemyRatio(totalHeavies, totalRegulars)
   val moreCommonType: Enemy = getMoreCommonType(totalHeavies, totalRegulars)
   val lessCommonType: Enemy = getLessCommonType(moreCommonType)
   spawningQueue = prepareSpawningQueue(moreCommonType, lessCommonType)
   newWave.trigger()
 }

  private def drawAndMoveEnemies() : Unit = {
    fill(250, 0, 0)
    enemies.foreach(drawEnemy)
    enemies = enemies.map(en => en.move(en.dir, waypoints))
  }

  private def drawMoveAndHitDetectBullets() : Unit = {
    bullets.foreach(drawBullet)
    bullets = bullets.map(bul => bul.update())
    bullets = enemiesHitDetect()
  }
  private def drawPlayer(Player : Player) : Unit = {
    rect(Player.x, Player.y, Player.size, Player.size,10)
  }

  private def drawCrosshair() : Unit = {
    val mx = mouseX
    val my = mouseY
    fill(255,0,0)
    rect(mx-8, my,8,4)
    rect(mx+8, my,8,4)
    rect(mx+2, my-8,4,8)
    rect(mx+2, my+5,4,8)
  }

  private def drawWaveText() : Unit = {
    textFont(f, 25)
    fill(255)
    text("Wave Number: " + waveNum, 20, 460)
  }
  private def drawBullet(bullet : Bullet) : Unit = {
    val color = Color.Purple
    fill(color.red, color.green, color.blue)
    circle(bullet.x, bullet.y, BULLET_SIZE)
  }

  override def mousePressed(): Unit = {
    if(player.canFire()) {
      shot.trigger()
      val mouseVector = new PVector(mouseX, mouseY)
      val bulletVector = new PVector(player.x, player.y + 25)
      val direction = mouseVector.sub(bulletVector)
      direction.normalize()
      val newBullet = Bullet(player.x, player.y + 25, BULLET_SPEED, direction.x, direction.y)
      bullets = bullets :+ newBullet
    }
  }

  private def enemiesHitDetect(): List[Bullet] = {
    enemies.foldLeft(bullets) { (remainingBullets, enemy) =>
      enemy.hasCollided(remainingBullets) match {
        case Some(bullet) =>
          enemies = enemy.health match {
            case rm if rm <= 1 => {enemyHitSound.trigger(); enemies.filter(_ != enemy)}
            case _ => {enemyHitSound.trigger(); enemies.updated(enemies.indexOf(enemy), enemy.changeHealthCol)}
          }
          remainingBullets.filter(_ != bullet)
        case None => remainingBullets
      }
    }
  }

  private def isGameOver : Boolean = {
    enemies.exists(en => en.x + 20 >= WIDTH)
  }

  private def displayGameOver() : Unit = {
    fill(255)
    textFont(f, 20)
    background(0)
    text("Game Over",WIDTH/2-50, HEIGHT/2)
  }

  private def drawRegEnemy(e: RegularEnemy): Unit = {
    val col = e.color
    fill(col.red, col.green, col.blue)
    rect(e.x, e.y, 20, 20)
    circle(e.x, e.y, 15)
    circle(e.x + 20, e.y, 15)
    circle(e.x, e.y + 20, 15)
    circle(e.x + 20, e.y + 20, 15)
  }

  private def drawHeavyEnemy(e: HeavyEnemy): Unit = {
    val col = e.color
    fill(col.red, col.green, col.blue)
    rect(e.x, e.y, 25, 25)
    fill(100, 100, 100)
    rect(e.x + 5, e.y + 5, 15, 15)
  }

  private def drawBossEnemy(boss: Boss): Unit = {
    val col = boss.color
    fill(col.red, col.green, col.blue)
    rect(boss.x, boss.y, 80, 80)

    fill(255, 0, 0)

    // Weak Spot 1: Left eye
    circle(boss.x + 20, boss.y + 35, 15)

    // Weak Spot 2: Right eye
    circle(boss.x + 60, boss.y + 35, 15)

    // Weak Spot 3: Chest
    circle(boss.x + 40, boss.y + 55, 25)

    // Weak Spot 4: Back
    circle(boss.x + 40, boss.y + 15, 25)

    //Horns
    fill(100, 100, 100)
    triangle(boss.x + 15, boss.y, boss.x + 25, boss.y - 20, boss.x + 35, boss.y)
    triangle(boss.x + 45, boss.y, boss.x + 55, boss.y - 20, boss.x + 65, boss.y)

    //Wings
    fill(255, 140, 0)
    triangle(boss.x - 20, boss.y + 40, boss.x - 10, boss.y + 20, boss.x, boss.y + 40)
    triangle(boss.x + 80, boss.y + 40, boss.x + 90, boss.y + 20, boss.x + 100, boss.y + 40)
  }

  private def drawEnemy(e : Enemy) : Unit = {
    e match {
      case re : RegularEnemy => drawRegEnemy(re)
      case he : HeavyEnemy => drawHeavyEnemy(he)
      case b : Boss => {
        drawBossEnemy(b)
        drawBossHealth(b)
      }
    }
  }

  private def getNewWave(numWave : Int) : Unit = {
      currWave = wavesList(numWave)
      totalBosses = currWave.numBosses
      totalRegulars = currWave.numRegs
      totalHeavies = currWave.numHeavies
      waveNum = currWave.numWave.toString
      enemiesLeftToSpawn = totalHeavies + totalRegulars
  }

  private def getWavesByDiff(diff : Difficulty) : List[Wave] = {
    val listOfWaves = diff match{
      case e : easy => waves.easyWaves
      case m : medium => waves.mediumWaves
      case h : hard => waves.hardWaves
      case o : OOFPExam => waves.oofpWaves
      case _ => List.empty[Wave]
    }
    listOfWaves
  }
}
