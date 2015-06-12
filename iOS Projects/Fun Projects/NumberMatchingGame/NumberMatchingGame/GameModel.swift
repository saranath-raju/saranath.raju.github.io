//
//  GameModel.swift
//  NumberMatchingGame
//
//  Created by Govindaraju, Saranath on 6/10/15.
//  Copyright (c) 2015 Saranath Govindaraju. All rights reserved.
//

import Foundation

class GameModel{
    
    private var gameDictionary = [Int : Int]()
    let N: Int = 16
    private var valueAtPosition: [Int] = Array(count: 16, repeatedValue: -1)
    init(){
        setUp()
    }
    
    private func setUp(){
        var index: Int
        var randomNumbers: (key: Int, value: Int)? = nil
        for index in 0..<N/2 {
            randomNumbers = getRandomKeyValue(N)
            while randomNumbers == nil{
            randomNumbers = getRandomKeyValue(N)
            }
            gameDictionary[randomNumbers!.key] = randomNumbers!.value
        }
        index = getRandomInteger(UInt32(100))
        for (key, value) in gameDictionary{
            valueAtPosition[key] = index
            valueAtPosition[value] = index
            index++
        }
        println("\(valueAtPosition)")
        
    }
    
    func getTitle(index: Int) -> Int{
        return valueAtPosition[index]
    }
    
    func getRandomInteger(n: UInt32) ->Int{
        return Int(arc4random_uniform(n))
    }
    
    private func getRandomKeyValue(n: Int) -> (Int,Int)?{
        let key = getRandomInteger(UInt32(n))
        let value = getRandomInteger(UInt32(n))
        if (key != value) && !contains(gameDictionary.keys, key) && !contains(gameDictionary.keys, value) && !contains(gameDictionary.values, key) && !contains(gameDictionary.values, value)
        { return (key, value) }
        return nil
    }
    
    func reset(){
        gameDictionary.removeAll(keepCapacity: false)
        setUp()
    }
    
    
}