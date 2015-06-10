//
//  ViewController.swift
//  TicTacToe
//
//  Created by Govindaraju, Saranath on 6/8/15.
//  Copyright (c) 2015 Saranath Govindaraju. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    
    @IBOutlet weak var winnerDisplayLabel: UILabel!
    
    var usedButtons = [UIButton]()
    var xTurn = true
    var model = GameModelClass()
    var winnerFound = false
    
    @IBAction func resetAll(sender: UIButton) {
        for usedbutton in usedButtons{
            usedbutton.setTitle(" ", forState: UIControlState())
        }
        usedButtons.removeAll(keepCapacity: false)
        winnerDisplayLabel.text = "Welcome to Game"
        model.resetScore()
    }
    
    
    
    @IBAction func gameButtonClicked(sender: UIButton) {
        
        if !winnerFound{
            if xTurn{
                modelCommunicator(sender, title: "X", titleColor: UIColor.whiteColor())
                xTurn = false
            }
            else{
                modelCommunicator(sender, title: "O", titleColor: UIColor.whiteColor())
                xTurn = true
            }
        }
    }
    
    func modelCommunicator(button: UIButton, title: String, titleColor: UIColor){
        if !contains(usedButtons, button){
            button.setTitleColor(titleColor, forState: UIControlState())
            button.setTitle(title, forState: UIControlState())
            usedButtons.append(button)
            winnerFound = model.findWinner(button.tag, turn: title)
            if usedButtons.count == 9 {
                winnerDisplayLabel.text = "Match Drawn"
            }
            if winnerFound {
                winnerDisplayLabel.text = title + " - Wins"
            }
        }
    }
    
}

