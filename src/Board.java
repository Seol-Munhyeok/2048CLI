package src;

import java.util.Arrays;
import java.util.Random;

public class Board {
	public static final int RIGHT = 0;
    public static final int LEFT = 1;
    public static final int UP = 2;
    public static final int DOWN = 3;

    private final int size;
    private final int[][] map;
    private final int[][] temp;
    private final Random random;

    public Board(int size) {
        if (size < 2) {
            throw new IllegalArgumentException("Board size must be at least 2.");
        }
        this.size = size;
        this.map = new int[size][size];
        this.temp = new int[size][size];
        this.random = new Random();
    }
    
    public int getSize() {
        return size;
    }

    public int getCell(int row, int col) {
        return map[row][col];
    }

    public int getMaxTile() {
        int max = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                max = Math.max(max, map[i][j]);
            }
        }
        return max;
    }
    
    public void addRandomTile() {
    	int emptyCount = 0;
    	for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (map[i][j] == 0) {
                    emptyCount++;
                }
            }
        }
    	
    	if (emptyCount == 0) {
    		return;
    	}
    	
    	int target = random.nextInt(emptyCount);
    	for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (map[i][j] == 0) {
                    if (target == 0) {
                        map[i][j] = random.nextDouble() < 0.9 ? 2 : 4;
                        return;
                    }
                    target--;
                }
            }
        }
    }
    
    public boolean move(int dir) {
        int[][] before = copyMap();
        push(dir);
        merge(dir);
        push(dir);
        return !isSame(before, map);
    }
    
    public boolean canMoveAnyDirection() {
        if (hasEmptyCell()) {
            return true;
        }

        for (int d = 0; d < 4; d++) {
            int[][] backup = copyMap();
            boolean changed = move(d);
            restoreFrom(backup);
            if (changed) {
                return true;
            }
        }
        return false;
    }
    
    private boolean hasEmptyCell() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (map[i][j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void clearTemp() {
        for (int i = 0; i < size; i++) {
            Arrays.fill(temp[i], 0);
        }
    }

    private void deepCopyMap() {
        for (int i = 0; i < size; i++) {
            map[i] = temp[i].clone();
        }
    }
    
    private void push(int dir) {
        clearTemp();

        if (dir == RIGHT) {
            for (int i = 0; i < size; i++) {
                int endOfArray = size - 1;
                for (int j = size - 1; j >= 0; j--) {
                    if (map[i][j] != 0) {
                        temp[i][endOfArray--] = map[i][j];
                    }
                }
            }
            deepCopyMap();
        } else if (dir == LEFT) {
            for (int i = 0; i < size; i++) {
                int endOfArray = 0;
                for (int j = 0; j < size; j++) {
                    if (map[i][j] != 0) {
                        temp[i][endOfArray++] = map[i][j];
                    }
                }
            }
            deepCopyMap();
        } else if (dir == UP) {
            for (int i = 0; i < size; i++) {
                int endOfArray = 0;
                for (int j = 0; j < size; j++) {
                    if (map[j][i] != 0) {
                        temp[endOfArray++][i] = map[j][i];
                    }
                }
            }
            deepCopyMap();
        } else if (dir == DOWN) {
            for (int i = 0; i < size; i++) {
                int endOfArray = size - 1;
                for (int j = size - 1; j >= 0; j--) {
                    if (map[j][i] != 0) {
                        temp[endOfArray--][i] = map[j][i];
                    }
                }
            }
            deepCopyMap();
        } else {
            throw new IllegalArgumentException("Invalid direction: " + dir);
        }
    }

    private void merge(int dir) {
        if (dir == RIGHT) {
            for (int i = 0; i < size; i++) {
                for (int j = size - 1; j >= 1; j--) {
                    if (map[i][j] != 0 && map[i][j] == map[i][j - 1]) {
                        map[i][j] *= 2;
                        map[i][j - 1] = 0;
                    }
                }
            }
        } else if (dir == LEFT) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size - 1; j++) {
                    if (map[i][j] != 0 && map[i][j] == map[i][j + 1]) {
                        map[i][j] *= 2;
                        map[i][j + 1] = 0;
                    }
                }
            }
        } else if (dir == UP) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size - 1; j++) {
                    if (map[j][i] != 0 && map[j][i] == map[j + 1][i]) {
                        map[j][i] *= 2;
                        map[j + 1][i] = 0;
                    }
                }
            }
        } else if (dir == DOWN) {
            for (int i = 0; i < size; i++) {
                for (int j = size - 1; j >= 1; j--) {
                    if (map[j][i] != 0 && map[j][i] == map[j - 1][i]) {
                        map[j][i] *= 2;
                        map[j - 1][i] = 0;
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid direction: " + dir);
        }
    }
    
    private int[][] copyMap() {
        int[][] copied = new int[size][size];
        for (int i = 0; i < size; i++) {
            copied[i] = map[i].clone();
        }
        return copied;
    }

    private void restoreFrom(int[][] backup) {
        for (int i = 0; i < size; i++) {
            map[i] = backup[i].clone();
        }
    }

    private boolean isSame(int[][] a, int[][] b) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (a[i][j] != b[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
