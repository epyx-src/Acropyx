======================================
== Quick start guide for developers ==
======================================

Modify /etc/hosts

    > Add 
        # Acropyx tests
        127.0.0.1 test1.localhost
        127.0.0.1 test2.localhost
        
To run the app:

    > grails run-app
    
Open the admin application   

    > Open http://test1.localhost or change the http port in conf/BuildConfig : grails.server.port.http={your port}

Open the displayer

    > On the admin application, click on the link "Open the displayer"
    
    
======================================
== Import pilots from acroleague.ch ==
====================================== 
    
    > with phpMyAdmin, run the following query and export the data to excel
        
        SELECT firstname, name, birthdate, flysince, job, glider, sponsors, successes, mod_listing_images.imgname
        FROM mod_listing
        INNER JOIN mod_listing_images ON mod_listing.itemid = mod_listing_images.itemid
        GROUP BY firstname, name
        
    > open the file in excel, add an empty column after firstname and lastname for the country
    
    > execute the following excel macro to sanitize all the cells texts (remove ',', ';' and new lines)
    
        Sub ReplaceCarriageReturns()
            Selection.Replace Chr(44), " "
            Selection.Replace Chr(59), " "
            Selection.Replace Chr(13), " "
            Selection.Replace Chr(13), " "
        End Sub
        
    > export to a csv file, if needed replace ';' by ',' with a text editor and save the csv file in UTF-8 format
    
======================================
== Monitoring                       ==
======================================

    > connect to the acropyx server with ssh
        
        ssh -i your-amazon-EC2-privatekey.pem -D1234 ubuntu@acropyx.com
        
    > launch jconsole
        
        jconsole -J-DsocksProxyHost=localhost -J-DsocksProxyPort=1234 service:jmx:rmi:///jndi/rmi://internal-ip-amazon:8999/jmxrmi         
    
