//
//  CalculatorBrain.swift
//  Calculator
//
//  Created by Govindaraju, Saranath on 5/12/15.
//  Copyright (c) 2015 Govindaraju, Saranath. All rights reserved.
//

import Foundation

class CalculatorBrain{
    
    private enum Op: Printable {
        case Operand( Double )
        case UnaryOperation( String, Double -> Double)
        case BinaryOperation( String, (Double,Double) ->Double)
        
        
        var description: String{
            get{
                switch self{
                case .Operand(let value):
                        return "\(value)"
                case .UnaryOperation(let symbol, _):
                        return symbol
                case .BinaryOperation(let symbol, _):
                        return symbol
                }
            }
        }
    }
    
    private var knownOps = [String:Op]()
    
    private var opStack = [Op]()
    
    init()
    {
        knownOps["*"] = Op.BinaryOperation("*" , *)
        knownOps["/"] = Op.BinaryOperation("/") { $1 / $0 }
        knownOps["-"] = Op.BinaryOperation("-") { $1 - $0 }
        knownOps["+"] = Op.BinaryOperation("+", +)
        knownOps["√"] = Op.UnaryOperation("√", sqrt)
    }
    
    private func evaluate(ops: [Op]) -> (result: Double?, remainingStack: [Op])
    {
        if !ops.isEmpty
        {
            var remainingOps = ops
            let op = remainingOps.removeLast()
            switch op {
            case .Operand(let operand):
                return (operand,remainingOps)
            case .UnaryOperation(_, let operation):
                let operationEvaluation = evaluate(remainingOps)
               if let operand = operationEvaluation.result
               {
                    return (operation(operand),operationEvaluation.remainingStack)
                }
            case .BinaryOperation(_, let operation):
                let op1Evaluation = evaluate(remainingOps)
                if let operand1 = op1Evaluation.result
                {
                    let op2Evaluation = evaluate(op1Evaluation.remainingStack)
                    if let operand2 = op2Evaluation.result
                    {
                        return (operation(operand1,operand2),op2Evaluation.remainingStack)
                    }
                }
            }
            
        }
        return (nil,ops)
    }
    
    func evaluate() -> Double?
    {
        let (result, remainder) = evaluate(opStack)
        println("\(opStack) = \(result) with \(remainder) left over")
        return result
    }
    
    func pushOperand(operand: Double) -> Double?
    {
        opStack.append(Op.Operand(operand))
        return evaluate()
    }
    
    func performOperation(symbol: String) -> Double?
    {
        if let operation = knownOps[symbol]
        {
            opStack.append(operation)
        }
        return evaluate()
    }
}