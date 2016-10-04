//
//  SpawnElements.swift
//  Maze Run
//
//  Created by Vincent Nafrada on 2016-07-25.
//  Copyright © 2016 NFRD. All rights reserved.
//

import SpriteKit
var arrowNode = SKSpriteNode()

class SpawnElements: SKSpriteNode{
    
    var imageName:String = ""
    internal var maze = SKSpriteNode()
    internal var ball = SKSpriteNode()
    internal var nextLvl = SKSpriteNode()
    internal var spike1 = SKSpriteNode()
    internal var splash = SKSpriteNode()
    internal var playButton = SKLabelNode()
    var levelTimerLabel = SKLabelNode(fontNamed: "ArialMT")
    
    init() {
        let spawntexture = SKTexture(imageNamed: (imageName))
        super.init(texture: spawntexture, color: UIColor.white, size: spawntexture.size())
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    
    func getArrow(_ xPos: Int, yPos: Int, frame: CGRect, imageName: String) -> SKSpriteNode{
        
        let arrow = SKTexture(imageNamed:  (imageName))
        
            arrowNode = SKSpriteNode(texture: arrow)
            arrowNode.position = CGPoint(x: frame.midX + CGFloat(xPos), y: frame.midY + CGFloat(yPos))

        
        return(arrowNode)
    }
    
    func getLevel(_ xPos: Int, yPos: Int, frame: CGRect, lvl: Int) -> SKSpriteNode{

        let mazes = ["lvl1.png", "lvl2.png", "lvl3.png", "lvl4.png", "lvl5.png", "lvl6.png", "lvl7.png", "endgame.png"]
        let mazeTxt = SKTexture(imageNamed:  (mazes[lvl]))
        maze = SKSpriteNode(texture: mazeTxt)
        maze.position = CGPoint(x: frame.midX + CGFloat(xPos), y: frame.midY + CGFloat(yPos))

        maze.physicsBody = SKPhysicsBody(texture: maze.texture!, alphaThreshold: 0.99, size: size)
        maze.physicsBody!.isDynamic = false
        maze.physicsBody!.usesPreciseCollisionDetection = true

        return maze

    }
    
    func getNextLevel(_ frame: CGRect, lvl: Int) -> SKSpriteNode{
        let nextLvlSpot = [(-50, 120), (-80, 264), (-50, 2), (-70, -80),(-890, 220),(-800, -250) , (-40, -160),
            (-890, 0), (-400, -30)]
        let nextTexture = SKTexture(imageNamed: "nextLevel.png")
        nextLvl = SKSpriteNode(texture: nextTexture)
        nextLvl.position = CGPoint(x: frame.maxX + CGFloat(nextLvlSpot[lvl].0), y: frame.midY + CGFloat(nextLvlSpot[lvl].1))
        return nextLvl
    }
    
    func getBall(_ frame: CGRect, lvl: Int) -> SKSpriteNode{
        let ballSpot = [(40, 120),(40, 152), (40 , 6), (40, 200), (40, -250), (500, 262), (40, 170), (40, 0), (40, 220), (40, 220)]
        let ballTexture = SKTexture(imageNamed: "ball.png")
        ball = SKSpriteNode(texture: ballTexture)
        ball.position = CGPoint(x: frame.minX + CGFloat(ballSpot[lvl].0), y: frame.midY + CGFloat(ballSpot[lvl].1))
        return ball
    }
    
    func getSpikes(_ xPos: Int, yPos: Int, frame: CGRect, lvl: Int) -> SKSpriteNode{

        let spikeText = SKTexture(imageNamed: "spike.png")
        spike1 = SKSpriteNode(texture: spikeText)
        spike1.position = CGPoint(x: frame.midX + CGFloat(xPos), y: frame.midY + CGFloat(yPos))
        
        spike1.physicsBody = SKPhysicsBody(circleOfRadius: spike1.size.width/2)
        spike1.physicsBody!.isDynamic = false
        spike1.physicsBody!.affectedByGravity = false
        spike1.physicsBody!.usesPreciseCollisionDetection = true
        
        return spike1
    }
    
    func getTimer(_ rect: CGRect, lvl: Int) -> SKLabelNode{
        var levelTimerValue = 0{
            didSet {
                levelTimerLabel.text = "Level \(lvl)"
            }
        }
        levelTimerLabel.fontColor = SKColor.black
        levelTimerLabel.fontSize = 40
        levelTimerLabel.position = CGPoint(x: rect.midX - 470, y: rect.midY + 240)
        
        return levelTimerLabel
        
    }
    
    func getSplash(_ rect:CGRect) -> SKSpriteNode{
        let splashTxt = SKTexture(imageNamed: "splash.png")
        splash = SKSpriteNode(texture: splashTxt)
        splash.position = CGPoint(x: rect.midX, y: rect.midY)
        return splash
    }
    func  getPlayButton(_ rect: CGRect) -> SKLabelNode {
        playButton.text = "PLAY"
        playButton.position = CGPoint(x: rect.midX, y: rect.midY - 156)
        playButton.fontColor = SKColor.orange
        playButton.fontSize = 72
        playButton.fontName = "Arial"
        return playButton
    }
}
