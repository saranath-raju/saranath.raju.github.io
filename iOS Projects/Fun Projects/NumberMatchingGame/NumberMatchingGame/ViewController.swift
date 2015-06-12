//
//  ViewController.swift
//  NumberMatchingGame
//
//  Created by Govindaraju, Saranath on 6/10/15.
//  Copyright (c) 2015 Saranath Govindaraju. All rights reserved.
//

import UIKit
import Darwin

class ViewController: UIViewController {

   var usedButtons = [UIButton]()
   var secondTime = false
    var twoClicksNotOver = true
    var model = GameModel()
    var buttonBgColor = UIColor.whiteColor()
    var previousButton: UIButton? = nil
    
    @IBAction func resetGame(sender: AnyObject) {
        secondTime = false
        model.reset()
        for button in usedButtons{
            button.enabled = true
            button.backgroundColor = buttonBgColor
            button.setTitle("", forState: UIControlState())
        }
    }
    
    
    @IBAction func numberButtonClicked(sender: UIButton) {
        buttonBgColor = sender.backgroundColor!
        if twoClicksNotOver {
        if !secondTime{
            previousButton = sender
            var previousButtonNumber = model.getTitle(sender.tag)
            sender.setTitle("\(previousButtonNumber)", forState: UIControlState())
            secondTime = true
        }
        else{
            twoClicksNotOver = false
            var presentButtonNumber = model.getTitle(sender.tag)
            var previousButtonNumber = self.model.getTitle(self.previousButton!.tag)
            sender.setTitle("\(presentButtonNumber)", forState: UIControlState())
            secondTime = false
            
            self.usedButtons.append(self.previousButton!)
            self.usedButtons.append(sender)

            
            let delay = 2.0 * Double(NSEC_PER_SEC)
            let time = dispatch_time(DISPATCH_TIME_NOW, Int64(delay))
            dispatch_after(time, dispatch_get_main_queue()){
            
            if presentButtonNumber ==  previousButtonNumber{
                self.previousButton!.backgroundColor = UIColor.whiteColor()
                sender.backgroundColor = UIColor.whiteColor()
                self.previousButton!.enabled = false
                sender.enabled = false
            }
            else{
                self.previousButton!.setTitle("", forState: UIControlState())
                sender.setTitle("", forState: UIControlState())
            }
            self.previousButton = nil
            self.twoClicksNotOver = true
            }
            }
        }
    }
    

}

