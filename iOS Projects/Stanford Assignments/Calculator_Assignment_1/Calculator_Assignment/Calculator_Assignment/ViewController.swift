//
//  ViewController.swift
//  Calculator_Assignment
//
//  Created by Govindaraju, Saranath on 4/27/15.
//  Copyright (c) 2015 Govindaraju, Saranath. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

 
    @IBOutlet weak var displayLabel: UILabel!

    @IBOutlet weak var historyLabel: UILabel!
    
    var usertypinginthemiddle = false
    var decimalPresent = false
    
    var operandStack: Array<Double> = Array<Double>()
    var historyStack: Array<String> = Array<String>()
    
    var displayValue: Double
    {
        get{
            return NSNumberFormatter().numberFromString(displayLabel.text!)!.doubleValue
        }
        set{
            displayLabel.text = "\(newValue)"
            usertypinginthemiddle = false
        }
    }
    
    
    
    @IBAction func appendDigits(sender: UIButton) {
        
        let digit = sender.currentTitle!
        
        if (decimalPresent && checkPresence(digit, checkString: ".")){
            return
        }
        
        if usertypinginthemiddle
        {
            displayLabel.text = displayLabel.text! + digit
        }
        else
        {
            displayLabel.text = digit
            usertypinginthemiddle = true
        }
        
        if checkPresence(displayLabel.text!, checkString: ".")
        {
            decimalPresent = true
        }
        
        
    }
    
    func checkPresence(givenString: String, checkString: String) -> Bool
    {
        return givenString.rangeOfString(checkString) != nil
    }
    
    
    @IBAction func enter() {
        
        usertypinginthemiddle = false;
        decimalPresent = false
        operandStack.append(displayValue)
        println("\(operandStack)")
        
    }
    
    @IBAction func clearAll(sender: UIButton) {
        let symbol = sender.currentTitle!
        switch symbol{
            case "AC":
                operandStack.removeAll(keepCapacity: false)
                historyStack.removeAll(keepCapacity: false)
                displayLabel.text = "0"
                historyLabel.text = "History"
            case "⬅︎":
                if countElements(displayLabel.text!) > 1
                {
                    displayLabel.text = dropLast(displayLabel.text!)
                }
                else
                {
                    displayLabel.text = "0"
                    usertypinginthemiddle = false
                }
        default: break
        }
        
    }
    
    
    @IBAction func positiveAndNegative(sender: UIButton) {
        if sender.currentTitle! == "+/-"{
        displayValue = displayValue * -1
        }
    }
   
   
    
    @IBAction func addConstants(sender: UIButton) {
        let symbol = sender.currentTitle!
        if !usertypinginthemiddle {
            switch symbol{
             case "∏":
                operandStack.append(3.14)
                displayLabel.text = sender.currentTitle!
            default: break
            }
        }
    }
    
    @IBAction func operation(sender: UIButton) {
        
        let symbol = sender.currentTitle!
        historyStack.append(symbol)
        if usertypinginthemiddle {
            enter()
        }
        switch symbol{
        case "÷": performOperation {$1 / $0}
        case "×": performOperation {$0 * $1}
        case "-": performOperation {$1 - $0}
        case "+": performOperation {$0 + $1}
        case "√": performOperation {sqrt($0)}
        case "Sin": performOperation {sin($0)}
        case "Cos": performOperation {cos($0)}
        default : break
        }
        historyStack.removeAll(keepCapacity: false)
    }
    
    func performOperation(operation: (Double,Double) ->Double){
        if(operandStack.count>1)
        {
            historyStack.append("\(operandStack[operandStack.count-1])")
            historyStack.append("\(operandStack[operandStack.count-2])")
            displayValue = operation(operandStack.removeLast(),operandStack.removeLast())
            historyLabel.text = "\(historyStack) = "
            enter()
        }
    }
    
    func performOperation(operation: (Double) ->Double){
        if(operandStack.count>0)
        {
            historyStack.append("\(operandStack[operandStack.count-1])")
            historyLabel.text = "\(historyStack) ="
            displayValue = operation(operandStack.removeLast())
            enter()
        }
    }

}

