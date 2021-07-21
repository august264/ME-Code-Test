@Author: Jianquan Li
@File Created: 20/07/2021
@Last Modified: 21/07/2021
@Editor: ItelliJ
@Jave Version: 16
@Project Description: This program is for the Code Challgenge of ME bank, which 
implements a system that will display related transcations and associated account
balance accroding to user's input.

@Code Explaination: The basic idea for this task can be divided into 3 parts roughly.
	1. .csv file handling - Scanning the csv file and handle the data format 
	properly
	2. compare date and accountID to get the right transactions. Cope with 
	balance caculation especially for REVERSAL Transactions. In my design, 
	"transaction" should be a class, the type of a transaction should be an
	enum.
	3. Display the result.



==================================================================================
PLEASE ENTER VALID INPUT FOR TESTING. IT IS CASE SENSITIVE.
==================================================================================
How To Run:
	1. Using Intellij to open the project.
	2. Go to ME Code Test\Src to find FinancialTransaction, the main() function 
	is in it.
	3. Configure the environment to Java 16.
	4. Run


Result:

accountId:
ACC334455
from:
20/10/2018 12:00:00
to:
20/10/2018 19:00:00
transactionId   fromAccountId   toAccountId     createdAt                 amount          transactionType relatedTransaction
TX10001         ACC334455       ACC778899       20/10/2018 12:47:55       25.00           PAYMENT                        

Relative balance for the period is: $-25.00
Number of transactions included is: 1