package cron;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

//========================================================================================
/*                                                                                      *
 *                                        Parser                                        *
 *                                                                                      */
//========================================================================================

public class App {
    // Configuration variables
    private HashMap<String, int[]> fields = new HashMap<>();
    private String[] fieldOrder = { "minute", "hour", "dayMonth", "month", "dayWeek" };

    // Stores the result table
    private HashMap<String, String> result = new HashMap<>();

    // colors
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";

    // ──── Constructor
    // ───────────────────────────────────────────────────────────────────────
    public App() {
        this.config();
    }

    // ──── Validation and Config
    // ─────────────────────────────────────────────────────────────
    // Parse program argument into cron fields and command
    public String[] parseAndValidateArguments(String input) {
        String[] list = input.split(" ");
        if (list.length < 6) {
            printError("Please provide an input");
            return null;
        }
        return list;
    }

    public void config() {
        fields.put("minute", new int[] { 0, 59 });
        fields.put("hour", new int[] { 0, 23 });
        fields.put("dayMonth", new int[] { 1, 31 });
        fields.put("month", new int[] { 1, 12 });
        fields.put("dayWeek", new int[] { 0, 7 });
    }

    public HashMap<String, String> getResult() {
        return this.result;
    }

    // ──── Main Parse Function
    // ───────────────────────────────────────────────────────────────
    // Parse cron field and apply corresponding rule function and limits (start end)
    public boolean parseCronString(String[] fields) {
        for (int i = 0; i < fields.length; i++)
            // step
            if (fields[i].contains("*/")) {
                int start = this.fields.get(fieldOrder[i])[0];
                int end = this.fields.get(fieldOrder[i])[1];
                boolean parsed = parseStep(fields[i], fieldOrder[i], start, end);
                if (!parsed)
                    System.exit(0);
                // star
            } else if (fields[i].equals("*")) {
                int start = this.fields.get(fieldOrder[i])[0];
                int end = this.fields.get(fieldOrder[i])[1];
                boolean parsed = parseStar(fields[i], fieldOrder[i], start, end);
                if (!parsed)
                    System.exit(0);
                // range
            } else if (fields[i].contains("-") && fields[i].length() > 2) {
                int start = this.fields.get(fieldOrder[i])[0];
                int end = this.fields.get(fieldOrder[i])[1];
                boolean parsed = parseRange(fields[i], fieldOrder[i], start, end);
                if (!parsed)
                    System.exit(0);
                // sequence
            } else if (fields[i].contains(",")) {
                int start = this.fields.get(fieldOrder[i])[0];
                int end = this.fields.get(fieldOrder[i])[1];
                boolean parsed = parseSequence(fields[i], fieldOrder[i], start, end);
                if (!parsed)
                    System.exit(0);
                // base
            } else {
                int start = this.fields.get(fieldOrder[i])[0];
                int end = this.fields.get(fieldOrder[i])[1];
                boolean parsed = parseBase(fields[i], fieldOrder[i], start, end);
                if (!parsed)
                    System.exit(0);
            }
        return true;
    }

    // ──── Rule parsers
    // ──────────────────────────────────────────────────────────────────────
    // parse base function "n"
    public boolean parseBase(String field, String key, int start, int end) {
        try {
            int n = Integer.parseInt(field);
            if (!(n >= start && n <= end)) {
                printError("Invalid " + key + " format");
                return false;
            }
            this.result.put(key, n + "");
        } catch (NumberFormatException ex) {
            printError("Invalid " + key + " format");
            return false;
        }
        return true;
    }

    // parse and expand range function "i-j" where i < j
    public boolean parseRange(String field, String key, int start, int end) {
        StringBuilder expanded = new StringBuilder();
        try {
            String[] range = field.split("-");
            int i = Integer.parseInt(range[0]);
            int j = Integer.parseInt(range[1]);
            if (!(i >= start && j <= end && i < j)) {
                printError("Invalid range " + key + " format");
                return false;
            }
            for (int it = i; it <= j - 1; it++)
                expanded.append(it + " ");
            expanded.append(j);
            this.result.put(key, expanded.toString());
            return true;
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // parse and expand step function "*/"
    public boolean parseStep(String field, String key, int start, int end) {
        StringBuilder expanded = new StringBuilder();
        try {
            int step = Integer.parseInt(field.substring(2, field.length()));
            if (!(step >= start && step <= end)) {
                printError("Invalid step " + key + " format");
                return false;
            }
            for (int i = start; i < end; i += step) {
                expanded.append(i + " ");
            }
            expanded.deleteCharAt(expanded.length() - 1);
            this.result.put(key, expanded.toString());
            return true;
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // parse and expand sequence function "x,y,z"
    public boolean parseSequence(String field, String key, int start, int end) {
        StringBuilder expanded;
        try {
            String[] sequence = field.split(",");
            Arrays.sort(sequence);
            for (String s : sequence) {
                int i = Integer.parseInt(s);
                if (!(i >= start && i <= end)) {
                    printError("Invalid sequence " + key + " format");
                    return false;
                }
            }
            expanded = new StringBuilder(String.join(" ", sequence));
            this.result.put(key, expanded.toString());
            return true;
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // parse and expand star function "*"
    public boolean parseStar(String field, String key, int start, int end) {
        StringBuilder expanded = new StringBuilder();
        for (int i = start; i <= end - 1; i++) {
            expanded.append(i + " ");
        }
        expanded.append(end);
        this.result.put(key, expanded.toString());
        return true;
    }

    public void addCommand(String command) {
        this.result.put("command", command);
    }

    // ──── Output and Print Out
    // ──────────────────────────────────────────────────────────────

    public void printResult() {
        printTableHeader();
        for (String key : this.result.keySet()) {
            printRow(key, this.result.get(key));
        }
    }

    public void printTableHeader() {
        System.out.println(ANSI_YELLOW + "\nResult\n");
        System.out.println(String.format("%15s %5s %-30s", ANSI_YELLOW + "Key", "|", "Expanded"));
        StringBuilder lines = new StringBuilder();
        int i = 0;
        while (i++ < this.result.values().stream().mapToInt(String::length).max().getAsInt() + 20)
            lines.append("-");
        System.out.println(String.format("%s", lines.toString()));
    }

    public void printRow(String key, String value) {
        System.out.println(String.format("%15s %5s %-30s", ANSI_GREEN + key, "|", value));
    }

    public void printError(String error) {
        System.out.println(ANSI_RED + error);
    }

    // ──── Main method
    // ───────────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        App cron = new App();

        if (args.length > 0) {
            String[] parsed = cron.parseAndValidateArguments(args[0]);
            if (parsed == null)
                System.exit(0);

            // Separate the cron fields to the command
            String[] cronFields = new String[5];
            for (int i = 0; i < 5; i++)
                cronFields[i] = parsed[i];

            cron.parseCronString(cronFields);
            cron.addCommand(parsed[5]);
            cron.printResult();
        } else {
            System.out.println("Please provide an input");
        }

    }
}
