//
//  MoveObject.swift
//  Maze Run
//
//  Created by Vincent Nafrada on 2016-07-26.
//  Copyright © 2016 NFRD. All rights reserved.
//

import SpriteKit

class MoveObject: SKAction{
    override init() {
        super.init()
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func moveHorizontal(delta: Int, duration: Double) -> SKAction {
        let move = SKAction.moveBy(x: (CGFloat(delta)), y: 0, duration: TimeInterval(duration))
        let moveBack = SKAction.moveBy(x: -(CGFloat)(delta), y: 0, duration: TimeInterval(duration))
        let moveForever = SKAction.repeatForever(SKAction.sequence([move, moveBack]))
            return(moveForever)
    }
    
    func moveVertical(delta: Int, duration: Double) -> SKAction {
        let move = SKAction.moveBy(x: 0, y: CGFloat(delta), duration: TimeInterval(duration))
        let moveBack = SKAction.moveBy(x: 0, y: -(CGFloat)(delta), duration: TimeInterval(duration))
        let moveForever = SKAction.repeatForever(SKAction.sequence([move, moveBack]))
        return(moveForever)
    }
    
    func moveBall(deltaX: Int, deltaY: Int) -> SKAction{
        let roll = SKAction.moveBy(x: CGFloat(deltaX), y: CGFloat(deltaY), duration: 0.2)
        let rollForever = SKAction.repeatForever(roll)
        
        return(rollForever)
    }
    
    
}

