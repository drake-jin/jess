

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import jess.*;
import jess.awt.*;
import java.util.*;


public class WaterJug extends javax.swing.JFrame {
	private JPanel jPanel1;
	private JTextArea taTrace;
	private JScrollPane jScrollPane1;
	private JButton btnStep;
	private JButton btnRun;
	private JLabel lbl4;
	private JLabel lbl3;
	
	private Rete engine;	
	private ValueVector vecWaterState;
	private int index = 0;
	private int liter3 = 0;
	private int liter4 = 0;	
	private ImageIcon image3 = new ImageIcon(getClass().getClassLoader().getResource("images/0_3l.jpg"));
	private ImageIcon image4 = new ImageIcon(getClass().getClassLoader().getResource("images/0_4l.jpg"));

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		WaterJug inst = new WaterJug();
		inst.setVisible(true);
	}
	
	public WaterJug() {
		super();
		initGUI();
		
		this.lbl4.setSize(image3.getIconWidth(),image3.getIconHeight());
		this.lbl4.setIcon(image3);
		
		this.lbl3.setSize(image4.getIconWidth(),image4.getIconHeight());
		this.lbl3.setIcon(image4);
		
		//settingJess();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.getContentPane().setLayout(null);
			{
				jPanel1 = new JPanel();
				this.getContentPane().add(jPanel1);
				jPanel1.setBounds(36, 27, 259, 125);
				jPanel1.setBackground(new java.awt.Color(128,255,255));
				jPanel1.setLayout(null);
				{
					lbl3 = new JLabel();
					jPanel1.add(lbl3);
					lbl3.setText("3 L");
					lbl3.setBounds(20, 12, 99, 99);
					lbl3.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
					lbl3.setSize(100, 100);
				}
				{
					lbl4 = new JLabel();
					jPanel1.add(lbl4);
					lbl4.setText("4 L");
					lbl4.setBounds(136, 12, 104, 97);
					lbl4.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
					lbl4.setSize(100, 100);
				}
			}
			{
				btnRun = new JButton();
				this.getContentPane().add(btnRun);
				btnRun.setText("Run");
				btnRun.setBounds(85, 159, 67, 30);
				btnRun.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnRunActionPerformed(evt);
					}
				});
			}
			{
				btnStep = new JButton();
				this.getContentPane().add(btnStep);
				btnStep.setText("Step");
				btnStep.setBounds(174, 159, 68, 30);
				btnStep.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnStepActionPerformed(evt);
					}
				});
			}
			{
				jScrollPane1 = new JScrollPane();
				this.getContentPane().add(jScrollPane1);
				jScrollPane1.setBounds(41, 200, 255, 184);
				{
					taTrace = new JTextArea();
					jScrollPane1.setViewportView(taTrace);
				}
			}
			pack();
			this.setSize(350, 450);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void btnRunActionPerformed(ActionEvent evt) {
		try {
			ReadJessFile readJessFile = new ReadJessFile("wjSlotModTreeJava.clp");
			String strTemp = readJessFile.getJessFileContent();
			
			if (strTemp.substring(0, 1).equals("1")) {  // 0번째 첫 문자열 
				this.engine = new Rete();
				this.engine.executeCommand(strTemp.substring(1)); // 1번째부터 전체 문자열, 자바에서 Jess 실행 

				Value valWaterState = this.engine.fetch("waterState");
				
				this.vecWaterState = valWaterState.listValue(this.engine.getGlobalContext());
				
  			    for(int i=0; i<vecWaterState.size(); i++) 
					this.taTrace.append(vecWaterState.get(i)+"\n");				
			} else
				this.taTrace.append(strTemp.substring(1) + "\n");
			
		} catch (JessException ex) {
			this.taTrace.append(ex.toString() + "\n");
		}
	}
	
	private void btnStepActionPerformed(ActionEvent evt) {
		try {
			if(this.liter4 != 2) {
				String water = ((Value)vecWaterState.get(this.index++)).toString();
		
				if(water.equals("FULL4LJUG")) {
					this.liter4 = 4;
					ShowWaterJug();				
				}					
				if(water.equals("FULL3LJUG")) {
					this.liter3 = 3;
					ShowWaterJug();				
				} 
				if(water.equals("EMPTY4LJUG")) {
					this.liter4 = 0;
					ShowWaterJug();				
				} 
				if(water.equals("EMPTY3LJUG")) {
					this.liter3 = 0;
					ShowWaterJug();				
				}
				if(water.equals("POUR34FULL")) {
					this.liter3 = this.liter3 - (4 - this.liter4);
					this.liter4 = 4;
					ShowWaterJug();				
				}
				if(water.equals("POUR43FULL")) {
					this.liter4 = this.liter4 - (3 - this.liter3);
					this.liter3 = 3;
					ShowWaterJug();				
				} 
				if(water.equals("POUR34")) {
					this.liter4 = this.liter4 + this.liter3;
					this.liter3 = 0;
					ShowWaterJug();				
				}
				if(water.equals("POUR43")) {
					this.liter3 = this.liter4 + this.liter3;
					this.liter4 = 0;
					ShowWaterJug();				
				}
			}
		} catch (JessException ex) {
			this.taTrace.append(ex.toString() + "\n");
		}
	}
	
	private void ShowWaterJug() {
		this.image3 = new ImageIcon(getClass().getClassLoader().getResource("images/"+String.valueOf(this.liter3)+ "_3l.jpg"));
		this.image4 = new ImageIcon(getClass().getClassLoader().getResource("images/"+String.valueOf(this.liter4)+ "_4l.jpg"));
		this.lbl4.setSize(image3.getIconWidth(),image3.getIconHeight());
		this.lbl4.setIcon(image3);		
		this.lbl3.setSize(image4.getIconWidth(),image4.getIconHeight());
		this.lbl3.setIcon(image4);
	}

}
