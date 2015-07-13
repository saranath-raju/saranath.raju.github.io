//
//  BookModel.swift
//  BookWorld
//
//  Created by Govindaraju, Saranath on 7/4/15.
//  Copyright (c) 2015 Saranath Govindaraju. All rights reserved.
//

import Foundation
import UIKit

class BookModel {
    
    struct Book {
        var title: String
        var subTitle: String
        var publisher: String
        var publishedDate: String
        var description: String
        var authors: String
        var price: String
        
        var rating: Int
        var pageCount: Int
        
        var thumbnailURL: String?
        var pdfURL: String?
        
        var ratingImage: UIImage
        
        init(){
            title = ""
            subTitle = ""
            publisher = "Publisher - Unknown"
            publishedDate = "Published Date - Unknown"
            description = "Description - Unknown"
            authors = "Author - Unknown"
            price = "-- $"
            
            rating = 3
            pageCount = 100
            
            thumbnailURL = nil
            pdfURL = nil
            
            ratingImage =   UIImage()
            
        }
    }
    
    var bookArray = [Book]()
    var totalBooks = 0
    
    private func parseBookInfo(bookInfo: NSDictionary, book_received: Book, searchText: String) -> Book {
        var book = book_received
        
        if let title = bookInfo["title"] as? String {
            book.title = title
        }
        
        if let subtitle = bookInfo["subtitle"] as? String {
            book.subTitle = subtitle
        }
        else {
            book.subTitle = searchText
        }
        
        if let publisher = bookInfo["publisher"] as? String {
            book.publisher = publisher
        }
        if let publishedDate = bookInfo["publishedDate"] as? String {
            var dateFormatter = NSDateFormatter()
            dateFormatter.dateFormat = "yyyy-mm-dd"
            if let date = dateFormatter.dateFromString(publishedDate) {
                dateFormatter.dateFormat = "MMM dd, YYYY"
                let dateInWords = dateFormatter.stringFromDate(date)
                book.publishedDate = dateInWords
            }
        }
        if let description = bookInfo["description"] as? String {
            book.description = description
        }
        if let authors = bookInfo["authors"] as? NSArray {
            book.authors = authors.componentsJoinedByString(",")
        }
        if let averageRating = bookInfo["averageRating"] as? Int {
            book.rating = min( max(averageRating, 1), 5)
        }
        if let pageCount = bookInfo["pageCount"] as? Int {
            book.pageCount = pageCount
        }
        if let imageLinks = bookInfo["imageLinks"] as? NSDictionary {
            if let thumbnail_small = imageLinks["smallThumbnail"] as? String {
                book.thumbnailURL = thumbnail_small
            }
            else if let thumbnail = imageLinks["thumbnail"] as? String {
                book.thumbnailURL = thumbnail
            }
        }
        book.ratingImage = UIImage(named: "stars\(book.rating)")!
        
        return book
    }
    
    private func findPrice(salesInfo: NSDictionary, bookReceived: Book) -> Book{
        var book = bookReceived
        
        if let listprice = salesInfo["listPrice"] as? NSDictionary {
            if let amount1 = listprice["amount"] as? Int {
                book.price = "\(amount1) $"
            }
        }
        else if let retailprice = salesInfo["retailPrice"] as? NSDictionary {
            if let amount2 = retailprice["amount"] as? Int {
                book.price = "\(amount2) $"
            }
        }
        
        return book
    }
    
    private func findPDFLink(accessInfo: NSDictionary, bookReceived: Book) -> Book {
        var book = bookReceived
        var isPDFAvailable = false
        if let webReaderURL = accessInfo["webReaderLink"] as? String {
            book.pdfURL = webReaderURL
        }
        return book
    }
    
    
    func parseJSON(json: NSDictionary, searchText: String) {
        if let totalItems = json["totalItems"] as? Int {
            totalBooks = totalItems
        }
        if let items = json["items"] as? NSArray {
            for item in items {
               var book = Book()
                if let bookInfo = item["volumeInfo"] as? NSDictionary  {
                    book = parseBookInfo(bookInfo, book_received: book, searchText: searchText)
                }
                if let saleInfo = item["saleInfo"] as? NSDictionary {
                    book = findPrice(saleInfo, bookReceived: book)
                }
                if let accessInfo = item["accessInfo"] as? NSDictionary {
                    book = findPDFLink(accessInfo, bookReceived: book)
                }
                bookArray.append(book)
            }
        }
        
    }
    
    
}