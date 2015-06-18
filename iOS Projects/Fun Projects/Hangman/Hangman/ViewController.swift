//
//  ViewController.swift
//  Hangman
//
//  Created by Govindaraju, Saranath on 6/14/15.
//  Copyright (c) 2015 Saranath Govindaraju. All rights reserved.
//

import UIKit
import Foundation

class ViewController: UIViewController {

    var model = GameModel()
    
    var dataString: String = ""
    var dataLength: Int = 0
    
    var usedButtons = [String: UIButton]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        initialStateSetup()
    }
    
    func initialStateSetup(){
        resetButton.titleLabel?.adjustsFontSizeToFitWidth = true
        dataString = model.getWord().uppercaseString
        dataLength = (dataString as NSString).length
        var temp = String(count: dataLength, repeatedValue: Character("-"))
        displayLabel.text = temp
        println(dataString)
    }
    
    @IBOutlet weak var labelAndButtonsView: UIView!
    @IBOutlet weak var hangView: HangmanView!
    @IBOutlet weak var displayLabel: UILabel!
    
    @IBOutlet weak var resetButton: UIButton!
    
    @IBAction func resetButtonPressed() {
        for (alphabet, button) in usedButtons {
            button.setTitle(alphabet, forState: UIControlState())
            button.backgroundColor = self.view.backgroundColor ?? UIColor.blueColor()
            button.enabled = true
        }
        hangView.count = 6
        initialStateSetup()
    }
    
    
    @IBAction func alphabetPressed(sender: UIButton) {
        if displayLabel.text! == dataString {
            return
        }
        if hangView.count > 0 {
            if isAlphabetPresent(sender.currentTitle!) {
                changeDisplayLabelCharacterAtIndex(sender.currentTitle!)
            }
            else
            {
                hangView.count--
                if hangView.count == 0 { displayLabel.text = dataString   }
            }
            usedButtons[sender.currentTitle!] = sender
            sender.enabled = false
            sender.setTitle("", forState: UIControlState())
            sender.backgroundColor = self.view.backgroundColor ?? UIColor.blueColor()
        }
    }
    
    
    func isAlphabetPresent(buttonString: String) -> Bool {
        if dataString.rangeOfString(buttonString) != nil {
            return true
        }
        return false
    }
    
    func isAlphabetPresentAtIndex(alphabet: String, index: String.Index) -> Bool {
        if dataString[index] == Character(alphabet) {
            return true
        }
        
        return false
    }
    
    func replaceLabelCharacterAtIndex(index: String.Index, alphabet: String){
        
    }
    
    func changeDisplayLabelCharacterAtIndex(alphabet: String){
        var labelText = displayLabel.text!
        for var i = dataString.startIndex; i != dataString.endIndex; i = advance(i, 1) {
            if isAlphabetPresentAtIndex(alphabet, index: i) {
                labelText.replaceRange(i..<advance(i,1), with: alphabet)
                displayLabel.text = labelText
            }
        }
    }
    
    
    
}
