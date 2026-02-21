package src;

import java.util.Scanner;

public class Game {
	private final Board board;
    private final Scanner scanner;

    public Game(int size) {
        this.board = new Board(size);
        this.scanner = new Scanner(System.in);
    }
    
    public void run() {
        board.addRandomTile();
        board.addRandomTile();

        while (true) {
            printBoard();

            if (!board.canMoveAnyDirection()) {
                System.out.println("Game Over! No more valid moves.");
                System.out.println("Max tile: " + board.getMaxTile());
                break;
            }

            System.out.print("Move (W/A/S/D, Q=quit): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                continue;
            }

            char command = Character.toUpperCase(input.charAt(0));
            if (command == 'Q') {
                System.out.println("Quit game.");
                break;
            }

            int dir = toDirection(command);
            if (dir == -1) {
                System.out.println("Invalid input. Please use W/A/S/D.");
                continue;
            }

            boolean moved = board.move(dir);
            if (moved) {
                board.addRandomTile();
            } else {
                System.out.println("No tiles moved. Try another direction.");
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

    private void printBoard() {
        int size = board.getSize();
        int cellWidth = 6;

        System.out.println();
        printHorizontalLine(size, cellWidth);
        for (int i = 0; i < size; i++) {
            System.out.print("|");
            for (int j = 0; j < size; j++) {
                int value = board.getCell(i, j);
                if (value == 0) {
                    System.out.printf("%" + cellWidth + "s|", ".");
                } else {
                    System.out.printf("%" + cellWidth + "d|", value);
                }
            }
            System.out.println();
            printHorizontalLine(size, cellWidth);
        }
        System.out.println();
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
