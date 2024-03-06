package TP;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class TaskPerf10 extends JFrame implements ActionListener{
	static final long serialVersionUID = 1L;
	static int[][] diff = {{10, 10, 10, 100}, {16, 16, 40, 256}, {16, 30, 99, 480}, {0, 0, 0, 0}};
	static int dSel = 0;
	static int flagCount;
	static int victoryCounter = 0;
	button[][] btn;
	static boolean[][] bombGrid;
	static ArrayList<Boolean> bombList = new ArrayList<>();
	JPanel board = new JPanel();
	static URL[] icons = new URL[13];
	JMenuBar mb = new JMenuBar();
	JMenu mGame = new JMenu("Game");
	JMenu mDiff = new JMenu("Difficulty");
	JMenuItem easy = new JMenuItem("Beginner");
	JMenuItem medium = new JMenuItem("Intermediate");
	JMenuItem hard = new JMenuItem("Expert");
	JMenuItem custom = new JMenuItem("Custom...");
	JMenuItem exit = new JMenuItem("Exit");
	JMenuItem reset = new JMenuItem("Reset");
	JLabel bombCount = new JLabel();
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				new TaskPerf10();
			}
		});
	}
	TaskPerf10(){ //Constructor for JFrame
		super("Minesweeper");
		icons[0] = TaskPerf10.class.getResource("/kuuhaku.png");
		icons[1] = TaskPerf10.class.getResource("/no1.png");
		icons[2] = TaskPerf10.class.getResource("/no2.png");
		icons[3] = TaskPerf10.class.getResource("/no3.png");
		icons[4] = TaskPerf10.class.getResource("/no4.png");
		icons[5] = TaskPerf10.class.getResource("/no5.png");
		icons[6] = TaskPerf10.class.getResource("/no6.png");
		icons[7] = TaskPerf10.class.getResource("/no7.png");
		icons[8] = TaskPerf10.class.getResource("/no8.png");
		icons[9] = TaskPerf10.class.getResource("/mine.png");
		icons[10] = TaskPerf10.class.getResource("/redo.png");
		icons[11] = TaskPerf10.class.getResource("/cross.png");
		icons[12] = TaskPerf10.class.getResource("/flag.png");
		setJMenuBar(mb);
		mb.add(mGame);
		mb.add(mDiff);
		mGame.add(reset);
		mGame.add(exit);
		mDiff.add(new JMenuItem("Beginner"));
		mDiff.add(medium);
		mDiff.add(hard);
		mDiff.add(custom);
		reset.addActionListener(this);
		exit.addActionListener(this);
		easy.addActionListener(this);
		medium.addActionListener(this);
		hard.addActionListener(this);
		custom.addActionListener(this);
		initialize(0);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		bombCount.setAlignmentX(Component.CENTER_ALIGNMENT);
		bombCount.setFont(new Font("Impact", Font.PLAIN, 35));
		bombCount.setBorder(new EmptyBorder(10,0,0,0));
		LineBorder line = new LineBorder(Color.BLACK, 5);
		EmptyBorder empty = new EmptyBorder(10,10,10,10);
		board.setBorder(BorderFactory.createCompoundBorder(empty, line));
		add(bombCount);
		add(board);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
	}
	private void initialize(int x) { //Resets the board for a reset or new difficulty setting
		dSel = x;
		victoryCounter = 0;
		btn = new button[diff[dSel][0]][diff[dSel][1]];
		bombGrid = new boolean[diff[dSel][0]][diff[dSel][1]];
		for(Component component : board.getComponents()) {
			board.remove(component);
		}
		board.setEnabled(true);
		bombCount.setText(String.valueOf(diff[dSel][2]));
		flagCount = diff[dSel][2];
		bombList.clear();
		for(int i = 0; i < diff[dSel][3]; i++) {
			if(i < diff[dSel][2]) {
				bombList.add(true);
			} else {
				bombList.add(false);
			}
		}
		Collections.shuffle(bombList);
		int i = 0;
		for(int row = 0; row < diff[dSel][0]; row++) {
			for(int col = 0; col < diff[dSel][1]; col++) {
				bombGrid[row][col] = bombList.get(i);
				btn[row][col] = new button();
				btn[row][col].setBomb(bombList.get(i));
				btn[row][col].addActionListener(this);
				if(row % 2 == 0) {
					if(col % 2 == 0) {
						btn[row][col].setBackground(Color.GRAY);
						btn[row][col].setColor(Color.GRAY);
					} else {
						btn[row][col].setBackground(Color.DARK_GRAY);
						btn[row][col].setColor(Color.DARK_GRAY);
					}
				} else {
					if(col % 2 == 0) {
						btn[row][col].setBackground(Color.DARK_GRAY);
						btn[row][col].setColor(Color.DARK_GRAY);
					} else {
						btn[row][col].setBackground(Color.GRAY);
						btn[row][col].setColor(Color.GRAY);
					}
				}
				board.add(btn[row][col]);
				i++;
			}
		}
		board.setLayout(new GridLayout(diff[dSel][0], diff[dSel][1]));
	}
	private void checker(int r, int c) { //Checks the clicked cell, and the 8 surrounding cells
		int trapCount = 0;
		button cell = btn[r][c];
		cell.setOpen(true);
		cell.setEnabled(false);
		int[][] cell8 = {{r-1, c-1}, {r-1, c}, {r-1, c+1},
						{r, c-1}, {r, c+1},
						{r+1, c-1}, {r+1, c}, {r+1, c+1}};

		if(cell.hasBomb()) {
			bombReveal();
			board.setEnabled(false);
			endDialog('L');
		} else {
			victoryCounter++;
			if(victoryCounter == diff[dSel][3] - diff[dSel][2]) {
				endDialog('V');
				board.setEnabled(false);

			}
			setBG(cell);
			for(int i = 0; i < 8; i++) {
				try {
					if(bombGrid[cell8[i][0]][cell8[i][1]]) {
						trapCount++;
					}
				} catch(IndexOutOfBoundsException e) {}
			}
			placeIcon(btn[r][c], trapCount);
		}
		if(cell.getText() == "" && !cell.isNumbered() && !cell.hasBomb()) {
			for(int i = 0; i < 8; i++) {
				try {
					if(!btn[cell8[i][0]][cell8[i][1]].isOpen()) {
						checker(cell8[i][0], cell8[i][1]);
					}
				} catch(IndexOutOfBoundsException e) {}
			}
		}
	}
	private void bombReveal() { //reveals all bombs
		for(int r = 0; r < diff[dSel][0]; r++) {
			for(int c = 0; c < diff[dSel][1]; c++) {
				if(bombGrid[r][c] && !btn[r][c].isFlagged()) {
					btn[r][c].setOpen(true);
					btn[r][c].setEnabled(false);
					placeIcon(btn[r][c], 9);
					setBG(btn[r][c]);
				}
			}
		}
	}
	private void placeIcon(button btn, int iconNo) { //Places an icon on a numbered or bomb cell
		btn.setIcon(new ImageIcon(icons[iconNo]));
		btn.setDisabledIcon(new ImageIcon(icons[iconNo]));
		btn.setNumbered(iconNo != 0 ? true : false);
	}
	private void setBG(button cell) { //Changes cell color once it's opened
		if(cell.baseColor == Color.DARK_GRAY) {
			cell.setBackground(Color.decode("#dddddd"));
		} else if(cell.baseColor== Color.GRAY) {
			cell.setBackground(Color.decode("#eeeeeee"));
		}
	}
	private void endDialog(char trigger) { //Appears at the end of a game, win or lose
		for(Component component : board.getComponents()) {
			component.setEnabled(false);
		}
		JDialog endDialog = new JDialog();
		JLabel endMessage = new JLabel();
		if(trigger == 'V') {
			endMessage.setText("<html><center>You win!<br>Play again?</center></html>");
		} else if(trigger == 'L') {
			endMessage.setText("<html><center>You lost.<br>Try again?</center></html>");
		}
		endMessage.setFont(new Font("Arial", Font.BOLD, 13));
		JButton resetBtn = new JButton();
		resetBtn = new JButton(new ImageIcon(icons[10]));
		resetBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boardReset(dSel);
				endDialog.dispose();
			}
		});
		resetBtn.setBackground(Color.white);
		resetBtn.setOpaque(false);
		resetBtn.setBorder(new EmptyBorder(0,0,0,0));
		resetBtn.setPreferredSize(new Dimension(30,30));
		JButton crossBtn = new JButton();
		crossBtn = new JButton(new ImageIcon(icons[11]));
		crossBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				endDialog.dispose();
			}
		});
		crossBtn.setBackground(Color.white);
		crossBtn.setOpaque(false);
		crossBtn.setBorder(new EmptyBorder(0,0,0,0));
		crossBtn.setPreferredSize(new Dimension(30,30));
		endDialog.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5,5,5,5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		endDialog.add(endMessage, gbc);
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		endDialog.add(resetBtn, gbc);
		gbc.gridx = 1;
		endDialog.add(crossBtn, gbc);
		endDialog.setVisible(true);
		endDialog.pack();
		endDialog.setResizable(false);
		endDialog.setLocationRelativeTo(null);
		endDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	private void customDiff() { //Creates a JDialog to set custom settings
		setEnabled(false);
		JDialog customDialog = new JDialog();
		customDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		customDialog.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {}
			public void windowClosing(WindowEvent e) {
				setEnabled(true);
			}
			public void windowClosed(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
		});
		JLabel rows = new JLabel("Rows:");
		JLabel columns = new JLabel("Columns:");
		JLabel mines = new JLabel("Mines:");
		JTextField tfrows = new JTextField(8);
		JTextField tfcolumns = new JTextField(8);
		JTextField tfmines = new JTextField(8);
		JButton confirm = new JButton("Start custom game");
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					int row = Integer.parseInt(tfrows.getText());
					int col = Integer.parseInt(tfcolumns.getText());
					int bombs = Integer.parseInt(tfmines.getText());
					if(row <= 24 && col <= 30 && bombs <= 225) {
						diff[3][0] = row;
						diff[3][1] = col;
						diff[3][2] = bombs;
						diff[3][3] = row * col;
						boardReset(3);
						setEnabled(true);
						customDialog.dispose();
					} else {
						throw new NumberFormatException();
					}
				} catch(NumberFormatException ex) {
					tfrows.setText("");
					tfcolumns.setText("");
					tfmines.setText("");
					customDialog.setEnabled(false);
					JDialog warning = new JDialog();
					JLabel warnMessage = new JLabel("Please enter a valid number within range.");
					JLabel maxMessage = new JLabel("The maximum size is 24x30 with 225 mines.");
					JButton okaybtn = new JButton("OK");
					warning.setLayout(new BoxLayout(warning.getContentPane(), BoxLayout.Y_AXIS));
					warnMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
					maxMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
					okaybtn.setAlignmentX(Component.CENTER_ALIGNMENT);
					warning.add(warnMessage);
					warning.add(maxMessage);
					warning.add(okaybtn);
					warning.setVisible(true);
					warning.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
					warning.pack();
					warning.setResizable(false);
					warning.setLocationRelativeTo(null);
					okaybtn.setPreferredSize(new Dimension(30, 10));
					okaybtn.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							customDialog.setEnabled(true);
							warning.dispose();
						}
					});
				}
			}
		});
		customDialog.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5,5,5,5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		customDialog.add(rows, gbc);
		gbc.gridx = 1;
		customDialog.add(tfrows, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		customDialog.add(columns, gbc);
		gbc.gridx = 1;
		customDialog.add(tfcolumns, gbc);
		gbc.gridx = 0;
		gbc.gridy = 2;
		customDialog.add(mines, gbc);
		gbc.gridx = 1;
		customDialog.add(tfmines, gbc);
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		customDialog.add(confirm, gbc);
		customDialog.setVisible(true);
		customDialog.setResizable(false);
		customDialog.pack();
		customDialog.setLocationRelativeTo(null);
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == easy) {
			boardReset(0);
		} else if(e.getSource() == medium) {
			boardReset(1);
		} else if(e.getSource() == hard) {
			boardReset(2);
		} else if(e.getSource() == custom) {
			customDiff();
		} else if(e.getSource() == reset) {
			boardReset(dSel);
		} else if(e.getSource() == exit) {
			System.exit(0);
		}
	}
	private void boardReset(int x) { //Reinitializes and Refreshes the board
		initialize(x);
		revalidate();
		repaint();
		pack();
	}
	
	class button extends JButton implements MouseListener{
		static final long serialVersionUID = 1L;
		private boolean open = false;
		private boolean trap;
		private boolean warn;
		private boolean numbered = false;
		private Color baseColor;
		button(){
			setFocusPainted(false);
			addMouseListener(this);
			setPreferredSize(new Dimension(30,30));
			setBorder(new EmptyBorder(0,0,0,0));
		}
		public void setOpen(boolean bool) {
			open = bool;
		}
		public boolean isOpen() {
			return open;
		}
		public void setBomb(boolean bool) {
			trap = bool;
		}
		public boolean hasBomb() {
			return trap;
		}
		public void setNumbered(boolean bool) {
			numbered = bool;
		}
		public boolean isNumbered() {
			return numbered;
		}
		public boolean isFlagged() {
			return warn;
		}
		public void setColor(Color color) {
			baseColor = color;
		}
		public void mouseClicked(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {
			if(SwingUtilities.isLeftMouseButton(e) && !open && !warn && isEnabled()) {
				int row = 0;
				int col = 0;
				for(int r = 0; r < diff[dSel][0]; r++) {
					for(int c = 0; c < diff[dSel][1]; c++) {
						if(e.getSource() == btn[r][c]) {
							row = r;
							col = c;
							break;
						}
					}
				}
				checker(row, col);
			} else if(SwingUtilities.isRightMouseButton(e) && !open && board.isEnabled()) {
				if(isEnabled()) {
					setBackground(baseColor);
					setIcon(new ImageIcon(icons[12]));
					setDisabledIcon(new ImageIcon(icons[12]));
					setEnabled(false);
					warn = true;
					flagCount--;
					bombCount.setText(String.valueOf(flagCount));
				} else {
					setIcon(null);
					setEnabled(true);
					warn = false;
					flagCount++;
					bombCount.setText(String.valueOf(flagCount));
				}
			}
		}
		public void mouseReleased(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {
			if(!open && !warn && board.isEnabled()) {
				setBackground(Color.white);
			}
		}
		public void mouseExited(MouseEvent e) {
			if(!open) {
				setBackground(baseColor);
			}
		}
	}
}