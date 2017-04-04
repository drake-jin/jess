

import javax.swing.JLabel;
import javax.swing.border.BevelBorder;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.ImageIcon;
import java.awt.*;

import jess.*;
import jess.awt.*;
import java.util.*;

public class Cannibal extends javax.swing.JFrame {
	private JLabel lblImage;
	private JButton btnRun;
	private JButton btnStep;
	private JScrollPane jScrollPane1;
	private JTextArea taTrace;
	
	private Rete engine;	
	private ValueVector vecCannibalState;
	private int index = 0;
	private int m = 3;
	private int c = 3;
	private int b = 1;
	private ImageIcon image = new ImageIcon(getClass().getClassLoader().getResource("images/init.gif"));

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		Cannibal inst = new Cannibal();
		inst.setVisible(true);
	}
	
	public Cannibal() {
		super();
		initGUI();
		
		this.lblImage.setSize(image.getIconWidth(),image.getIconHeight());
		this.lblImage.setIcon(image);
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.getContentPane().setLayout(null);
			{
				lblImage = new JLabel();
				this.getContentPane().add(lblImage);
				lblImage.setBounds(28, 23, 297, 72);
				lblImage.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
				lblImage.setSize(300, 80);
			}
			{
				btnRun = new JButton();
				this.getContentPane().add(btnRun);
				btnRun.setText("Run");
				btnRun.setBounds(76, 113, 86, 30);
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
				btnStep.setBounds(168, 113, 86, 30);
				btnStep.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnStepActionPerformed(evt);
					}
				});
			}
			{
				jScrollPane1 = new JScrollPane();
				this.getContentPane().add(jScrollPane1);
				jScrollPane1.setBounds(29, 155, 300, 187);
				{
					taTrace = new JTextArea();
					jScrollPane1.setViewportView(taTrace);
				}
			}
			pack();
			this.setSize(361, 404);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void btnRunActionPerformed(ActionEvent evt) {
		try {
			this.taTrace.setText("");
			ReadJessFile readJessFile = new ReadJessFile("MC-ModTreeJava.clp");
			String strTemp = readJessFile.getJessFileContent();
			
			if (strTemp.substring(0, 1).equals("1")) {
				this.engine = new Rete();
				this.engine.executeCommand(strTemp.substring(1));

				Value valCannibalState = this.engine.fetch("cannibalState");
				
				this.vecCannibalState = valCannibalState.listValue(this.engine.getGlobalContext());
				
  			    for(int i=0; i<this.vecCannibalState.size(); i++)
					this.taTrace.append(this.vecCannibalState.get(i)+"\n");
				
			} else
				this.taTrace.append(strTemp.substring(1) + "\n");
			

		} catch (JessException ex) {
			this.taTrace.append(ex.toString() + "\n");
		}
	}
	
	private void btnStepActionPerformed(ActionEvent evt) {
		try {
			if(this.m != 0 || this.c != 0 || this.b != 2) {
				String cannibal = ((Value)this.vecCannibalState.get(this.index++)).toString();
		
				if(cannibal.equals("M_1to2")) {
					this.m--;
					this.b = 2;
					ShowCannibal();				 
				} else if(cannibal.equals("M_2to1")) {
					this.m++;
					this.b = 1;
					ShowCannibal();				
				} else if(cannibal.equals("C_1to2")) {
					this.c--;
					this.b = 2;
					ShowCannibal();				
				} else if(cannibal.equals("C_2to1")) {
					this.c++;
					this.b = 1;
					ShowCannibal();				
				} else if(cannibal.equals("MC_1to2")) {
					this.m--;
					this.c--;
					this.b = 2;
					ShowCannibal();				
				} else if(cannibal.equals("MC_2to1")) {
					this.m++;
					this.c++;
					this.b = 1;
					ShowCannibal();				
				} else if(cannibal.equals("MM_1to2")) {
					this.m -= 2;
					this.b = 2;
					ShowCannibal();				
				} else if(cannibal.equals("MM_2to1")) {
					this.m += 2;
					this.b = 1;
					ShowCannibal();				
				} else if(cannibal.equals("CC_1to2")) {
					this.c -= 2;
					this.b = 2;
					ShowCannibal();				
				} else if(cannibal.equals("CC_2to1")) {
					this.c += 2;
					this.b = 1;
					ShowCannibal();	
				}
			}
		} catch (JessException ex) {
			this.taTrace.append(ex.toString() + "\n");
		}
	}
	
	private void ShowCannibal() {
		this.image = new ImageIcon(getClass().getClassLoader().getResource("images/"+String.valueOf(this.m)+String.valueOf(this.c)+String.valueOf(this.b)+ ".gif"));
		
		this.lblImage.setSize(this.image.getIconWidth(),this.image.getIconHeight());
		this.lblImage.setIcon(this.image);
	}
}

