import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FiveInARow extends JFrame {
    private static final int BOARD_SIZE = 15; // 棋盘大小
    private static final int CELL_SIZE = 40; // 每格大小（像素）
    private static final int MARGIN = 20;    // 棋盘边距
    private int[][] board = new int[BOARD_SIZE][BOARD_SIZE]; // 棋盘数据：0为空，1为黑，2为白
    private boolean isBlackTurn = true; // 黑棋先手

    public FiveInARow() {
        setTitle("五子棋");
        setSize(BOARD_SIZE * CELL_SIZE + MARGIN * 2, BOARD_SIZE * CELL_SIZE + MARGIN * 2);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // 添加鼠标监听器，处理下棋逻辑
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = (e.getX() - MARGIN + CELL_SIZE / 2) / CELL_SIZE;
                int y = (e.getY() - MARGIN + CELL_SIZE / 2) / CELL_SIZE;

                // 判断是否点击在有效范围内且未被占用
                if (x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE && board[x][y] == 0) {
                    board[x][y] = isBlackTurn ? 1 : 2; // 放置黑或白棋
                    repaint(); // 重绘棋盘

                    // 检查是否胜利
                    if (checkWin(x, y)) {
                        String winner = isBlackTurn ? "黑棋" : "白棋";
                        JOptionPane.showMessageDialog(null, winner + "获胜！");
                        resetBoard(); // 重置棋盘
                        return;
                    }
                    isBlackTurn = !isBlackTurn; // 切换玩家
                }
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);

        // 绘制棋盘
        for (int i = 0; i < BOARD_SIZE; i++) {
            g.drawLine(MARGIN, MARGIN + i * CELL_SIZE, MARGIN + (BOARD_SIZE - 1) * CELL_SIZE, MARGIN + i * CELL_SIZE);
            g.drawLine(MARGIN + i * CELL_SIZE, MARGIN, MARGIN + i * CELL_SIZE, MARGIN + (BOARD_SIZE - 1) * CELL_SIZE);
        }

        // 绘制棋子
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                if (board[x][y] == 1) { // 黑棋
                    g.setColor(Color.BLACK);
                    g.fillOval(MARGIN + x * CELL_SIZE - CELL_SIZE / 2, MARGIN + y * CELL_SIZE - CELL_SIZE / 2, CELL_SIZE, CELL_SIZE);
                } else if (board[x][y] == 2) { // 白棋
                    g.setColor(Color.WHITE);
                    g.fillOval(MARGIN + x * CELL_SIZE - CELL_SIZE / 2, MARGIN + y * CELL_SIZE - CELL_SIZE / 2, CELL_SIZE, CELL_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawOval(MARGIN + x * CELL_SIZE - CELL_SIZE / 2, MARGIN + y * CELL_SIZE - CELL_SIZE / 2, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    // 检查是否有玩家获胜
    private boolean checkWin(int x, int y) {
        int player = board[x][y];
        return checkDirection(x, y, player, 1, 0)  // 横向
                || checkDirection(x, y, player, 0, 1)  // 纵向
                || checkDirection(x, y, player, 1, 1)  // 主对角线
                || checkDirection(x, y, player, 1, -1); // 副对角线
    }

    // 检查某个方向是否连成五子
    private boolean checkDirection(int x, int y, int player, int dx, int dy) {
        int count = 1;

        // 向一个方向延伸
        for (int i = 1; i < 5; i++) {
            int nx = x + i * dx, ny = y + i * dy;
            if (nx >= 0 && ny >= 0 && nx < BOARD_SIZE && ny < BOARD_SIZE && board[nx][ny] == player) {
                count++;
            } else {
                break;
            }
        }

        // 向另一个方向延伸
        for (int i = 1; i < 5; i++) {
            int nx = x - i * dx, ny = y - i * dy;
            if (nx >= 0 && ny >= 0 && nx < BOARD_SIZE && ny < BOARD_SIZE && board[nx][ny] == player) {
                count++;
            } else {
                break;
            }
        }

        return count >= 5; // 五子连珠即胜利
    }

    // 重置棋盘
    private void resetBoard() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        isBlackTurn = true;
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FiveInARow game = new FiveInARow();
            game.setVisible(true);
        });
    }
}

