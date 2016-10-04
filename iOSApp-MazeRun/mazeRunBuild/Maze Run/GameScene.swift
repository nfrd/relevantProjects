//
//  GameScene.swift
//  Maze Run
//
//  Created by Vincent Nafrada on 2016-07-12.
//  Copyright (c) 2016 NFRD. All rights reserved.
//


import SpriteKit

class GameScene: SKScene, SKPhysicsContactDelegate {
    var levelTimerLabel = SKLabelNode()
    var playButton = SKLabelNode(fontNamed: "Arial")

    var taps = 0
    var lvl = 0
    
    var splash = SKSpriteNode()
    
    var nextLevel = SKSpriteNode()
    var level = SKSpriteNode()
    
    let moveSpike = MoveObject()
    
    var rightArr = SKSpriteNode()
    var leftArr = SKSpriteNode()
    var downArr = SKSpriteNode()
    var upArr = SKSpriteNode()
    var spike1 = SKSpriteNode()
    
    let backgroundMusic = SKAudioNode(fileNamed: "mazeRunBG.mp3")

    var ball = SKSpriteNode()
    
    enum BodyType:UInt32 {
        case ball = 1
        case walls = 2
        case nextLvl = 4
        case spike = 8
        case anotherBody3 = 16
        
    }
    
    override func didMove(to view: SKView) {
        
        self.physicsWorld.contactDelegate = self
        self.backgroundColor = SKColor(red: 0.37, green: 0.49, blue: 0.60, alpha: 0.8 )

        splash = SpawnElements().getSplash(self.frame)
        self.addChild(splash)
        playButton = SpawnElements().getPlayButton(self.frame)
        self.addChild(playButton)
        
        
    }
    func timer(){
        var levelTimerLabel = SpawnElements().getTimer(self.frame, lvl: lvl)
        var levelTimerValue = [11,11,16,11,26,31,11,21, 10, 10, 10]{
            didSet{
                levelTimerLabel.text = "\(levelTimerValue[lvl])"
            }
        }
        self.addChild(levelTimerLabel)
        
        
        let wait = SKAction.wait(forDuration: 1) //change countdown speed here
        let block = SKAction.run({
            [unowned self] in
            
            if levelTimerValue[self.lvl] > 0{
                levelTimerValue[self.lvl]-=1
            }else{
                self.speed = 0
                let reveal = SKTransition.flipHorizontal(withDuration: 0.5)
                let gameOverScene = GameOverScene(size: self.size, hit: true)
                self.view!.presentScene(gameOverScene, transition: reveal)

                self.removeAction(forKey: "countdown")
            }
            })
        let sequence = SKAction.sequence([wait,block])
        run(SKAction.repeatForever(sequence), withKey: "countdown")
    }
    
    func spawnNextLevelSpot(){
        nextLevel = SpawnElements().getNextLevel(self.frame, lvl: lvl)
        nextLevel.physicsBody = SKPhysicsBody(circleOfRadius: nextLevel.size.width/3.5)
        nextLevel.physicsBody!.isDynamic = false
        nextLevel.physicsBody!.usesPreciseCollisionDetection = true
        nextLevel.physicsBody!.categoryBitMask = BodyType.nextLvl.rawValue
        nextLevel.physicsBody!.contactTestBitMask = BodyType.ball.rawValue
        self.addChild(nextLevel)
        
    }
    
    func spawnArrow(){
        rightArr = SpawnElements().getArrow(453 , yPos: -160, frame: self.frame, imageName: "right.png")
        rightArr.size.height = self.frame.maxY - 650
        rightArr.size.width = self.frame.maxX - 910
        rightArr.alpha = 0.03
        self.addChild(rightArr)
        
        
        leftArr = SpawnElements().getArrow(255, yPos: -160, frame: self.frame, imageName: "left.png")
        leftArr.size.height = self.frame.maxY - 650
        leftArr.size.width = self.frame.maxX - 910
        leftArr.alpha = 0.03
        self.addChild(leftArr)
        

        downArr = SpawnElements().getArrow(354, yPos: -240, frame: self.frame, imageName: "down.png")
        downArr.size.height = self.frame.maxY - 670
        downArr.size.width = self.frame.maxX - 885
        downArr.alpha = 0.03
        self.addChild(downArr)
        
        upArr = SpawnElements().getArrow(354, yPos: -80, frame: self.frame, imageName: "up.png")
        upArr.size.height = self.frame.maxY - 670
        upArr.size.width = self.frame.maxX - 885
        upArr.alpha = 0.03
        self.addChild(upArr)
        
    }
    
    func spawnLevel(){
        level = SpawnElements().getLevel(0, yPos: 0, frame: self.frame, lvl: lvl)
        level.physicsBody = SKPhysicsBody(texture: level.texture!, size: level.size)
        level.physicsBody!.isDynamic = false
        
        level.physicsBody!.categoryBitMask = BodyType.walls.rawValue
        level.physicsBody!.contactTestBitMask = BodyType.walls.rawValue
        self.addChild(level)
        
    }

    let spikeCount: [Int] = [3,3,5,3,5,8, 6]
    let spike1Spot = [[0,0],[222,0],[-222,0],
                      [37,-180],[-50,250], [260,250],
                      [-232,-5], [-110,-90],[240, 75], [80, 115],[115,63],
                      [-70,205], [-70,-85], [-70,60],
                      [-15,108], [-120,108], [123, 108], [-110,-100],[-240,-72],
                      [0,0],[20,0],[40,0],[50,0],[60,0],[70,0],[80,0],[100,0],
                      [0,0],[20,0],[40,0],[50,0],[60,0],[70,0]]
    var direction = 0
    var num = 0
    
