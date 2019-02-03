# credit-suisse-assignment
Assignment for log file analysis --


Steps to run
  1. Checkout code from github -- https://github.com/Dharmanna/credit-suisse-assignment.git
  2. Import gradle project into your eclipse (Assuming you already have gradle plug in installed)
  3. Go to Gradle Task view - Right click on project folder and Run Default Gradle Tasks
  4. After build successful --> Go to Gradle Task view --> Expand project folder --> Application --> run 
  5. Program will ask you for two arguments 
  
  
    a) File name - Give file stored in your local directory or default file name = testFile.txt
    
    b) Even though file based hsqldb is implemented you may get "User lacks privilege or object not found" exception due to previllage            issues.
       If exception :Data to be stored in file? Enter true or flase == true
            -- this will store data in file instead hsqldb
       else : Data to be stored in file? Enter true or flase  == false
            -- this will store data using hsqldb connections
            
            
   6. File data can be seen here - /credit-suisse-assignment/logFileEventsDbFile.data 
   7. hsqldb no issues then -- /credit-suisse-assignment/logFileEventsDb.data
   
   Thanks
   -Dharma
