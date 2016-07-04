//
//  ViewController.swift
//  diamed
//
//  Created by Jayaradha on 6/22/16.
//  Copyright (c) 2016 olisource. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    @IBOutlet weak var imgHealthy: UIImageView!
    @IBOutlet weak var lblMedication: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        // Create the lables
       lblMedication.text = "Baba Med"
        
        let urlPath = "http://ec2-52-91-142-211.compute-1.amazonaws.com:8089/app/getResult.json?ampm=1&MorningAfterMedicationBloodGlucose=140.0&Gender=1"
        let url = NSURL(string:urlPath)!
        let session = NSURLSession.sharedSession()
        var strmedlist = ""
        
        let task = session.dataTaskWithURL(url, completionHandler:{data,response,error -> Void in
            if (error != nil){
                println(error)
            
            }else
            {
               // println(data)
               let jsonResult =  NSJSONSerialization.JSONObjectWithData(data, options: NSJSONReadingOptions.MutableContainers, error: nil) as! NSDictionary
                
                let medication: AnyObject? = jsonResult["dataValue"]
                if let value1: AnyObject = jsonResult["dataValue"] {
                    println(value1)
                    dispatch_async(dispatch_get_main_queue()) {
                    
                        var myString = String(value1 as! Int)
                        var mymed = ""
                        if(myString=="0")
                        {
                            mymed = "Metformin 250mg"
                            self.lblMedication.text = mymed
                        }
                        else
                        {
                            mymed = "Metformin 500mg"
                            self.lblMedication.text = mymed
                        }
                        
                        
                    }
                }
                
                let newmed = medication as! Int
                
            }
        })
        
        task.resume()
        
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

