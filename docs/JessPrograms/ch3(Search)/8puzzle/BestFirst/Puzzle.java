package BestFirst;

/** Copyright ¢ç 2007  KyungWon AILab in korea  
 * All right reserved. 
 */

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.util.Random;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javax.swing.WindowConstants;

import java.util.Vector;

/**
 * This code was generated using CloudGarden's Jigloo SWT/Swing GUI Builder,
 * which is free for non-commercial use. If Jigloo is being used commercially
 * (ie, by a corporation, company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo. Please visit
 * www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. ************************************* A COMMERCIAL LICENSE
 * HAS NOT BEEN PURCHASED for this machine, so Jigloo or this code cannot be
 * used legally for any corporate or commercial purpose.
 * *************************************
 */
public class Puzzle extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel jPanel1;
	
	private JLabel jLabel1;

	private JButton jButtonInit;

	private JButton jButtonSolve;

	private JButton jButtonRand;

	private JButton jButtonShow;

	private JButton jButtonA9;

	private JButton jButtonA8;

	private JButton jButtonA7;

	private JButton jButtonA6;

	private JButton jButtonA5;

	private JButton jButtonA4;

	private JButton jButtonA3;

	private JButton jButtonA2;

	private JButton jButtonA1;

	private JTextField jPath;

	private JTextField jEcho;

	private JTextField jInPut;

	private int currentStat[] = new int[9];

	private String strSearchKind = "BestFirst";

	// initilize state
	private static int goalStat[] = { 1, 2, 3, 4, 5, 6, 7, 8, 0 };

	// expanding state
	private int search[] = new int[9];

	// show step processing state
	private int showarr[] = new int[9];
    // expanding temp state 
	private int tmpStat[] = new int[9];

	// Four directions
	private final static int LEFT = 0;

	private final static int UP = 1;

	private final static int RIGHT = 2;

	private final static int DOWN = 3;

	// current Path
	private StringBuffer currentPath = new StringBuffer();

	// show step processing Path
	private StringBuffer showPath = new StringBuffer();

	// Best First Search open list
	private static Vector open = new Vector();

	// Best First Search close list
	private static Vector close = new Vector();

	// node Number initialization
	private static int order = 1;

	private static PuzzleNode puzzleNode = null;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		Puzzle inst = new Puzzle();
		inst.setVisible(true);
	}

	public Puzzle() {
		super();
		initGUI();
		init(currentStat);
	}

	// GUI image initialization
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				JMenuBar jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				jMenuBar1.setPreferredSize(new java.awt.Dimension(392, 26));
				{
					JMenu jMenu1 = new JMenu();
					jMenuBar1.add(jMenu1);
					jMenu1.setText("File");
					{
						JMenuItem jMenuItem1 = new JMenuItem();
						jMenu1.add(jMenuItem1);
						jMenuItem1.setText("Best First Search");
						jMenuItem1.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jMenuItem1ActionPerformed(evt);
							}
						});
					}
					{
						JMenuItem jMenuItem2 = new JMenuItem();
						jMenu1.add(jMenuItem2);
						jMenuItem2.setText("Breadth First Search");
						jMenuItem2.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jMenuItem2ActionPerformed(evt);
							}
						});
					}
					{
						JMenuItem jMenuItem3 = new JMenuItem();
						jMenu1.add(jMenuItem3);
						jMenuItem3.setText("Depth First Search");
						jMenuItem3.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jMenuItem3ActionPerformed(evt);
							}
						});
					}
				}

				jPanel1 = new JPanel();

				this.getContentPane().add(jPanel1, BorderLayout.NORTH);
				jPanel1.setPreferredSize(new java.awt.Dimension(392, 267));
				jPanel1.setLayout(null);

				{
					jButtonA1 = new JButton();
					jPanel1.add(jButtonA1);
					jButtonA1.setText("1");
					jButtonA1.setBounds(0, 0, 70, 70);
					jButtonA1.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							clickMove(0, currentStat);
						}
					});

				}
				{
					jButtonA2 = new JButton();
					jPanel1.add(jButtonA2);
					jButtonA2.setText("2");
					jButtonA2.setBounds(70, 0, 70, 70);
					jButtonA2.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							clickMove(1, currentStat);
						}
					});

				}
				{
					jButtonA3 = new JButton();
					jPanel1.add(jButtonA3);
					jButtonA3.setText("3");
					jButtonA3.setBounds(140, 0, 70, 70);
					jButtonA3.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							clickMove(2, currentStat);
						}
					});

				}
				{
					jButtonA4 = new JButton();
					jPanel1.add(jButtonA4);
					jButtonA4.setText("4");
					jButtonA4.setBounds(0, 70, 70, 70);
					jButtonA4.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							clickMove(3, currentStat);

						}
					});

				}
				{
					jButtonA5 = new JButton();
					jPanel1.add(jButtonA5);
					jButtonA5.setText("5");
					jButtonA5.setBounds(70, 70, 70, 70);
					jButtonA5.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							clickMove(4, currentStat);
						}
					});

				}
				{
					jButtonA6 = new JButton();
					jPanel1.add(jButtonA6);
					jButtonA6.setText("6");
					jButtonA6.setBounds(140, 70, 70, 70);
					jButtonA6.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							clickMove(5, currentStat);
						}
					});

				}
				{
					jButtonA7 = new JButton();
					jPanel1.add(jButtonA7);
					jButtonA7.setText("7");
					jButtonA7.setBounds(0, 140, 70, 70);
					jButtonA7.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							clickMove(6, currentStat);
						}
					});

				}
				{
					jButtonA8 = new JButton();
					jPanel1.add(jButtonA8);
					jButtonA8.setText("8");
					jButtonA8.setBounds(70, 140, 70, 70);
					jButtonA8.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							clickMove(7, currentStat);
						}
					});

				}
				{
					jButtonA9 = new JButton();
					jPanel1.add(jButtonA9);
					jButtonA9.setBounds(140, 140, 70, 70);
					jButtonA9.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							clickMove(8, currentStat);
						}
					});

				}
				{
					jButtonRand = new JButton();
					jPanel1.add(jButtonRand);
					jButtonRand.setText("Randomize");
					jButtonRand.setBounds(244, 54, 127, 25);
					jButtonRand.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							Randomize();
							update();
						}
					});
				}
				{
					jButtonSolve = new JButton();
					jPanel1.add(jButtonSolve);
					jButtonSolve.setText("Solve");
					jButtonSolve.setBounds(244, 98, 127, 25);
					jButtonSolve.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							if (strSearchKind == "BestFirst")
								BestFirst();
							if (strSearchKind == "BreadthFirst")
								BreadthFirst();
							if (strSearchKind == "DepthFirst")
								DepthFirst();
						}
					});
				}
				{
					jButtonInit = new JButton();
					jPanel1.add(jButtonInit);
					jButtonInit.setText("Initilize");
					jButtonInit.setBounds(244, 140, 127, 25);
					jButtonInit.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							init(currentStat);
							update();
						}
					});
				}
				{
					jButtonShow = new JButton();
					jPanel1.add(jButtonShow);
					jButtonShow.setText("showStep");
					jButtonShow.setBounds(244, 180, 127, 25);
					jButtonShow.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							showStep();
						}
					});
				}
				{
					jEcho = new JTextField();
					jPanel1.add(jEcho);
					jEcho.setBounds(0, 240, 393, 30);
					jEcho.setEditable(false);
				}
				{
					jPath = new JTextField();
					jPanel1.add(jPath);
					jPath.setBounds(0, 210, 393, 30);
					jPath.setEditable(false);
				}
				{
					jInPut = new JTextField();
					jPanel1.add(jInPut);
					jInPut.setBounds(309, 16, 60, 30);
					jInPut.setVisible(false);
				}
				{
					jLabel1 = new JLabel();
					jPanel1.add(jLabel1);
					jLabel1.setText("input layer");
					jLabel1.setBounds(240, 15, 66, 30);
					jLabel1.setVisible(false);
				}
			}
			pack();
			setSize(400, 300);
			setBounds(300, 150, 400, 330);
			setTitle("BestFirst");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jMenuItem1ActionPerformed(ActionEvent evt) {
		this.strSearchKind = "BestFirst";
		setTitle("BestFirst");
		this.jInPut.setVisible(false);
		this.jLabel1.setVisible(false);
	}

	private void jMenuItem2ActionPerformed(ActionEvent evt) {
		this.strSearchKind = "BreadthFirst";
		setTitle("BreadthFirst");
		this.jInPut.setVisible(true);
		this.jLabel1.setVisible(true);
	}

	private void jMenuItem3ActionPerformed(ActionEvent evt) {
		this.strSearchKind = "DepthFirst";
		setTitle("DepthFirst");
		this.jInPut.setVisible(true);
		this.jLabel1.setVisible(true);
	}

	// Best First Search
	public void BestFirst() {

		// initial current path
		currentPath.delete(0, currentPath.length());
		//int currentStat[]={0,2,1,5,3,6,4,7,8};
		// initial state setting in close vector
		puzzleNode = new PuzzleNode(currentStat);
		close.add(puzzleNode);

		// If now already initial state, return
		if (arrCheck(currentStat, goalStat)) {
			jEcho.setText("Already Goal!");
			return;
		}
		// Backup start state
		System.arraycopy(currentStat, 0, search, 0, 9);

		// expanding search, if the search state equal goal state, stop
		while (!arrCheck(search, goalStat)) {
			// If move left possible and the close list isn't exist
			if (searchLeft(search, tmpStat) && checkClose(close, tmpStat)) {
				puzzleNode = new PuzzleNode(currentPath.append('L'),
						heuristValue(tmpStat), order++, tmpStat);
				vecSort();
				currentPath.deleteCharAt(currentPath.length() - 1);
			}
			// If move up possible and the close list isn't exist
			if (searchUp(search, tmpStat) && checkClose(close, tmpStat)) {
				puzzleNode = new PuzzleNode(currentPath.append('U'),
						heuristValue(tmpStat), order++, tmpStat);
				vecSort();
				currentPath.deleteCharAt(currentPath.length() - 1);
			}
			// If move right possible and the close list isn't exist
			if (searchRight(search, tmpStat) && checkClose(close, tmpStat)) {
				puzzleNode = new PuzzleNode(currentPath.append('R'),
						heuristValue(tmpStat), order++, tmpStat);
				vecSort();
				currentPath.deleteCharAt(currentPath.length() - 1);
			}
			// If move down possible and the close list isn't exist
			if (searchDown(search, tmpStat) && checkClose(close, tmpStat)) {
				puzzleNode = new PuzzleNode(currentPath.append('D'),
						heuristValue(tmpStat), order++, tmpStat);
				vecSort();
				currentPath.deleteCharAt(currentPath.length() - 1);
			}
			// add the list first node which in the open into the close list
			close.add(open.elementAt(0));
			// Set the current status and path to be a new search node
			System.arraycopy(((PuzzleNode) open.elementAt(0)).openTmpStat, 0,
					search, 0, 9);
			currentPath.delete(0, currentPath.length());
			currentPath = currentPath
					.append(((PuzzleNode) open.elementAt(0)).path);
			open.remove(0);
		}
		// show search result in the jPath and jEcho
		System.arraycopy(currentStat, 0, showarr, 0, 9);
		jEcho.setText("Now solved!");
		jPath.setText(String.valueOf(currentPath));
		System.out.println("ok!");
		// delete open and close list
		open.clear();
		close.clear();
	}

	public void BreadthFirst() {

		// initial current path
		currentPath.delete(0, currentPath.length());
		
		puzzleNode = new PuzzleNode(currentStat);

		// If now already initial status, return
		if (arrCheck(currentStat, goalStat)) {
			jEcho.setText("Already Goal!");
			return;
		}
		// Backup start status
		System.arraycopy(currentStat, 0, search, 0, 9);

		while (currentPath.length() <= Integer.parseInt(this.jInPut.getText())) {
			// If move left possible and check the last step whether go right
			if (searchLeft(search, tmpStat) && checkBack_R()) {
				puzzleNode = new PuzzleNode(currentPath.append('L'),
						currentPath.length(), order++, tmpStat);
				open.add(puzzleNode);
				currentPath.deleteCharAt(currentPath.length() - 1);
			}
			// If move up possible and check the last step whether go down
			if (searchUp(search, tmpStat) && checkBack_D()) {
				puzzleNode = new PuzzleNode(currentPath.append('U'),
						currentPath.length(), order++, tmpStat);
				open.add(puzzleNode);
				currentPath.deleteCharAt(currentPath.length() - 1);
			}

			// If move right possible and check the last step whether go left
			if (searchRight(search, tmpStat) && checkBack_L()) {
				puzzleNode = new PuzzleNode(currentPath.append('R'),
						currentPath.length(), order++, tmpStat);
				open.add(puzzleNode);
				currentPath.deleteCharAt(currentPath.length() - 1);
			}

			// If move down possible and check the last step whether go up
			if (searchDown(search, tmpStat) && checkBack_U()) {
				puzzleNode = new PuzzleNode(currentPath.append('D'),
						currentPath.length(), order++, tmpStat);
				open.add(puzzleNode);
				currentPath.deleteCharAt(currentPath.length() - 1);
			}
			// if the search state equal goal state ,break
			if (arrCheck(((PuzzleNode) open.elementAt(0)).openTmpStat, goalStat))
				break;
			else {
				System.arraycopy(((PuzzleNode) open.elementAt(0)).openTmpStat,
						0, search, 0, 9);
				currentPath.delete(0, currentPath.length());
				currentPath = currentPath.append(((PuzzleNode) open
						.elementAt(0)).path);
				open.remove(0);
			}
		}

		currentPath.delete(0, currentPath.length());
		currentPath = currentPath.append(((PuzzleNode) open.elementAt(0)).path);
		// if current node layour more than input layour
		if (currentPath.length() > Integer.parseInt(this.jInPut.getText())) {
			jEcho.setText("goal node out of "
					+ Integer.parseInt(this.jInPut.getText()) + "layer");
			jPath.setText("");
		} else {
			System.arraycopy(currentStat, 0, showarr, 0, 9);
			jEcho.setText("Now solved!");
			jPath.setText(String.valueOf(currentPath));
			System.out.println("ok!");
			open.clear();

		}
	}

	public void DepthFirst() {
		
		// initial used variable
		currentPath.delete(0, currentPath.length());
		puzzleNode = new PuzzleNode(currentStat);

		// If now already initial state, return
		if (arrCheck(currentStat, goalStat)) {
			jEcho.setText("Already Goal!");
			return;
		}
		// Backup start state
		System.arraycopy(currentStat, 0, search, 0, 9);
		// expanding search, if search state equal goal state, stop
		while (!arrCheck(search, goalStat)) {
			// If move down possible ,check current node depth layour ,
			// check the last step whether go up
			if (searchDown(search, tmpStat) && checkDepth() && checkBack_U()) {
				puzzleNode = new PuzzleNode(currentPath.append('D'), tmpStat,
						order++, currentPath.length());
				open.add(0, puzzleNode);
				currentPath.deleteCharAt(currentPath.length() - 1);
			}
			// If move right possible ,check current node depth layour ,
			// check the last step whether go left
			if (searchRight(search, tmpStat) && checkDepth() && checkBack_L()) {
				puzzleNode = new PuzzleNode(currentPath.append('R'), tmpStat,
						order++, currentPath.length());
				open.add(0, puzzleNode);
				currentPath.deleteCharAt(currentPath.length() - 1);
			}
			// If move up possible ,check current node depth layour ,
			// check the last step whether go down
			if (searchUp(search, tmpStat) && checkDepth() && checkBack_D()) {
				puzzleNode = new PuzzleNode(currentPath.append('U'), tmpStat,
						order++, currentPath.length());
				open.add(0, puzzleNode);
				currentPath.deleteCharAt(currentPath.length() - 1);
			}
			// If move left possible ,check current node depth layour ,
			// check the last step whether go right
			if (searchLeft(search, tmpStat) && checkDepth() && checkBack_R()) {
				puzzleNode = new PuzzleNode(currentPath.append('L'), tmpStat,
						order++, currentPath.length());
				open.add(0, puzzleNode);
				currentPath.deleteCharAt(currentPath.length() - 1);
			}
			// if current node depth layour more than input layour or open list
			// is empty
			if ((!checkDepth() || (((PuzzleNode) open.elementAt(0)).layerNum > Integer
					.parseInt(this.jInPut.getText())))
					&& (open.size() == 0)) {
				jEcho.setText("goal node out of "
						+ Integer.parseInt(this.jInPut.getText()) + " layer");
				jPath.setText("");
				return;
			}
			else {
				// if search state equal goal state ,break
				if (arrCheck(((PuzzleNode) open.elementAt(0)).openTmpStat,
						goalStat))
					break;
				else {
					// Set the current status and path to be a new search node
					System.arraycopy(
							((PuzzleNode) open.elementAt(0)).openTmpStat, 0,
							search, 0, 9);
					currentPath.delete(0, currentPath.length());
					currentPath = currentPath.append(((PuzzleNode) open
							.elementAt(0)).path);
					open.remove(0);
				}
			}
		}
		// Set the current pathe and state to be a new search node
		currentPath.delete(0, currentPath.length());
		currentPath = currentPath.append(((PuzzleNode) open.elementAt(0)).path);
		System.arraycopy(currentStat, 0, showarr, 0, 9);
		jEcho.setText("Now solved!");
		jPath.setText(String.valueOf(currentPath));
		System.out.println("ok!");
		open.clear();
	}

	// puzzleNodes sort in the open Vector
	public static void vecSort() {
		if (open.size() == 0) {
			open.add(puzzleNode);
		} else {
			for (int j = 0; j < open.size(); j++) {
				if (puzzleNode.distance < ((PuzzleNode) open.elementAt(j)).distance) {
					open.add(j, puzzleNode);
					break;
				}
			}
			if (puzzleNode.distance >= ((PuzzleNode) open.elementAt(open.size() - 1)).distance)
				open.add(puzzleNode);
		}

//		for (int k = 0; k < open.size(); k++) {
//			for (int l = 0; l < open.size() - 1; l++) {
//				if ((((PuzzleNode) open.elementAt(l)).distance == ((PuzzleNode) open
//						.elementAt(l + 1)).distance)
//						&& (((PuzzleNode) open.elementAt(l)).nodeNum > ((PuzzleNode) open
//								.elementAt(l + 1)).nodeNum)) {
//					PuzzleNode pNode = (PuzzleNode) open.elementAt(l);
//					open.remove(l);
//					open.add(l + 1, pNode);
//				}
//			}
//		}

	}

	// If expand left possible it's ture otherwise false
	public boolean searchLeft(int search[], int tmpStat[]) {
		if (directionCheck(zeroSearch(search), LEFT) == 0) {
			System.arraycopy(search, 0, tmpStat, 0, 9);
			swap(zeroSearch(search), LEFT, tmpStat);
			return true;
		} else
			return false;
	}

	// If expand right possible it's ture otherwise false
	public boolean searchRight(int search[], int tmpStat[]) {
		if (directionCheck(zeroSearch(search), RIGHT) == 0) {
			System.arraycopy(search, 0, tmpStat, 0, 9);
			swap(zeroSearch(search), RIGHT, tmpStat);
			return true;
		} else
			return false;
	}

	// If expand up possible it's ture otherwise false
	public boolean searchUp(int search[], int tmpStat[]) {
		if (directionCheck(zeroSearch(search), UP) == 0) {
			System.arraycopy(search, 0, tmpStat, 0, 9);
			swap(zeroSearch(search), UP, tmpStat);
			return true;
		} else
			return false;
	}

	// If expand down possible it's ture otherwise false
	public boolean searchDown(int search[], int tmpStat[]) {
		if (directionCheck(zeroSearch(search), DOWN) == 0) {
			System.arraycopy(search, 0, tmpStat, 0, 9);
			swap(zeroSearch(search), DOWN, tmpStat);
			return true;
		} else
			return false;
	}

	// check the node in close list exist or nonexist
	public boolean checkClose(Vector vec, int a[]) {
		if (currentPath.length() == 0) {
			return true;
		}
		for (int i = 0; i < vec.size(); i++) {
			if (arrChangeStr(a).equals(
					arrChangeStr(((PuzzleNode) vec.elementAt(i)).openTmpStat)))
				return false;
		}
		return true;
	}

	// int variable change to string variable
	public static String intToString(int a) {
		if (a == 0)
			return "";
		return String.valueOf(a);
	}

	// change button status
	public void update() {
		jButtonA1.setText(intToString(currentStat[0]));
		jButtonA2.setText(intToString(currentStat[1]));
		jButtonA3.setText(intToString(currentStat[2]));
		jButtonA4.setText(intToString(currentStat[3]));
		jButtonA5.setText(intToString(currentStat[4]));
		jButtonA6.setText(intToString(currentStat[5]));
		jButtonA7.setText(intToString(currentStat[6]));
		jButtonA8.setText(intToString(currentStat[7]));
		jButtonA9.setText(intToString(currentStat[8]));
	}

	// Limited randomize, empty block moves only
	public void Randomize() {
		// init(currentStat);
		System.arraycopy(goalStat, 0, currentStat, 0, 9);
		// find the empty block
		int position = zeroSearch(currentStat);
		int direction;
		Random rand = new Random();
		// direction = 0 (Left), 1 (Up),2(Right), 3(Down)
		for (int i = 0; i < 30; i++) {
			direction = rand.nextInt(4);
			position = swap(position, directionAdjust(position, direction),
					currentStat);
		}
		currentPath.delete(0, currentPath.length());
		jEcho.setText("Now Randomized!");
		jPath.setText("");
	}

	// return of index of specific number in a array
	// Get the index of specific number in the array
	public static int getLocation(int i, int a[]) {
		if (i > 8 || i < 0)
			return 0;
		int j = 0;
		st: for (; j < a.length; j++)
			if (a[j] == i) {
				break st;
			}
		return j;
	}

	// return of index 0 in a array
	// Get the index of 0 element in the array
	public static int zeroSearch(int a[]) {
		return getLocation(0, a);
	}

	// For initial all blocks
	// Make current status as initial
	public void init(int a[]) {
		System.arraycopy(goalStat, 0, a, 0, 9);
		currentPath.delete(0, currentPath.length());
		jEcho.setText("Now initialized!");
		jPath.setText("");
	}

	// Check the moving possibility of a given direction
	public static int directionCheck(int position, int direction) {
		// return 1 -- need adjustment
		// return 0 -- no need for direction adjustment

		int adj = 0;
		switch (direction) { // check moving possible
		case LEFT:// Left possible? otherwise Right
			if (position % 3 == 0)
				adj = 1;
			break;
		case UP:// Up possible? otherwise Down
			if (position / 3 == 0)
				adj = 1;
			break;
		case RIGHT:// Right possible? otherwise Left
			if (position % 3 == 2)
				adj = 1;// can't right
			break;
		case DOWN:// Down possible? otherwise Up
			if (position / 3 == 2)
				adj = 1;// can't down
			break;
		default:

		}
		return adj;

	}

	// Adjust the direction to oppposite if needed
	public static int directionAdjust(int position, int direction) {
		if (directionCheck(position, direction) == 1)
			return (direction + 2) % 4;
		else
			return direction;
	}

	// Convert char to int for direction
	public static int directionConvert(char direction) {
		if (direction == 'L')
			return LEFT;
		if (direction == 'R')
			return RIGHT;
		if (direction == 'U')
			return UP;
		if (direction == 'D')
			return DOWN;
		else
			return 4;
	}

	// Swap two blocks, return new position
	public static int swap(int position, int direction, int arr[]) {
		int tmp = 0; // tmp for value swap between two blocks
		int newposition = 0; // new position for moving block
		switch (direction) {
		case LEFT:
			tmp = arr[position - 1];
			arr[position - 1] = arr[position];
			arr[position] = tmp;
			newposition = position - 1;
			break;
		case UP:
			tmp = arr[position - 3];
			arr[position - 3] = arr[position];
			arr[position] = tmp;
			newposition = position - 3;
			break;
		case RIGHT:
			tmp = arr[position + 1];
			arr[position + 1] = arr[position];
			arr[position] = tmp;
			newposition = position + 1;
			break;
		case DOWN:
			tmp = arr[position + 3];
			arr[position + 3] = arr[position];
			arr[position] = tmp;
			newposition = position + 3;
		}
		return newposition;
	}

	// Moving empty block if clicked
	public void clickMove(int position, int a[]) {
		switch (position - zeroSearch(a)) {
		case -1:// RIGHT
			if (directionCheck(position, RIGHT) == 0) {
				swap(position, RIGHT, a);
				update();
				jEcho.setText("Move RIGHT succussfully!");
				break;
			}
		case 1:// LEFT
			if (directionCheck(position, LEFT) == 0) {
				swap(position, LEFT, a);
				update();
				jEcho.setText("Move LEFT succussfully!");
				break;
			}
		case -3:// DOWN
			if (directionCheck(position, DOWN) == 0) {
				swap(position, DOWN, a);
				update();
				jEcho.setText("Move DOWN succussfully!");
				break;
			}
		case 3:// UP
			if (directionCheck(position, UP) == 0) {
				swap(position, UP, a);
				update();
				jEcho.setText("Move UP succussfully!");
				break;
			}
		default:
			jEcho.setText("This block can NOT be moved now!");
		}
	}

	// manhattan distance method
	public static int manhaDist(int src[]) {
		int total = 0;
		int tmp = 0;

		for (int i = 0; i < src.length; i++) {
			if (src[i] != 0) {
				tmp = getLocation(src[i], goalStat);
				int dx = i / 3 - tmp / 3;
				int dy = i % 3 - tmp % 3;
				if (dx < 0)
					dx = -dx;
				if (dy < 0)
					dy = -dy;
				total += (dx + dy);
			}
		}
		return total;
	}

	// int array change into string array
	public static String arrChangeStr(int a[]) {

		String str = "";
		int i = 0;
		for (; i < a.length; i++) {
			str += String.valueOf(a[i]);
		}
		return str;
	}

	// Check if two array are of the same size and value
	public boolean arrCheck(int src[], int destination[]) {

		if (src.length != destination.length)
			return false;
		for (int i = 0; i < src.length; i++) {
			if (src[i] != destination[i]) {
				return false;
			}
		}
		return true;
	}

	// Heurist Value compute method
	public int heuristValue(int arr[]) {
		int heuristValue = this.currentPath.length() + manhaDist(arr);
		return heuristValue;
	}

	// show step method
	public void showStep() {
		showPath = currentPath;
		if (showPath.length() == 0) {
			jEcho.setText("Already Goal");
			return;
		}
		char showChar;
		showChar = showPath.charAt(0);
		showPath.deleteCharAt(0);
		swap(zeroSearch(currentStat), directionConvert(showChar), showarr);
		System.arraycopy(showarr, 0, currentStat, 0, 9);
		update();
	}

	// check the last step whether go left
	public boolean checkBack_L() {
		if (currentPath.length() == 0
				|| currentPath.charAt(currentPath.length() - 1) != 'L')
			return true;
		else
			return false;
	}

	// check the last step whether go up
	public boolean checkBack_U() {
		if (currentPath.length() == 0
				|| currentPath.charAt(currentPath.length() - 1) != 'U')
			return true;
		else
			return false;
	}

	// check the last step whether go right
	public boolean checkBack_R() {
		if (currentPath.length() == 0
				|| currentPath.charAt(currentPath.length() - 1) != 'R')
			return true;
		else
			return false;
	}

	// check the last step whether go down
	public boolean checkBack_D() {
		if (currentPath.length() == 0
				|| currentPath.charAt(currentPath.length() - 1) != 'D')
			return true;
		else
			return false;
	}

	// check current node depth layour less than input layour
	public boolean checkDepth() {
		if (currentPath.length() + 1 <= Integer.parseInt(this.jInPut.getText()))
			return true;
		else
			return false;
	}

}