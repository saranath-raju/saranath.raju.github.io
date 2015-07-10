//
//  BookDetailViewController.swift
//  BookWorld
//
//  Created by Govindaraju, Saranath on 7/4/15.
//  Copyright (c) 2015 Saranath Govindaraju. All rights reserved.
//

import UIKit

class BookDetailViewController: UIViewController, NSURLConnectionDelegate {

    var bookModel = BookModel()
    lazy var data = NSMutableData()
    var book: BookModel.Book? = nil {
        didSet{
            if book != nil {
                loadData()
            }
        }
    }
    var searchText: String = "iOS Programming"
    
    //MARK: - UI Outlets
    
    @IBOutlet weak var bookThumbNailImage: UIImageView!
    @IBOutlet weak var bookTitleLabel: UILabel!
    @IBOutlet weak var bookSubtitleLabel: UILabel!
    @IBOutlet weak var bookAuthorLabel: UILabel!
    @IBOutlet weak var bookPublisherlabel: UILabel!
    @IBOutlet weak var bookRatingImage: UIImageView!
    @IBOutlet weak var previewButton: UIButton!
    @IBOutlet weak var bookDescriptionView: UITextView!
    @IBOutlet weak var bookPublishedDateLabel: UILabel!
    @IBOutlet weak var bookPriceLabel: UILabel!
    @IBOutlet weak var bookPreviewButton: UIButton!
    
    // MARK: - VC LifeCycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        startConnection()
    }
    
    
    
    private struct Constants {
        static let APP_KEY = "AIzaSyCJNhKLrQm1zeuhK_TV1k_9opl5gv1016A"
        static let SEARCH_URL = "https://www.googleapis.com/books/v1/volumes?q="
    }

    
    func startConnection(){
        var searchTextLowerCaseNoSpace = searchText.lowercaseString.stringByReplacingOccurrencesOfString(" ", withString: "+")
        let urlPath: String = Constants.SEARCH_URL + searchTextLowerCaseNoSpace + "&key=" + Constants.APP_KEY
        if let url = NSURL(string: urlPath) {
            var request: NSURLRequest = NSURLRequest(URL: url)
            if let connection = NSURLConnection(request: request, delegate: self, startImmediately: false) {
                connection.start()
            }
        }
    }
    
    
    // MARK: - NSURLConnection Delegate
    
    func connection(connection: NSURLConnection!, didReceiveData data: NSData!){
        self.data.appendData(data)
        if let jsonResult = NSJSONSerialization.JSONObjectWithData(data, options: NSJSONReadingOptions.MutableContainers, error: nil) as? NSDictionary {
            bookModel.parseJSON(jsonResult, searchText: searchText)
            book = bookModel.bookArray[0]
            println(book?.authors)
        }
    }

    func connectionDidFinishLoading(connection: NSURLConnection!) {
        var err: NSError
        if let jsonResult = NSJSONSerialization.JSONObjectWithData(data, options: NSJSONReadingOptions.MutableContainers, error: nil) as? NSDictionary {
            bookModel.parseJSON(jsonResult, searchText: searchText)
            book = bookModel.bookArray[0]
            println(book?.authors)
        }
    }

    // MARK: - Loading Data to View
    
    private func loadData() {
        if let imageURLString = book?.thumbnailURL {
            fetchBookImage(imageURLString)
        }
        bookTitleLabel.text = book?.title
        bookSubtitleLabel.text = book?.subTitle
        bookAuthorLabel.text = book?.authors
        bookPublisherlabel.text = book?.publisher
        bookPublishedDateLabel.text = book?.publishedDate
        bookRatingImage.image = book?.ratingImage
        bookPriceLabel.text = book?.price
        bookDescriptionView.text = book?.description
        if let pdfLink = book?.pdfURL {
            bookPreviewButton.setTitle("Preview Available", forState: UIControlState.Normal)
            bookPreviewButton.enabled = true
        }
        else {
            bookPreviewButton.setTitle("Preview Not Available", forState: UIControlState.Normal)
            bookPreviewButton.enabled = false
        }
    }
    
    func fetchBookImage(imageURLString: String) {
        var imageURL = NSURL(string: imageURLString)
        let qos = Int(QOS_CLASS_USER_INITIATED.value)
        dispatch_async(dispatch_get_global_queue(qos, 0), { () -> Void in
            if let imageData = NSData(contentsOfURL: imageURL!) {
                dispatch_async(dispatch_get_main_queue(), { () -> Void in
                    self.bookThumbNailImage.image = UIImage(data: imageData)
                })
            }
            else {
                self.bookThumbNailImage.image = UIImage(contentsOfFile: "Unknown")
            }
        })
    }

    // MARK: - Segue
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        var destination = segue.destinationViewController as? UIViewController
        if let previewVC = destination as? PreviewViewController {
            if let identifier = segue.identifier {
                switch identifier {
                case "showPreview":
                   previewVC.book = self.book
                default: break
                }
            }
        }

    }
}
