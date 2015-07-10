//
//  BookTableViewCell.swift
//  BookWorld
//
//  Created by Govindaraju, Saranath on 7/4/15.
//  Copyright (c) 2015 Saranath Govindaraju. All rights reserved.
//

import UIKit

class BookTableViewCell: UITableViewCell {

    var book: BookModel.Book? = nil {
        didSet{
            if book != nil {
                loadData()
            }
        }
    }
    
    var textField: UITextField? = nil
    
    // MARK: - UI Outlets
    
    @IBOutlet weak var bookThumbnailImage: UIImageView!
    @IBOutlet weak var bookTitleLabel: UILabel!
    @IBOutlet weak var bookSubTitleLabel: UILabel!
    @IBOutlet weak var bookAuthorsLabel: UILabel!
    @IBOutlet weak var ratingImage: UIImageView!
    @IBOutlet weak var bookPriceLabel: UILabel!
    
    // MARK: - Loading data into Cell
    
    func loadData() {
        bookTitleLabel.text = book?.title
        bookSubTitleLabel.text = book?.subTitle
        bookAuthorsLabel.text = book?.authors
        bookPriceLabel.text = book?.price
        ratingImage.image = book?.ratingImage
        if let imageURLString = book?.thumbnailURL {
            fetchBookImage(imageURLString)
        }
        
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    
    func fetchBookImage(imageURLString: String) {
        var imageURL = NSURL(string: imageURLString)
        let qos = Int(QOS_CLASS_USER_INITIATED.value)
        dispatch_async(dispatch_get_global_queue(qos, 0), { () -> Void in
            if let imageData = NSData(contentsOfURL: imageURL!) {
                dispatch_async(dispatch_get_main_queue(), { () -> Void in
                    self.bookThumbnailImage.image = UIImage(data: imageData)
                })
            }
            else {
                self.bookThumbnailImage.image = UIImage(named: "Unknown")
            }
        })
    }
    
        override func touchesBegan(touches: Set<NSObject>, withEvent event: UIEvent) {
        textField?.resignFirstResponder()
    }

}
