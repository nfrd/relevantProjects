//
//  GameOver.swift
//  Maze Run
//
//  Created by Vincent Nafrada on 2016-08-18.
//  Copyright © 2016 NFRD. All rights reserved.
//
import SpriteKit

class GameOverScene: SKScene {
    
    init(size: CGSize, hit:Bool) {
        
        super.init(size: size)
        
        backgroundColor = SKColor.orange
        
        let msg = hit ? "Try again!" : "Congratulations!"
        
        let label = SKLabelNode(fontNamed: "Arial")
        label.text = msg
        label.fontSize = 70
        label.fontColor = SKColor.black
        label.position = CGPoint(x: size.width/2, y: size.height/2)
        addChild(label)
        
        run(SKAction.sequence([
            SKAction.wait(forDuration: 4.0),
            SKAction.run() {

                let reveal = SKTransition.fade(withDuration: 0.5)
                let scene = GameScene(size: size)
                scene.scaleMode = .aspectFill

                self.view!.presentScene(scene, transition:reveal)
            }
            ]))
        
    }
    
    required init(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}
