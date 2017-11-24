/*
package SwirlTxt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SwirlTxt {
    // Commands
    private static final String[][] COMMANDS   = {{"open", "load", "o", "l"},
                                                  {"save", "s"},
                                                  {"use", "u"},
                                                  {"quit", "q"},
                                                  {"mode", "m"},
                                                  {"help", "h"},
                                                  {"man", "describe", "d"},
                                                  {"begin", "start", "play", "b"},
                                                  {"clear", "clr", "c"},
                                                  {"summarize", "summary", "v"},
                                                  {"results"},
                                                  {"synonyms", "aka"}};
    private static final String[][] FILE_TYPES = {{"standard", "std", "s", "swirl", "0"}}; //, ".swirl"?
    private static final String[][] MODES      = {{"standard", "std", "s", "0"},
                                                  {"debug", "verbose", "d", "v", "1"},
                                                  {"log", "l", "2"}};
    private static final String[][] YES_NO     = {{"no", "nope", "false", "negative", "0", "n"},
                                                  {"yes", "yeah", "true", "affirmative", "1", "y"},
                                                  {"maybe", "2", "m"},
                                                  {"unsure", "unknown", "no clue", "don't know", "?", "u"},
                                                  {"yes?", "no?", "possibly", "probably", "i think so"}};
    private static final String[][] DEFAULTS   = {{"default", "example"},
                                                  //{"simple", "basic"},
                                                  /*{"large", "advanced"}/};


    private static final int INVALID = -1;

    private static final int C_OPEN    = 0;
    private static final int C_SAVE    = 1;
    private static final int C_USE     = 2;
    private static final int C_QUIT    = 3;
    private static final int C_MODE    = 4;
    private static final int C_HELP    = 5;
    private static final int C_MAN     = 6;
    private static final int C_BEGIN   = 7;
    private static final int C_CLEAR   = 8;
    private static final int C_SUM     = 9;
    private static final int C_RESULTS = 10;
    private static final int C_SYN     = 11;

    private static final int T_SWIRL = 0;

    private static final int M_STD   = 0;
    private static final int M_DEBUG = 1;
    private static final int M_LOG   = 2;

    private static final int D_GIVEN    = 0;
    private static final int D_BASIC    = 1;
    private static final int D_ADVANCED = 2;

    private static final int NO         = 0;
    private static final int YES        = 1;
    private static final int MAYBE      = 2;
    private static final int UNSURE     = 3;
    private static final int INDECISIVE = 4;

    private static final int SCORE_CATEGORIES = 5;

    private static final boolean[] COMMANDLINE_VALID  =
            {true, false, true, false, true, false, false, false, true, true, false, false};
    private static final int[]     EXPECTED_ARGUMENTS = {1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1};


    // Descriptions
    private static final String[] COMMAND_DESCRIPTIONS   =
            {"Loads saved parameters from a given file, or allows the user to select a file if none is given.",
             "Saves a file.",
             "Sets the default file type.",
             "Ends the program.",
             "Changes to program mode.",
             "Provides help.",
             "Describes a command.",
             "Begins the current simulation.",
             "Clears the parameter info.",
             "Summarizes the current run.",
             "Displays the results.",
             "Displays synonyms for a term."};
    private static final String[] FILE_TYPE_DESCRIPTIONS = {"Swirl format"};
    private static final String[] MODE_DESCRIPTIONS      = {"Normal mode",
                                                            "Debug mode",
                                                            "Logging mode"};
    private static final String[] DEFAULTS_DESCRIPTIONS  =
            {"Basic default setting."};

    //private static final String F_DBUG_S = "log"; // Log flag

    //private static final String ERROR_UNKNOWN_COMMAND = "Failed to parse command: ";
    //private static final String ERROR_ARGUMENT_NUMBER = "Incorrect number of arguments: ";

    private static final String CLEAR_REGEX = "[()#]";
    private static final String STRIP_REGEX = "[.,:;!@#$%^&*~`\"]";

    private static final String ERROR_UNKNOWN_COMMAND = "Failed to parse command: ";
    private static final String ERROR_ARGUMENT_NUMBER = "Incorrect number of arguments: ";
    private static final String ERROR_UNKNOWN         = "Unknown: ";
    private static final String ERROR_FILE_READ       = "Unable to read file: ";
    private static final String ERROR_FILE_write      = "Unable to write file: ";
    private static final String ERROR_FILE_TYPE       = "Wrong file type: ";
    private static final String ERROR_TRYING_FILE     = "Trying as file...";
    private static final String ERROR_TRYING_AGAIN    = "Attempting to fix...";

    private static final String WARN_HAVE_GAME =
            "This will destroy the current settings, are you sure?";
    private static final String WARN_SAVE      = "Do you want to save your current settings?";
    private static final String WARN_SURE      = "Are you sure?";

    private static final String FAIL_NO_GAME = "Unable to proceed, no valid settings. (l to load settings)";
    private static final String FAIL_LOAD    = "Failed to load...";

    private static final String SETTINGS_LOADED    = "New settings loaded.";
    private static final String SETTINGS_CREATED   = "New settings created.";
    private static final String SETTINGS_CLEARED   = "Data cleared.";
    private static final String SETTINGS_SAVED     = "Data saved.";
    private static final String COMMAND_RECOGNIZED = "Command recognized: ";

    private static final String CURRENT_MODE      = "Current mode: ";
    private static final String CURRENT_FILE_TYPE = "Current default file type: ";

    private static final String RUN_QUERY    = "Run command ";

    private static final String[] SYNONYM    =
            {"The term ", " is the same as: "};

    private static final String[] THANKS    = {"Thanks!", "Thank you!"};
    private static final String[] BYE_HAPPY =
            {"Goodbye!", "Bye!", "So long!", "Come play again sometime!"};
    private static final String[] BYE_SAD   =
            {"Good riddance!", "I was done anyways...", "Don't come back!",
             "That means you forfeit!"};
    private static final String[][] STAYING   = {
            {"Now I can catch up...", "Good, I'm not done yet...", "Awesome, I need the practice..."},
            {"Now I can increase my lead!", "Yay! More games!", "My luck continues!"}};
    private static final String[] RESPONSE_MAYBE   =
            {"Yes or no please.", "I need a yes or no.", "You have to decide.", "I don't give hints."};
    private static final String[] RESPONSE_UNSURE   =
            {"Well look it up!", "I only accept yes or no.", "That is why they invented Wikipedia...",
             "I'm not a teacher..."};
    private static final String[] RESPONSE_INDECISIVE   =
            {"Be decisive!", "Be confident!", "That is unclear...",
             "Did you know that people find confidence attractive?"};
    private static final String[] SCORE_NO_PLAYS   =
            {"We haven't played any games yet!", "The score is tied 0-0, and I win ties!", "I haven't yet lost a game! (Type play to play our first game...)",
             "How can I have beaten you if you have yet to play?"};
    private static final String[][] SCORE_PREFACE   = {
            {"I've yet to win: ", "Apparently I have no points: ", "So far you have all the points: "},
            {"You are wining: ", "I'm losing: "},
            {"We are tied: ", "We are evenly matched: "},
            {"I'm beating you: ", "I'm wining: "},
            {"I am unbeaten: ", "I've got all the points: "}};
    private static final String[][] SCORE_RESPONSE   = {
            {"I'm just not ready yet!", "Wait, is that right?", "But I haven't started yet...", "Apparently my points aren't being scored..."},
            {"But I'll catch up...", "You're a tough opponent..."},
            {"We are roughly evenly matched...", "Though I feel a hot streak coming on."},
            {"I'm so good at this!", "Don't you know any hard animals?"},
            {"I am triumphant!", "I am undefeatable!", "What were you thinking, challenging me?", "Are you even trying?"}};



    private static final char[] PUNCTUATION = {'.', '!'};
    private static final char   QMARK       = '?';

    private GameTree<String> game;
    private int score = 0, played = 0;

    public final static String LOGFILE = "log_", ERRORFILE = "error_";
    // The default log file prefixes
    private Scanner     in; // The network and user input
    private PrintWriter out; // The network and diplay output
    private PrintWriter log; // The log out

    private int mode = 0, fileType = 0;
    private boolean logging; // Whether to log or not.
    private volatile boolean running = true;
    //Command last;

    public static void main(String[] args) throws IOException {
        GameDriver game = new GameDriver(args);
        game.run();
    }

    public SwirlTxt(String[] args) {

        Scanner lineScanner = new Scanner("the file stuff goes here");


        // file loop
        String line = lineScanner.nextLine();


        // prepare forline loop
        Scanner wordScanner = new Scanner(line);
        wordScanner.useDelimiter(",");

        wordScanner.hasNext(); // Start looping over words
        wordScanner.next();

        // end loop

        // end file loop







        out = new PrintWriter(System.out);
        in = new Scanner(System.in);

        String prev    = null;
        int    current = -1, have = 0;

        for (String s : args)
            try {
                if (prev == null) {
                    message("Processing command line argument: " + s);

                    int i = getCommandIndex(s);

                    if (i >= 0 && COMMANDLINE_VALID[i]) {
                        if (EXPECTED_ARGUMENTS[i] > 0) {
                            current = i;
                            prev = s;
                            have = 0;
                            continue;
                        }
                    }
                } else {
                    prev += " " + s;
                    have++;
                    if (have == EXPECTED_ARGUMENTS[current]) {
                        processCommand(prev.split(" "));
                        prev = null;
                    }
                }
            } catch (Exception e) {
                message("Invalid command line argument: " + s + "; error: " +
                        e); // Print the error.
            }

        if (game == null) {
            message("Initializing game to default...");
            game = GameTree.getDefault();
        }

        message("Hello! Please press h for help or b to begin game.");
    }

    private void run() {
        String r; // Temp variable

        try {
            message("Please enter command: ");
            while (running) {
                processCommand(in.nextLine().split(" ")); // Processes the incoming command.
                message("Please enter command: ");
            }
        } catch (Exception e) {
            message("Fatal Exception in game: " + e);
            log(e);
            running = false;
        }
    }

    private boolean processCommand(String[] args) {
        int  l, j, k;
        File file = null;
        if (args == null || (l = args.length) == 0) return false;
        l--;

        log(args[0]);
        int index = getCommandIndex(strip(args[0]));

        switch (index) {
            case C_MAN:
                if (l < EXPECTED_ARGUMENTS[index]) {
                    message(ERROR_ARGUMENT_NUMBER + l);
                    return false;
                }
                j = getCommandIndex(strip(args[1]));
                if (j < 0) {
                    message(ERROR_UNKNOWN_COMMAND + args[1]);
                    return false;
                }
                message(COMMAND_DESCRIPTIONS[j]);
                if (j == C_MODE) message(pairArrays("", MODES, MODE_DESCRIPTIONS));
                else if (j == C_USE) message(pairArrays("", FILE_TYPES, FILE_TYPE_DESCRIPTIONS));
                else if (j == C_OPEN) message(pairArrays("", DEFAULTS, DEFAULTS_DESCRIPTIONS));
                return true;
            case C_HELP:
                message(getHelp());
                return true;
            case C_MODE:
                if (l < EXPECTED_ARGUMENTS[index]) {
                    message(ERROR_ARGUMENT_NUMBER + l);
                    return false;
                }
                j = getIndex(strip(args[1]), MODES);
                if (j < 0) {
                    message(ERROR_UNKNOWN + args[1]);
                    return false;
                }
                setMode(j);
                message(CURRENT_MODE + MODE_DESCRIPTIONS[j]);
                return true;
            case C_QUIT:
                if (!confirm(WARN_SURE)) {
                    message(random((played > 0) ? (score*2)/played : 1, STAYING));
                    return false;
                }
                message(random((score > played / 2 || played < 3) ? BYE_HAPPY : BYE_SAD));
                if (hasGame() && confirm(WARN_SAVE)) {
                    processCommand(new String[]{COMMANDS[C_SAVE][0]});
                }
                debug("Finishing up...");
                finish();
                close();
                System.exit(0);
            case C_USE:
                if (l < EXPECTED_ARGUMENTS[index]) {
                    message(ERROR_ARGUMENT_NUMBER + l);
                    return false;
                }
                j = getIndex(strip(args[1]), FILE_TYPES);
                if (j < 0) {
                    message(ERROR_UNKNOWN + args[1]);
                    return false;
                }
                fileType = j;
                message(CURRENT_FILE_TYPE + FILE_TYPE_DESCRIPTIONS[j]);
                return true;
            case C_SAVE:
                if (!hasGame()) {
                    message(FAIL_NO_GAME);
                    return false;
                }
                if (l < EXPECTED_ARGUMENTS[index]) {
                    file = getSaveFile();
                    log(file);
                } else {
                    try {
                        file = new File(args[1]);
                    } catch (Exception e) {
                        message(ERROR_UNKNOWN + args[1]);
                        log(e);
                        return false;
                    }
                }
                if (file != null) {// && file.canWrite()
                    try {
                        game.writeToFile(file, fileType == T_SWIRL);
                        message(SETTINGS_SAVED);
                        return true;
                    } catch (Exception e) {
                        message(ERROR_FILE_write + file);
                        log(e);
                        return false;
                    }
                }
                message(FAIL_LOAD);
                return false;
            case C_OPEN:
                if (hasGame() && !confirm(WARN_HAVE_GAME)) {
                    return false;
                }
                if (l < EXPECTED_ARGUMENTS[index]) {
                    file = getGameFile();
                } else {
                    j = getIndex(args[1], DEFAULTS);
                    switch (j) {
                        case D_GIVEN:
                            try {
                                if (l < 5) {
                                    message(ERROR_ARGUMENT_NUMBER + l);
                                    message(ERROR_TRYING_FILE);
                                    //Intentionally no return
                                } else {
                                    GameTree<String> newGame  = new GameTree<>();
                                    newGame.setFirstQuestion(toQuestion(rebuild(4, args)), strip(args[2]), strip(args[3]));
                                    game = newGame;
                                    message(SETTINGS_CREATED);
                                    return true;
                                }
                            } catch (Exception e) {
                                message(ERROR_UNKNOWN + args[1]);
                                message(ERROR_TRYING_FILE);
                                //Intentionally no return
                            }
                        case D_BASIC:
                            game = GameTree.getDefault();
                            message(SETTINGS_LOADED);
                            return true;
                        case D_ADVANCED:
                            game = GameTree.getAdvancedDefault();
                            message(SETTINGS_LOADED);
                            return true;
                        default:
                            //Intentionally no return
                    }
                    try {
                        file = new File(args[1]);
                    } catch (Exception e) {
                        message(ERROR_UNKNOWN + args[1]);
                        return false;
                    }
                }
                if (file != null) { // && file.canRead()
                    GameTree<String> newGame;
                    try {
                        newGame = GameTree.readFromFile(file, fileType == T_SWIRL);
                        if (newGame == null) {
                            debug(ERROR_FILE_TYPE + FILE_TYPE_DESCRIPTIONS[fileType]);
                            newGame = GameTree.readFromFile(file, fileType != T_SWIRL);
                        }
                    } catch (Exception e) {
                        message(ERROR_FILE_READ + file);
                        return false;
                    }
                    if (newGame != null) {
                        game = newGame;
                        message(SETTINGS_LOADED);
                        return true;
                    }
                }
                message(FAIL_LOAD);
                return false;
            case C_BEGIN:
                if (hasGame()) {
                    runGame();
                } else {
                    message(FAIL_NO_GAME);
                }
                return true;
            case C_CLEAR:
                if (hasGame() && confirm(WARN_HAVE_GAME)) {
                    game = new GameTree<>();
                    message(SETTINGS_CLEARED);
                    return true;
                }
                return false;
            case C_SUM:
                if (!hasGame()) {
                    message(FAIL_NO_GAME);
                    return false;
                }
                message(game.getSummary());
                return true;
            case C_RESULTS:
                if (played == 0) {
                    message(random(SCORE_NO_PLAYS));
                    return true;
                }
                j = (score == 0) ? 0 :
                    (score == played) ? SCORE_CATEGORIES-1 :
                    (score == played-score) ? SCORE_CATEGORIES/2 :
                    (score > played-score) ? SCORE_CATEGORIES/2+1 : SCORE_CATEGORIES/2-1;
                debug(Integer.toString(j));
                double d = score*1.0d/played;
                debug(Double.toString(d));
                k = (score == 0) ? 0 :
                    (score == played) ? SCORE_CATEGORIES-1 :
                    (d < 2.0/5.0) ? SCORE_CATEGORIES/2-1 :
                    (d > 3.0/5.0) ? SCORE_CATEGORIES/2+1 : SCORE_CATEGORIES/2;
                debug(Integer.toString(k));
                message(random(SCORE_PREFACE[j])+score+":"+(played-score));
                message(random(SCORE_RESPONSE[k]));
                return true;
            case C_SYN:
                if (l < EXPECTED_ARGUMENTS[index]) {
                    message(ERROR_ARGUMENT_NUMBER + l);
                    return false;
                }
                String s = strip(args[1]);
                j = getCommandIndex(s);
                String[][] syn = COMMANDS;
                if (j < 0) j = getIndex(s, syn = FILE_TYPES);
                if (j < 0) j = getIndex(s, syn = MODES);
                if (j < 0) j = getIndex(s, syn = YES_NO);
                if (j < 0) j = getIndex(s, syn = DEFAULTS);
                if (j < 0) {
                    message(ERROR_UNKNOWN + args[1]);
                    return false;
                }
                message(SYNONYM[0]+args[1]+SYNONYM[1]+orList(0, syn[j]));
                return true;
            case INVALID:
                message(ERROR_UNKNOWN_COMMAND);
                return false;
        } //SCORE_NO_PLAYS SCORE_PREFACE SCORE_RESPONSE SYNONYM
        return false;
    }

    private boolean confirm(String question) {
        message(question);
        return getYesNoResponse(false);
    }

    private void runGame() {
        Position<String> current = game.root();
        boolean          yes, correct;
        if (current == null) {
            message(FAIL_NO_GAME);
            return;
        }
        while (game.isInternal(current)) {
            message(current.getContent());
            yes = getYesNoResponse(true);
            current = (yes) ? game.yes(current) : game.no(current);
        }

        if (Character.toLowerCase(current.getContent().charAt(0)) == 'a' ||
            Character.toLowerCase(current.getContent().charAt(0)) == 'e' ||
            Character.toLowerCase(current.getContent().charAt(0)) == 'i' ||
            Character.toLowerCase(current.getContent().charAt(0)) == 'o' ||
            Character.toLowerCase(current.getContent().charAt(0)) == 'u') {
            message(ANIMAL_QUERY_V + current.getContent() + "?");
        } else {
            message(ANIMAL_QUERY + current.getContent() + "?");
        }
        correct = getYesNoResponse(false);

        if (correct) {
            message(((played == 0) ? "First blood!" : random(score, EXCLAIM_GOOD)) + " " +
                    random(score, WIN));
            score++;
        } else {
            message(((played == 0) ? "I wasn't ready!" : random(played - score, EXCLAIM_BAD)) +
                    " " + random(played - score, LOSE));
            promptForQuestion(current);
        }

        played++;
        return;
    }

    private void promptForQuestion(Position<String> last) {
        message(random(played - score, STUMPED) + " " + random(played - score, WHAT));
        String  newAnswer = strip(in.nextLine());
        boolean order     = Math.random() > 0.5d;
        if (order) {
            message(NEW_QUERY[0] + newAnswer + NEW_QUERY[1] + last.getContent() + "?");
        } else {
            message(NEW_QUERY[0] + last.getContent() + NEW_QUERY[1] + newAnswer + "?");
        }
        String newQuestion = toQuestion(in.nextLine());

        game.addQuestion(last, newQuestion, newAnswer, order);

        message(random(THANKS));
    }

    private boolean getYesNoResponse(boolean allowFork) {
        boolean repeat                   = true, yes;
        String  original, s, commandText = "";
        int     response, command        = INVALID;

        while (repeat) {
            original = in.nextLine();
            s = strip(original);
            response = getIndex(s, YES_NO);

            if (response == MAYBE) {
                message(random(RESPONSE_MAYBE));
                continue;
            } else if (response == UNSURE) {
                message(random(RESPONSE_UNSURE));
                continue;
            } else if (response == INDECISIVE) {
                message(random(RESPONSE_INDECISIVE));
                continue;
            } else if (response == INVALID) {
                if (allowFork) {
                    response = getCommandIndex(s);
                    if (response == INVALID) {
                        message(ERROR_UNKNOWN + original);
                        continue;
                    }

                    if (command != INVALID) {
                        message(COMMAND_RECOGNIZED + COMMANDS[response]);
                        message(RUN_QUERY + COMMANDS[response] + "?");
                        commandText = original;
                        command = response;
                    }
                    continue;
                } else {
                    message(ERROR_UNKNOWN + original);
                    continue;
                }
            } else {
                yes = response == YES;
                if (command != INVALID) {
                    if (yes) {
                        processCommand(commandText.split(" "));
                        command = INVALID;
                    }
                    continue;
                }
                return yes;
            }
        }
        return false; // Unreachable
    }

    private String strip(String s) {
        return s.trim().toLowerCase().replaceAll(STRIP_REGEX, "");
    }

    private String toQuestion(String q) {
        String question = q.trim().replaceAll(CLEAR_REGEX, "?");
        while (q.contains("  ")) q = q.replaceAll("  ", " ");
        for (char c : PUNCTUATION)
            if (question.endsWith(c + ""))
                question = question.replace(c, '?');
        if (!question.endsWith("?")) question += "?";
        if (Character.isLowerCase(question.charAt(0))) {
            question = question.replaceFirst(Character.toString(question.charAt(0)), Character.toString(Character.toUpperCase(question.charAt(0))));
        }
        return question;
    }

    private static final String ZZZZ = "Failed to parse command: ";

    //  Array helper methods

    private int getCommandIndex(String s) {
        return getIndex(s, COMMANDS);
    }

    private int getIndex(String s, String[][] list) {
        for (int i = 0; i < list.length; i++) {
            for (int j = 0; j < list[i].length; j++) {
                if (strip(s).equals(list[i][j])) {
                    return i;
                }
            }
        }
        return INVALID;
    }

    private void append(StringBuilder sb, String[] args) {
        for (int i = 0; i < args.length; i++) sb.append(" ").append(args[i]);
    }

    private String orList(int j, String[] args) {
        StringBuilder sb = new StringBuilder(args[j]);
        for (int i = j + 1; i < args.length-1; i++) sb.append(", ").append(args[i]);
        sb.append(", or ").append(args[args.length-1]);
        return sb.toString();
    }

    private String rebuild(int j, String[] args) {
        StringBuilder sb = new StringBuilder(args[j]);
        for (int i = j + 1; i < args.length; i++) sb.append(" ").append(args[i]);
        return sb.toString();
    }

    private String rebuild(String[] args) {
        StringBuilder sb = new StringBuilder(args[0]);
        for (int i = 1; i < args.length; i++) sb.append(" ").append(args[i]);
        return sb.toString();
    }

    private static String getHelp() {
        return pairArrays("The following commands are supported:\n", COMMANDS, COMMAND_DESCRIPTIONS);
    }

    private static String pairArrays(String pre, String[][] args, String[] descriptions) {
        StringBuilder sb = new StringBuilder(pre);
        for (int i = 0; i < args.length; i++) {
            String[] arg = args[i];
            sb.append('\t').append(arg[0]);
            for (int j = 1; j < arg.length; j++) sb.append(", ").append(arg[j]);
            sb.append(":\n\t").append(descriptions[i]).append('\n');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private static String random(int i, String[][] args) {
        return random(args[(i > 0) ? 1 : 0]);
    }

    private static String random(String[] args) {
        return args[(int) Math.round(Math.random() * (args.length - 1))];
    }

    //  Output methods

    private void debug(String s) {
        log(s);
        if (mode == M_DEBUG) {
            out.println(s);
            out.flush();
        }
    }

    private void message(String s) {
        log(s);
        out.println(s);
        out.flush();
    }

    /**
     * If logging, tries to log something.
     *
     * @param s what to log
     *
    private void log(String s) {
        if (logging) try {
            log.println(s);
            log.flush();
        } catch (Exception e) {
            closeLog();
            System.err.println("Unable to log: " + s + "; " + e);
        }
    }

    /**
     * If logging, tries to log something.
     *
     * @param e Exception to log
     *
    private void log(Exception e) {
        if (logging) try {
            log.println(e);
            e.printStackTrace();
            log.flush();
        } catch (Exception e2) {
            closeLog();
            System.err.println("Unable to log: " + e + "; " + e2);
        }
        else {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    /**
     * If logging, tries to log something.
     *
     * @param o what to log
     *
    private void log(Object o) {
        if (logging) try {
            log.println(o);
            log.flush();
        } catch (Exception e) {
            closeLog();
            System.err.println("Unable to log: " + o + "; " + e);
        }
    }

    //  IO methods

    private void finish() {
        running = false;
    }

    private void close() {
        try {
            out.close();
            in.close();
            log.close();
        } catch (Exception ignored) {}
    }

    private void openLog() {
        logging = true;
        if (logging) try {
            String now = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            log = new PrintWriter(new FileOutputStream(LOGFILE + ".txt", true), true);
            log.println("********* New Log at " + now + "*********");
            log.flush();
            System.setErr(new PrintStream(new FileOutputStream(ERRORFILE + ".txt", true)));
            System.err.println("New Session on " + now);
        } catch (Exception e) {
            System.setErr(System.out);
            System.err.print(e);
            logging = false;
            log = null;
        }
    }

    private void closeLog() {
        logging = false;
        try {
            System.setErr(System.out);
            log.close();
        } catch (Exception ignored) {}
        log = null;
    }

    //  Save file methods

    private File getSaveFile() {
        log("Getting save file...");
        boolean check = true;
        while (check) try {
            JFileChooser chooser = new JFileChooser(".");
            chooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
            int n = chooser.showSaveDialog(null);
            if (n == JFileChooser.APPROVE_OPTION) {
                log("File given.");
                check = false;
                return chooser.getSelectedFile();
            }
            if (n == JFileChooser.CANCEL_OPTION) processCommand(new String[]{"q"});
        } catch (Exception e) {
            log(e);
        }
        return null;
    }

    private File getGameFile() {
        log("Getting game file...");
        boolean check = true;
        while (check) try {
            JFileChooser chooser = new JFileChooser(".");
            chooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
            int n = chooser.showOpenDialog(null);
            if (n == JFileChooser.APPROVE_OPTION) {
                log("File found.");
                check = false;
                return chooser.getSelectedFile();
            }
            if (n == JFileChooser.CANCEL_OPTION) processCommand(new String[]{"q"});
        } catch (Exception e) {
            log(e);
        }
        return null;
    }

    //  Helper methods

    private void setMode(int mode) {
        log("Setting mode: " + mode);
        switch (mode) {
            case M_DEBUG:
                closeLog();
                break;
            case M_LOG:
                logging = true;
                openLog();
                break;
            case M_STD:
            default:
                this.mode = M_STD;
                closeLog();
                return;
        }

        this.mode = mode;
    }

    private boolean hasGame() {
        return game != null && game.root() != null;
    }
}//*/