    func spawnSpike(){
        
        let spikeDirections = [["vert fast short", "vert norm", "vert norm"],
                    ["vert fast long", "horiz fast short", "horiz norm"],
                    ["horiz norm", "horiz norm", "horiz fast long", "horiz fast long","vert slow short"],
                    ["horiz fast short", "horiz fast short", "horiz fast short"],
                    ["vert fast short", "vert fast short","vert fast short","vert fast short","horiz norm"],
                    ["vert fast short", "vert fast short","vert fast short","vert fast short","horiz norm", "vert fast short", "vert fast short","vert fast short"]]
        
        while direction < spikeCount[lvl-2]{
            spike1 = SpawnElements().getSpikes(spike1Spot[num][0], yPos: spike1Spot[num][1],frame: self.frame, lvl: lvl-2)
            spike1.physicsBody!.categoryBitMask = BodyType.spike.rawValue
            spike1.physicsBody!.contactTestBitMask = BodyType.ball.rawValue
            
            if spikeDirections[lvl-2][direction] ==  "vert norm"{
                spike1.run(moveSpike.moveVertical(delta: 150, duration: 2.0))
            }
            else if spikeDirections[lvl-2][direction] ==  "vert slow short"{
                spike1.run(moveSpike.moveVertical(delta: 90, duration: 2.5))
            }
            else if spikeDirections[lvl-2][direction] ==  "vert fast short"{
                spike1.run(moveSpike.moveVertical(delta: 140, duration: (0.5)))
            }
            else if spikeDirections[lvl-2][direction] ==  "vert fast long"{
                spike1.run(moveSpike.moveVertical(delta: 250, duration: (0.8)))
            }
                
            else if spikeDirections[lvl-2][direction] ==  "horiz fast short" {
                spike1.run(moveSpike.moveHorizontal(delta: 140, duration: (0.5)))
            }
            else if spikeDirections[lvl-2][direction] ==  "horiz slow short" {
                spike1.run(moveSpike.moveHorizontal(delta: 90, duration: (2.5)))
            }
            else if spikeDirections[lvl-2][direction] ==  "horiz fast long"{
                spike1.run(moveSpike.moveHorizontal(delta: 250, duration: (0.8)))
            }
            else if spikeDirections[lvl-2][direction] ==  "horiz norm"{
                spike1.run(moveSpike.moveHorizontal(delta: 150, duration: 2.0))
            }
            direction += 1
            self.addChild(spike1)
            print("spike" + String(num))

            num += 1
            
        }
        direction = 0
        
    }
    
    func spawnBall(){
        
        ball = SpawnElements().getBall(self.frame, lvl: lvl)
        
        ball.physicsBody = SKPhysicsBody(circleOfRadius: ball.size.width/2)
        ball.physicsBody!.isDynamic = true
        ball.physicsBody!.affectedByGravity = false
        ball.physicsBody!.usesPreciseCollisionDetection = true
        
        ball.physicsBody!.categoryBitMask = BodyType.ball.rawValue
        ball.physicsBody!.contactTestBitMask = BodyType.walls.rawValue | BodyType.nextLvl.rawValue

        self.addChild(ball)
    }
    
    func gameOver(){
        self.speed = 0
    }
    
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {

        var point: CGPoint = CGPoint()
        let move = MoveObject()
        
        
        for touch in touches{
            
            point = touch.location(in: self)
            
            switch (true) {
            case playButton.contains(point):
                if lvl == 0{
                    backgroundMusic.autoplayLooped = true
                    addChild(backgroundMusic)
                    splash.removeFromParent()
                    playButton.removeFromParent()
                    
                    timer()
                    spawnArrow()
                    spawnLevel()
                    spawnBall()
                    spawnNextLevelSpot()
                    
                    lvl+=1
                }
                
            case rightArr.contains(point):
                ball.run(move.moveBall(deltaX: 60, deltaY: 0))
            case leftArr.contains(point):
                ball.run(move.moveBall(deltaX: -60, deltaY: 0))
            case downArr.contains(point):
                ball.run(move.moveBall(deltaX: 0, deltaY: -60))
            case upArr.contains(point):
                ball.run(move.moveBall(deltaX: 0, deltaY: 60))
            
            default: break
                
            }
        }
    }
    func endGame(){
        // restart the game
        let gameScene = GameScene(size: size)
        self.view!.presentScene(gameScene)
    }
    
    func didBegin(_ contact: SKPhysicsContact) {
        let contactMask = contact.bodyA.categoryBitMask | contact.bodyB.categoryBitMask
        switch(contactMask) {
        case BodyType.spike.rawValue | BodyType.ball.rawValue:
            backgroundMusic.removeFromParent()

            let reveal = SKTransition.flipHorizontal(withDuration: 0.5)
            let gameOverScene = GameOverScene(size: self.size, hit: true)
            self.view!.presentScene(gameOverScene, transition: reveal)
            
        case BodyType.ball.rawValue | BodyType.nextLvl.rawValue:
            if action(forKey: "countdown") != nil {removeAction(forKey: "countdown")}
            
            self.removeAllChildren()
            if (lvl < 7){

            addChild(backgroundMusic)
            spawnArrow()
            spawnLevel()
            spawnBall()
            spawnNextLevelSpot()
            timer()
            
                if (lvl >= 2){
                spawnSpike()
                }
            }
            else{
                let reveal = SKTransition.flipHorizontal(withDuration: 0.5)
                let gameOverScene = GameOverScene(size: self.size, hit: false)
                self.view!.presentScene(gameOverScene, transition: reveal)
            }
            
            lvl+=1
            
        default:
            return
            
        }
    }
    
    override func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent?) {
        ball.removeAllActions()
    }
    
    override func update(_ currentTime: TimeInterval) {
        /* Called before each frame is rendered */
    }
}
