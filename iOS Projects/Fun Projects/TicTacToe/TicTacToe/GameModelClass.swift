//
//  GameModelClass.swift
//  TicTacToe
//
//  Created by Govindaraju, Saranath on 6/8/15.
//  Copyright (c) 2015 Saranath Govindaraju. All rights reserved.
//

import Foundation


class GameModelClass{
    
    struct Matrix {
        let rows: Int, columns: Int
        var grid: [String]
        
        init(rows: Int, columns: Int) {
            self.rows = rows
            self.columns = columns
            grid = Array(count: rows * columns, repeatedValue: "-")
        }
        
        func indexIsValidForRow(row: Int, column: Int) -> Bool {
            return row >= 0 && row < rows && column >= 0 && column < columns
        }
        subscript(row: Int, column: Int) -> String {
            get {
                assert(indexIsValidForRow(row, column: column), "Index out of range")
                return grid[(row * columns) + column]
            }
            set {
                assert(indexIsValidForRow(row, column: column), "Index out of range")
                grid[(row * columns) + column] = newValue
            }
        }
    }
    
    private var M = Matrix(rows: 3, columns: 3)
    
    
    private func checkWin(values: String...) -> Bool{
        return ((values[0] != "-" ) && (values[0] == values[1]) && (values[1] == values[2]))
    }
    
    private func checkRow() -> Bool{
        
        for var i = 0; i < 3; i++ {
            if checkWin(  M[i, 0], M[i, 1], M[i, 2] ){
                return true
            }
        }
        
        return false
    }
    
    private func checkColumn() -> Bool{
        
        for var i = 0; i < 3; i++ {
            if checkWin(  M[0, i], M[1, i], M[2, i] ){
                return true
            }
        }
        
        return false
    }
    
    private func checkDiagonal() -> Bool{
        return ( checkWin( M[0, 0], M[1, 1], M[2, 2]) || checkWin(M[0, 2], M[1, 1], M[2,0]) )
    }
    
    func findWinner(index: Int, turn: String) -> Bool{
        M[index / 10, index % 10] = turn
        return (checkRow() || checkColumn() || checkDiagonal())
    }
    
    
    func resetScore(){
        for var i = 0; i < M.rows; i++ {
            for var j = 0; j < M.columns; j++ {
                M[i,j] = "-"
                }
            }
    }
    
}