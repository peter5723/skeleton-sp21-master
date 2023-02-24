package gitlet;

import java.io.IOException;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) throws IOException {
        //if args is empty
        if (args.length==0){
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                Repository.initGitlet();
                break;
            case "add":
                Repository.JudgeInit();
                //判断是否初始化了
                if (args.length < 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.addGitlet(args[1]);
                break;
            case "commit":
                Repository.JudgeInit();
                if(args.length < 2) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
                Repository.commitGitlet(args[1]);
                break;
            case "rm":
                Repository.JudgeInit();
                Repository.remove(args[1]);
                break;
            case "log":
                Repository.JudgeInit();
                Repository.log();
                break;
            case "global-log":
                Repository.JudgeInit();
                Repository.globalLog();
                break;
            // TODO: FILL THE REST IN

            default:
                System.out.println("Please enter a command.");
                System.exit(0);
        }
    }
}
