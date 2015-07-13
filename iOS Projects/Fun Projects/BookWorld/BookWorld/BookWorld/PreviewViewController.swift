//
//  PreviewViewController.swift
//  BookWorld
//
//  Created by Govindaraju, Saranath on 7/4/15.
//  Copyright (c) 2015 Saranath Govindaraju. All rights reserved.
//

import UIKit

class PreviewViewController: UIViewController, UIWebViewDelegate {

    var book: BookModel.Book? = nil {
        didSet{
            if book != nil {
                loadURL()
            }
        }
    }
    
    // MARK: - UI Outlets
    
    
    @IBOutlet weak var webView: UIWebView!
    @IBOutlet weak var activityIndicator: UIActivityIndicatorView!
    
    // MARK: - VC LifeCycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        title = book?.title
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        activityIndicator.hidden = false
        activityIndicator.startAnimating()
        if let urlString = book?.pdfURL {
           var url = NSURL(string: urlString)
           var req = NSURLRequest(URL: url!)
           NSHTTPCookieStorage.sharedHTTPCookieStorage().cookieAcceptPolicy = NSHTTPCookieAcceptPolicy.Always
           self.webView.loadRequest(req)
        }

    }
    
    // MARK: - Loading URL
    
    private func loadURL() {
            }
    
    // MARK: - WebView Delegates
    
    func webViewDidStartLoad(webView: UIWebView) {
        activityIndicator.hidden = false
        activityIndicator.startAnimating()
    }
    
    func webViewDidFinishLoad(webView: UIWebView) {
        activityIndicator.stopAnimating()
        activityIndicator.hidden = true
    }
}
