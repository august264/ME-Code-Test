import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class FinancialTransaction {
    /**
     * Convert string from date
     *
     * @param dateString date string
     * @return date
     */
    static Date dateFromString(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
        }
        return null;
    }

    /**
     * Conver date to string
     *
     * @param date date
     * @return date string
     */
    static String dateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formatter.format(date);
    }

    /**
     * Read transaction data from file
     *
     * @param filename transaction file
     * @return transactions
     */
    static List<Transaction> readTransactions(String filename) {
        // all transactions
        List<Transaction> transactions = new ArrayList<>();
        try {
            // file reader
            Scanner scanner = new Scanner(new File(filename));
            // skip header
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                // read each line
                String line = scanner.nextLine();
                // split by comma
                String[] parts = line.split(",");
                // transaction without related transaction
                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                }
                if (parts.length == 6) {
                    transactions.add(new Transaction(
                            parts[0], parts[1], parts[2], dateFromString(parts[3]),
                            Double.parseDouble(parts[4]),
                            TransactionType.valueOf(parts[5]),
                            ""));
                } else if (parts.length == 7) { // transaction with related transaction
                    transactions.add(new Transaction(
                            parts[0], parts[1], parts[2], dateFromString(parts[3]),
                            Double.parseDouble(parts[4]),
                            TransactionType.valueOf(parts[5]),
                            parts[6]));
                }
            }
            // close file
            scanner.close();
        } catch (FileNotFoundException e) {
        }
        // return
        return transactions;
    }

    public static void main(String[] args) {
        // read transaction file
        List<Transaction> trans = readTransactions("data.csv");

        // create scanner
        Scanner keyboard = new Scanner(System.in);

        // read input parameters from user
        System.out.println("accountId:");
        String accountId = keyboard.nextLine();
        System.out.println("from:");
        String fromDateStr = keyboard.nextLine();
        System.out.println("to:");
        String toDateStr = keyboard.nextLine();

        // convert string to date
        Date fromDate = dateFromString(fromDateStr);
        Date toDate = dateFromString(toDateStr);


        List<Transaction> candidates = new ArrayList<>();
        List<String> reversalList = new ArrayList<>();
        for (Transaction transaction : trans) {
            // do date filter
            if (transaction.getCreateAt().compareTo(fromDate) >= 0 &&
                    transaction.getCreateAt().compareTo(toDate) <= 0) {
                // do account id filter
                if (accountId.equals(transaction.getFromAccountId()) || accountId.equals(transaction.getToAccountId())) {
                    // add to result
                    candidates.add(transaction);
                }
            }
            if (transaction.getTransactionType() == TransactionType.REVERSAL) {
                reversalList.add(transaction.getRelatedTransaction());
            }
        }

        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : candidates) {
            if (!reversalList.contains(transaction.getTransactionId())) {
                result.add(transaction);
            }
        }


        // print table header
        System.out.printf("%-15s %-15s %-15s %-25s %-15s %-15s %-15s\n",
                "transactionId", "fromAccountId", "toAccountId", "createdAt", "amount", "transactionType", "relatedTransaction");
        // print matched records
        for (Transaction transaction : result) {
            System.out.printf("%-15s %-15s %-15s %-25s %-15.2f %-15s %-15s\n",
                    transaction.getTransactionId(),
                    transaction.getFromAccountId(),
                    transaction.getToAccountId(),
                    dateToString(transaction.getCreateAt()),
                    transaction.getAmount(),
                    transaction.getTransactionType(),
                    transaction.getRelatedTransaction()
            );
        }

        System.out.println();
        // calculate balance
        double balance = 0;
        for (Transaction transaction : result) {
            if (accountId.equals(transaction.getFromAccountId())) {
                if (transaction.getTransactionType() == TransactionType.PAYMENT) {
                    balance -= transaction.getAmount();
                }
            } else if (accountId.equals(transaction.getToAccountId())) {
                if (transaction.getTransactionType() == TransactionType.PAYMENT) {
                    balance += transaction.getAmount();
                }
            }
        }

        // print final statistics result
        System.out.printf("Relative balance for the period is: $%.2f\n", balance);
        System.out.printf("Number of transactions included is: %d\n", result.size());

    }
}
