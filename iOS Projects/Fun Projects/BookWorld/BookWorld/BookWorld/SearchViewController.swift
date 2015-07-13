//
//  SearchViewController.swift
//  BookWorld
//
//  Created by Govindaraju, Saranath on 7/4/15.
//  Copyright (c) 2015 Saranath Govindaraju. All rights reserved.
//

import UIKit

class SearchViewController: UIViewController, NSURLConnectionDelegate, UITextFieldDelegate, UITableViewDataSource, UITableViewDelegate, UICollectionViewDataSource, UICollectionViewDelegate, NSURLConnectionDataDelegate {
    
    var bookModel = BookModel()
    var searchText: String = "iOS Programming" {
        didSet {
            startIndex = 1
            noResultLabel.hidden = true
            hidesTableAndCollectionView()
            activityIndicator.hidden = false
            activityIndicator.startAnimating()
            bookModel.bookArray.removeAll()
            startConnection()
        }
    }
    var isSwitchingToCollectionView: Bool = true {
        didSet {
            switchToCollectionView()
        }
    }
    var row = 0
    lazy var data = NSMutableData()
    var startIndex = 1
    
    // MARK: - UI Outlets and Actions
    
    @IBOutlet weak var searchTextField: UITextField!
    @IBOutlet weak var toolBar: UIToolbar!
    @IBOutlet weak var activityIndicator: UIActivityIndicatorView!
    @IBOutlet weak var noResultLabel: UILabel!
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var tableView: UITableView!
    
    @IBAction func switchingViews(sender: UIBarButtonItem) {
        switch sender.tag {
        case 1:
            isSwitchingToCollectionView = false
        case 2:
            isSwitchingToCollectionView = true
        default:
            break
        }
    }
    
    
    @IBAction func loadMoreResult(sender: UIBarButtonItem) {
            hidesTableAndCollectionView()
            activityIndicator.hidden = false
            activityIndicator.startAnimating()
            startIndex += 21
            startConnection()
    }
    
    // MARK: - Constants
    
    private struct Constants {
        static let APP_KEY = "AIzaSyCJNhKLrQm1zeuhK_TV1k_9opl5gv1016A"
        static let SEARCH_URL = "https://www.googleapis.com/books/v1/volumes?q="
    }
    
    private struct StoryboardConstants {
        static let TABLEVIEW_CELL_IDENTIFIER = "TableCell"
        static let COLLECTIONVIEW_CELL_IDENTIFIER = "CollectionCell"
    }

    
    // MARK:- Google Books API
    
    func startConnection(){
        var searchTextLowerCaseNoSpace = searchText.lowercaseString.stringByReplacingOccurrencesOfString(" ", withString: "+")
        let urlPath: String = Constants.SEARCH_URL + searchTextLowerCaseNoSpace + "&key=" + Constants.APP_KEY + "&maxResults=20&startIndex=\(startIndex)"
        if let url = NSURL(string: urlPath) {
            var request: NSURLRequest = NSURLRequest(URL: url)
            if let connection = NSURLConnection(request: request, delegate: self, startImmediately: false) {
                connection.start()
            }
        }
    }

    // MARK: - NSURLConnection Data Delegate
    
    func connection(connection: NSURLConnection, didReceiveResponse response: NSURLResponse) {
        self.data.length = 0
    }
    
    // MARK: - NSURLConnection Delegate
    
    func connection(connection: NSURLConnection!, didReceiveData data: NSData!){
        self.data.appendData(data)
        parseResult()
    }
    
    
    func parseResult() {
        if let jsonResult = NSJSONSerialization.JSONObjectWithData(data, options: NSJSONReadingOptions.MutableContainers, error: nil) as? NSDictionary {
            bookModel.parseJSON(jsonResult, searchText: searchText)
            activityIndicator.stopAnimating()
            noSearchResult()
            reload()
        }
        else if bookModel.totalBooks == 0 {
            noResultLabel.hidden = false
        }
    }
    
    // MARK: - TableView DataSource
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return bookModel.bookArray.count
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier(StoryboardConstants.TABLEVIEW_CELL_IDENTIFIER, forIndexPath: indexPath) as BookTableViewCell
        cell.book = bookModel.bookArray[indexPath.row]
        return cell
    }
    
    // MARK: - TableView Delegate
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        row = indexPath.row
        performSegueWithIdentifier("Show Preview", sender: self)
   }
    
    // MARK: - CollectionView Datasource
    
    func collectionView(collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return bookModel.bookArray.count
    }
    
    func collectionView(collectionView: UICollectionView, cellForItemAtIndexPath indexPath: NSIndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCellWithReuseIdentifier(StoryboardConstants.COLLECTIONVIEW_CELL_IDENTIFIER, forIndexPath: indexPath) as BookCollectionViewCell
        cell.book = bookModel.bookArray[indexPath.row]
        return cell
    }
    
    // MARK: - CollectionView Delegate
    
    func collectionView(collectionView: UICollectionView, didSelectItemAtIndexPath indexPath: NSIndexPath) {
        row = indexPath.row
        performSegueWithIdentifier("Show Preview", sender: self)
    }
    
    // MARK: - Segue
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        var destination = segue.destinationViewController as? UIViewController
        if let detailVC = destination as? PreviewViewController {
            if let identifier = segue.identifier {
                switch identifier {
                    case "Show Preview":
                            detailVC.book = bookModel.bookArray[row]
                    default: break
                }
            }
        }
    }
    
    // MARK: - UITextField Delegate
    
    func textFieldShouldReturn(userText: UITextField!) -> Bool {
        userText.resignFirstResponder()
        searchText = searchTextField.text
        return true;
    }
    
    // MARK: - Resign First Responder

    override func touchesBegan(touches: NSSet, withEvent event: UIEvent) {
        searchTextField.resignFirstResponder()
    }
    

    // MARK: - VC LifeCycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.allowsMultipleSelection = true
        view.addSubview(activityIndicator)
        activityIndicator.startAnimating()
        startConnection()
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        noResultLabel.hidden = true
    }
    
    // MARK: - Miscellaneous 
    
    func hidesTableAndCollectionView() {
        tableView.hidden = true
        collectionView.hidden = true
    }
    
    func reload(){
        self.collectionView.reloadData()
        self.tableView.reloadData()
    }
    
    func startSearch() {
        if !(searchTextField.text == "") {
            searchText = searchTextField.text
        }
    }
    
    func noSearchResult() {
        activityIndicator.hidden = true
        if bookModel.bookArray.count < 1 {
            noResultLabel.hidden = false
            view.addSubview(noResultLabel)
        }
        else {
            noResultLabel.hidden = true
            switchToCollectionView()
        }
    }
    
    func switchToCollectionView() {
        activityIndicator.hidden = true
        noResultLabel.removeFromSuperview()
        if isSwitchingToCollectionView {
            tableView.hidden = true
            collectionView.hidden = false
            view.addSubview(collectionView)
        }
        else {
            collectionView.hidden = true
            tableView.hidden = false
            view.addSubview(tableView)
        }
        reload()
    }
}
