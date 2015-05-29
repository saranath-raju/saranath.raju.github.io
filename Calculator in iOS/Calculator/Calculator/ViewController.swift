//
//  ViewController.swift
//  Calculator
//
//  Created by Govindaraju, Saranath on 4/23/15.
//  Copyright (c) 2015 Govindaraju, Saranath. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

  
    
    @IBOutlet weak var displayText: UILabel!
    
    var usermiddle = false
    var brain = CalculatorBrain()
    

    @IBAction func enterKeyAction() {
        usermiddle = false
        if let result = brain.pushOperand(displayValue)
        {
            displayValue = result
        }
        else
        {
            displayValue = 0
        }
    }

    @IBAction func appendDigit(sender: UIButton){
        let digit = sender.currentTitle!
        //println("digit = \(digit)")
        if usermiddle{
            displayText.text = displayText.text! + digit
        }
        else{
            displayText.text = digit
            usermiddle = true
        }
    }
    
    @IBAction func operate(sender: UIButton) {
        if usermiddle{
            enterKeyAction()
        }
        
        if let operation = sender.currentTitle
        {
        if let result = brain.performOperation(operation)
        {
            displayValue = result
        }
        else
        {
            displayValue = 0
        }
            }
    }
    
    var displayValue: Double{
        get{
            return NSNumberFormatter().numberFromString(displayText.text!)!.doubleValue
        }
        set{
            displayText.text = "\(newValue)";
            usermiddle = false;
            
        }
    }
}

