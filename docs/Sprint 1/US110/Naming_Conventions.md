# Naming Conventions  

## General

* Use consistent and descriptive identifiers and names.  
* Make judicious use of white space and indentation to make code easier to read.  
* Include comments in SQL code where necessary.  
* Ensure the name is unique and does not exist as a reserved keyword.  
* Keep the length  
* Names must begin with a letter and may not end with an underscore.  
* Only use letters, numbers and underscores in names.  
* Avoid the use of multiple consecutive underscores—these can be hard to read.  
* Use underscores where you would naturally include a space in the name (first name becomes first_name).  
* Avoid abbreviations and if you have to use them make sure they are commonly understood.  
* Do not use the more natural collective term where possible instead.  
* Always use uppercase for the reserved keywords like SELECT and WHERE.  
* When declaring schema information it is also important to maintain human-readable code.  

## Tables  

* Use a collective name or, less ideally, a plural form. For example (in order of preference) staff and employees.  
* Never give a table the same name as one of its columns and vice versa.  
* Avoid, where possible, concatenating two table names together to create the name of a relationship table.  

## Columns  

* Always use the singular name.  
* Do not add a column with the same name as its table and vice versa.  
* Always use lowercase except where it may make sense not to such as proper nouns.

## Uniform suffixes  

The following suffixes have a universal meaning ensuring the columns can be read and understood easily from SQL code. Use the correct suffix where appropriate.




