package src;

import java.util.Scanner;

public class Game {
	private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CLEAR = "\u001B[H\u001B[2J";

    private final Board board;
    private final Scanner scanner;

    public Game(int size) {
        this.board = new Board(size);
        this.scanner = new Scanner(System.in);
    }
    
    public void run() {
        board.addRandomTile();
        board.addRandomTile();

        String lastMessage = "Use W/A/S/D then Enter. Q to quit.";
        
        while (true) {
            clearScreen();
            printBoard();
            System.out.println("Move (W/A/S/D, Q=quit)");
            System.out.println("Status: " + lastMessage);

            if (!board.canMoveAnyDirection()) {
                clearScreen();
                printBoard();
                System.out.println("Game Over");
                System.out.println("Max tile: " + board.getMaxTile());
                break;
            }

            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                lastMessage = "Please enter W/A/S/D or Q.";
                continue;
            }

            char command = Character.toUpperCase(input.charAt(0));
            if (command == 'Q') {
                lastMessage = "Quit game.";
                clearScreen();
                printBoard();
                System.out.println(lastMessage);
                break;
            }

            int dir = toDirection(command);
            if (dir == -1) {
                lastMessage = "Invalid input. Please use W/A/S/D.";
                continue;
            }

            boolean moved = board.move(dir);
            if (moved) {
                board.addRandomTile();
                lastMessage = "Moved " + command + ".";
            } else {
                lastMessage = "No tiles moved. Try another direction.";
            }
        }
    }

    private int toDirection(char command) {
        if (command == 'D') return Board.RIGHT;
        if (command == 'A') return Board.LEFT;
        if (command == 'W') return Board.UP;
        if (command == 'S') return Board.DOWN;
        return -1;
    }
    
    private void clearScreen() {
        System.out.print(ANSI_CLEAR);
        System.out.flush();
    }

    private void printBoard() {
        int size = board.getSize();
        int cellWidth = 6;

        printHorizontalLine(size, cellWidth);
        for (int i = 0; i < size; i++) {
            System.out.print("|");
            for (int j = 0; j < size; j++) {
                int value = board.getCell(i, j);
                System.out.print(colorize(value, cellWidth));
                System.out.print("|");
            }
            System.out.println();
            printHorizontalLine(size, cellWidth);
        }
        System.out.println();
    }
    
    private String colorize(int value, int cellWidth) {
        String content = (value == 0) ? String.format("%" + cellWidth + "s", ".")
                : String.format("%" + cellWidth + "d", value);
        return getAnsiColor(value) + content + ANSI_RESET;
    }

    private String getAnsiColor(int value) {
        if (value == 0) return "\u001B[90m"; // bright black
        if (value == 2) return "\u001B[37m"; // white
        if (value == 4) return "\u001B[36m"; // cyan
        if (value == 8) return "\u001B[32m"; // green
        if (value == 16) return "\u001B[33m"; // yellow
        if (value == 32) return "\u001B[93m"; // bright yellow
        if (value == 64) return "\u001B[91m"; // bright red
        if (value == 128) return "\u001B[35m"; // magenta
        if (value == 256) return "\u001B[95m"; // bright magenta
        if (value == 512) return "\u001B[34m"; // blue
        if (value == 1024) return "\u001B[94m"; // bright blue
        return "\u001B[31;1m"; // 2048+
    }

    private void printHorizontalLine(int size, int cellWidth) {
        for (int i = 0; i < size; i++) {
            System.out.print("+");
            for (int j = 0; j < cellWidth; j++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
    }
}
