Pre-Requisite 
1. Java, TestNG & Maven should be installed before running this project.

Steps to run
1. Go to Test_CodeVita/src/main/java/CodeVita/Test_CodeVita
2. Run CompareIMDBWiki.java as TestNG test

About CompareIMDBWiki.java
To Search Movie by Name
1. Go to compareResult() method in CompareIMDBWiki.java file
2. Change movieName string value to desired movie (Note: Movie name should be complete & case Sensitive with all character present Eg: Pushpa: The Rise, Top Gun: Maverick)

Result format
1. In case both Country & Release date matches between Wiki & IMDB - we will get -> PASSED: compareResult
2. In case any of 2 mismatched result would be
      2.1 Release Date match failed - result -> Error Message: "Date Mismatch" &  FAILED: compareResult
      2.2 Origin Country match failed - result -> Error Message: "CountryMismatch" & FAILED: compareResult
