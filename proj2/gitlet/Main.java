package gitlet;

import java.io.IOException;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 *
 * @author TODO
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {
        //if args is empty
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init":
                try {
                    Repository.initGitlet();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "add":
                Repository.judgeInit();
                //判断是否初始化了
                if (args.length < 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.addGitlet(args[1]);
                break;
            case "commit":
                Repository.judgeInit();
                if (args.length < 2) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
                Repository.commitGitlet(args[1]);
                break;
            case "rm":
                Repository.judgeInit();
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.remove(args[1]);
                break;
            case "log":
                Repository.judgeInit();
                if (args.length != 1) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.log();
                break;
            case "global-log":
                Repository.judgeInit();
                if (args.length != 1) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.globalLog();
                break;
            case "find":
                Repository.judgeInit();
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.find(args[1]);
                break;
            case "status":
                Repository.judgeInit();
                if (args.length != 1) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.getStatus();
                break;
            case "checkout":
                Repository.judgeInit();
                if (args[1].equals("--")) {
                    if (args.length != 3) {
                        System.out.println("Incorrect operands.");
                        System.exit(0);
                    }
                    Repository.checkout(args[2]);
                } else if (args.length == 4 && args[2].equals("--")) {
                    Repository.checkout(args[1], args[3]);
                } else if (args.length == 2) {
                    Repository.checkoutWithBranch(args[1]);
                } else {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                break;
            case "branch":
                Repository.judgeInit();
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.setNewBranch(args[1]);
                break;
            case "rm-branch":
                Repository.judgeInit();
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.deleteBranch(args[1]);
                break;
            case "reset":
                Repository.judgeInit();
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.reset(args[1]);
                break;
            // TODO: FILL THE REST IN

            default:
                System.out.println("Please enter a command.");
                System.exit(0);
        }
    }
}
