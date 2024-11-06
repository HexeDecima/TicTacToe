import java.awt.*; // Library for painting graphics and images
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI; // Adding this because the regular setBackground doesn't work on Mac

public class TicTacToe {
    int boardWidth = 600;
    int boardHeight = 600; // Extra 50px for the text panel on top
    int topPanelHeight = 50;
    int topPanelTextSize = 30;

    JFrame frame = new JFrame("Tic-Tac-Toe");
    JLabel textLabel = new JLabel();
    JPanel topPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    // Adding an array of buttons for the fields
    JButton[][] board = new JButton[3][3];
    JButton newGameButton = new JButton();
    String playerX = "X";
    String playerO = "O";
    String currentPlayer = playerX;

    // Game logic
    boolean gameOver = false;
    int turns = 0;

    TicTacToe() {
        // Creating the main frame
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight + topPanelHeight);
        frame.setLocationRelativeTo(null); // If null, the window will be placed in the center
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Setting the label
        textLabel.setBackground(Color.darkGray);
        textLabel.setForeground(Color.white);
        textLabel.setFont(new Font("Arial", Font.BOLD, topPanelTextSize));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Tic-Tac-Toe");
        textLabel.setOpaque(true);

        // Creating the New Game button
        newGameButton.setUI(new MetalButtonUI()); // For Mac OS only
        newGameButton.setBackground(Color.darkGray);
        newGameButton.setForeground(Color.white);
        newGameButton.setBorder(BorderFactory.createLineBorder(Color.white)); // MetalButton doesn't have borders
        newGameButton.setFont(new Font("Arial", Font.BOLD, 30));
        newGameButton.setText("New Game");
        newGameButton.setFocusable(false);

        // Adding action listener for the New Game button
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row, col;
                if (gameOver) {
                    for (row = 0; row < 3; row++) {
                        for (col = 0; col < 3; col++) {
                            resetBoard(board[row][col]);
                        }
                    }
                    gameOver = false;
                    turns = 0;
                }
                JButton newGameButton = (JButton) e.getSource(); // Explicitly casting JButton type to prevent errors
            }
        });

        // Setting up the top panel
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(Color.darkGray);
        topPanel.add(newGameButton, BorderLayout.WEST);
        topPanel.add(textLabel, BorderLayout.EAST);
        frame.add(topPanel, BorderLayout.NORTH);

        // Creating the board panel
        boardPanel.setLayout(new GridLayout(3, 3));
        boardPanel.setBackground(Color.darkGray);
        frame.add(boardPanel); // If added BorderLayout.SOUTH, the panel disappears

        // Creating the buttons
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                JButton tile = new JButton();
                board[row][col] = tile; // Adding the buttons to the JButton array
                boardPanel.add(tile);

                // Styling the tiles
                tile.setUI(new MetalButtonUI()); // For Mac OS only
                tile.setBackground(Color.darkGray);
                tile.setForeground(Color.white);
                tile.setBorder(BorderFactory.createLineBorder(Color.white)); // Because MetalButton doesn't have borders
                tile.setFont(new Font("Arial", Font.BOLD, 120));
                tile.setFocusable(false);

                // Adding action listener
                tile.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (gameOver) return;
                        JButton tile = (JButton) e.getSource(); // Explicitly casting JButton type to prevent errors
                        if (tile.getText() == "") {
                            tile.setText(currentPlayer);
                            turns++;
                            checkWinner();
                            if (!gameOver) {
                                // Switching players
                                currentPlayer = currentPlayer == playerX ? playerO : playerX;
                                //            = currentPlayer if (?) = playerX, else if = player0, then (:) = playerX

                                // Changing the label accordingly
                                textLabel.setText(currentPlayer + "'s turn.");
                            }


                        }
                    }
                });
            }
        }

    }
    void checkWinner() {
        // Horizontal
        int row;
        for (row = 0; row < 3; row++) {
            if (board[row][0].getText() == "") continue; // If empty, no reason to check further
            if (board[row][0].getText() == board[row][1].getText() &&
                board[row][1].getText() == board[row][2].getText()) {
                for (int i = 0; i < 3; i++) {
                    setWinner(board[row][i]);
                }
                gameOver = true;
                return;
            }
        }

        // Vertical
        for (int col = 0; col < 3; col++) {
            if (board[col][0].getText() == "") continue; // If empty, no reason to check further
            if (board[col][0].getText() == board[col][1].getText() &&
                board[col][1].getText() == board[col][2].getText()) {
                for (int i = 0; i < 3; i++) {
                    setWinner(board[i][col]);
                }
                gameOver = true;
                return;
            }
        }

        // Diagonal \
        if (board[0][0].getText() == board[1][1].getText() &&
            board[1][1].getText() == board[2][2].getText() &&
            board[0][0].getText() != "") {
            for (int i = 0; i < 3; i++) { // Setting to match the diagonal tiles
                setWinner(board[i][i]);
            }
            gameOver = true;
            return;
        }

        // Diagonal /
        if (board[0][2].getText() == board[1][1].getText() &&
            board[1][1].getText() == board[2][0].getText() &&
            board[0][2].getText() != "") {
            setWinner(board[0][2]);
            setWinner(board[1][1]);
            setWinner(board[2][0]);
            gameOver = true;
            return;
        }
        if (turns == 9) {
            for (row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    setTie(board[row][col]);
                }
            }
            gameOver = true;
        }
    }

    void setWinner(JButton tile) {
        tile.setForeground(Color.green);
        tile.setBackground(Color.gray);
        textLabel.setText(currentPlayer + " wins!");
    }

    void setTie(JButton tile) {
        tile.setForeground(Color.yellow);
        tile.setBackground(Color.gray);
        textLabel.setText("Tie!");
    }

    void resetBoard(JButton tile) {
        tile.setBackground(Color.darkGray);
        tile.setForeground(Color.white);
        tile.setText("");
    }
}
