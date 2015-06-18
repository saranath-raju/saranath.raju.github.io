//
//  HangmanView.swift
//  Hangman
//
//  Created by Govindaraju, Saranath on 6/14/15.
//  Copyright (c) 2015 Saranath Govindaraju. All rights reserved.
//

import UIKit

@IBDesignable
class HangmanView: UIView {
    
    var count: Int = 6 {
        didSet {
            count = min(max(count, 0), 100)
            setNeedsDisplay()
        }
    }
    
    var lineWidth: CGFloat = 3.0
    var color = UIColor.blueColor()
    
    private struct constants {
        static  var meetingPoint = CGPoint()
        static  var faceRadius: CGFloat = 0
        static  var faceCenterPoint = CGPoint()
        static  var bodyStartPoint = CGPoint()
        static  var bodyEndPoint = CGPoint()

    }
    
    
    private enum Hand {
        case Left, Right
    }
    
    private enum Leg {
        case Left, Right
    }
    
    private func draw(path: UIBezierPath){
        path.lineWidth = lineWidth
        color.set()
        path.stroke()
    }
    
    private func drawFace() -> UIBezierPath {
        
        let faceRadius: CGFloat = min(bounds.size.height, bounds.size.width) / 2 * 0.12
        let faceCenter = CGPoint(x: bounds.maxX/2, y: bounds.maxY/6 + faceRadius + 2.0)
        
        constants.faceCenterPoint = faceCenter
        constants.faceRadius = faceRadius
        
        let path = UIBezierPath(arcCenter: faceCenter, radius: faceRadius, startAngle: 0, endAngle: CGFloat(2*M_PI), clockwise: true)
        
        return path
    }
    
    private func drawHand(whichHand: Hand) -> UIBezierPath {
        
        let path = UIBezierPath()
        
        let factor = min(bounds.size.height, bounds.size.width) / 2 * 0.15
        let bodyStart = constants.bodyStartPoint
        var handEnd = CGPoint(x: bodyStart.x , y: bodyStart.y + factor)
        
        switch whichHand {
        case .Left:
            handEnd.x -= factor
        case .Right:
            handEnd.x += factor
        }
       
        path.moveToPoint(bodyStart)
        path.addLineToPoint(handEnd)
        
        return path
    }
    
    private func drawBody() -> UIBezierPath {
        
        let path = UIBezierPath()
        let factor = min(bounds.size.height, bounds.size.width) / 2 * 0.35
        
        let bodyStart = CGPoint(x: constants.faceCenterPoint.x, y: constants.faceCenterPoint.y + constants.faceRadius)
        let bodyEnd = CGPoint(x: bodyStart.x, y: bodyStart.y + factor)
        
        constants.bodyStartPoint = bodyStart
        constants.bodyEndPoint = bodyEnd
        
        path.moveToPoint(bodyStart)
        path.addLineToPoint(bodyEnd)
        
        return path
    }
    
    private func drawLeg(whichLeg: Leg) -> UIBezierPath {
        let path = UIBezierPath()
        
        let factor = min(bounds.size.height, bounds.size.width) / 2 * 0.15
        let bodyEnd = constants.bodyEndPoint
        var legEnd = CGPoint(x: bodyEnd.x , y: bodyEnd.y + factor)
        
        switch whichLeg {
        case .Left:
            legEnd.x -= factor
        case .Right:
            legEnd.x += factor
        }
        
        path.moveToPoint(bodyEnd)
        path.addLineToPoint(legEnd)
        
        return path

    }
    
    
    private func drawStand() -> UIBezierPath {
        
        
        let path = UIBezierPath()
    
        path.moveToPoint(CGPoint(x: bounds.maxX/1.5, y: bounds.maxY/8))
        path.addLineToPoint(CGPoint(x: bounds.maxX/2, y: bounds.maxY/8))
        
        path.moveToPoint(CGPoint(x: bounds.maxX/2, y: bounds.maxY/8))
        path.addLineToPoint(CGPoint(x: bounds.maxX/2, y: bounds.maxY/6))
        
        path.moveToPoint(CGPoint(x: bounds.maxX/1.5, y: bounds.maxY/8))
        path.addLineToPoint(CGPoint(x: bounds.maxX/1.5, y: bounds.maxY/1.2))
        
        constants.meetingPoint = CGPoint(x: bounds.maxX/2, y: bounds.maxY/6)
    
        return path
    }
    
    
    override func drawRect(rect: CGRect) {
        switch count {
        case 0:
            //draw another leg
            draw(drawLeg(.Right))
            fallthrough
        case 1:
            //draw one leg
            draw(drawLeg(.Left))
            fallthrough
        case 2:
            //draw another hand
            draw(drawHand(.Right))
            fallthrough
        case 3:
            //draw one hand
            draw(drawHand(.Left))
            fallthrough
        case 4:
            //draw st line - body
            draw(drawBody())
            fallthrough
        case 5:
            //draw face
            draw(drawFace())
            fallthrough
        case 6:
            //draw stand
            draw(drawStand())
        default: break;
        }
        
    }


}
