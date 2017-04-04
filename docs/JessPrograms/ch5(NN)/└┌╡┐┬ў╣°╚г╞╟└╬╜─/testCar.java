// testCar.java
// NeuralNet Java classes: tester for Car Licence

import java.awt.*;
import java.awt.font.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.applet.Applet;
import java.awt.Toolkit;
import java.awt.font.*;
import com.sun.image.codec.jpeg.*;
import java.awt.image.*;
import java.awt.geom.*;

public class testCar  extends Applet{
    public String RunLabel   = new String("Test");
    public String ResetLabel = new String("Training");
    public String ClearLabel = new String("Clear");
    public String LoadLabel = new String("Load");
    public String InitialInput = "";

    BufferedImage image; // �̹����� �����ϸ��ϱ� ���� ���۵� �̹��� ����
    Image car;

    int width = 640;
    int height = 480;
    // GUI �������̽� ����
    java.awt.Choice choice1;
    java.awt.Choice choice2;
    java.awt.Label label1;
    java.awt.Checkbox radioButton1;
    CheckboxGroup Group1;
    java.awt.Checkbox radioButton2;

    protected Panel panel;
    TextField inputText;
    TextArea outputText;
    TextField text1;
    char outNumber[];
    String s1;

    Graphics background;  // used for double buffering

    // 2������ �Ű�� ũ�⸦ ����, �Է� �迭�� ũ��
    final static int SIZE=104;

    // �ν��ؾ��� ������ ���� ����
    final static int NUM = 10;
    char Chars[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    // ��ȣ�� ���� �� �ν��� ���ڿ� ���� ��ġ ����
    int X_Pos[] = {29, 46, 63, 80};
    int Y_Pos = 19;
    int Inputs[] = new int[SIZE];

    int num_cap=0;
    int active = 0;  // if 1, then train network

    // Neural network:
    Neural network;

    //*********************************************
    // ���ø� �ʱ�ȭ
    //*********************************************
    public void init() {
       width = 500; // ���ø��� �ʱ� ũ�� ����
       height = 400;

       String param = getParameter("width");
       outNumber = new char[4];
       // �ͽ��÷η��� ���ø��� ������ �� ���ø� ũ�� ���Ǹ� �о��
//       if (param != null) width = Integer.parseInt(param);
       param = getParameter("height");
//       if (param != null) height = Integer.parseInt(param);
       resize(width, height);
      // GUI �������̽� �ʱ�ȭ
       panel = new Panel();
       panel.setLayout(new GridLayout());
       panel.resize(width - 10, height / 2);

       choice1 = new java.awt.Choice();
       add(choice1);
       choice1.reshape(36,264,150,24);

       choice2 = new java.awt.Choice();
       add(choice2);
       choice2.reshape(36,264,150,24);

       Group1 = new CheckboxGroup();
       radioButton1 = new java.awt.Checkbox("Select training", Group1, true);
       radioButton1.reshape(24,300,100,24);
       add(radioButton1) ;
       radioButton2 = new java.awt.Checkbox("Select test", Group1, false);
       radioButton2.reshape(24,324,120,23);
       add(radioButton2) ;
       // ���� ��ư�� ���� �׸���� �߰���Ŵ
       choice1.addItem("car_train_01.jpg");
       choice1.addItem("car_train_02.jpg");
       choice1.addItem("car_train_03.jpg");
       choice1.addItem("car_train_04.jpg");
       choice1.addItem("car_train_05.jpg");
       choice1.addItem("car_train_06.jpg");
       choice1.addItem("car_train_07.jpg");
       choice1.addItem("car_train_08.jpg");
       choice1.addItem("car_train_09.jpg");

       choice2.addItem("car_test_01.jpg");
       choice2.addItem("car_test_02.jpg");
       choice2.addItem("car_test_03.jpg");
       choice2.addItem("car_test_04.jpg");
       choice2.addItem("car_test_05.jpg");
       choice2.addItem("car_test_06.jpg");
       choice2.addItem("car_test_07.jpg");
       choice2.addItem("car_test_08.jpg");
       choice2.addItem("car_test_09.jpg");
       choice2.addItem("car_test_10.jpg");
       choice2.addItem("car_test_11.jpg");
       choice2.addItem("car_test_12.jpg");
       choice2.addItem("car_test_21.jpg");
       choice2.addItem("car_test_22.jpg");
       choice2.addItem("car_test_23.jpg");
       choice2.addItem("car_test_24.jpg");
       choice2.addItem("car_test_25.jpg");

       choice2.addItem("car_train_01.jpg");
       choice2.addItem("car_train_02.jpg");
       choice2.addItem("car_train_03.jpg");
       choice2.addItem("car_train_04.jpg");
       choice2.addItem("car_train_05.jpg");
       choice2.addItem("car_train_06.jpg");
       choice2.addItem("car_train_07.jpg");
       choice2.addItem("car_train_08.jpg");
       choice2.addItem("car_train_09.jpg");

       choice1.select("car_train_01.jpg") ; // �ʱ� ���ð� ����
       choice2.select("car_test_01.jpg") ;

       panel.add(new Button(RunLabel));  // GUI �������̽� �߰�
       panel.add(new Button(ResetLabel));
       panel.add(new Button(ClearLabel));
       panel.add(new Button(LoadLabel));

       label1 = new java.awt.Label("Car Number");
       label1.reshape(24,240,96,24);
       add(label1);
       text1=new TextField("", 10);
       add(text1);

       add(panel);
       outputText = new TextArea("", 9, 45);
       add(outputText);

       panel.setBackground(Color.orange); // panel ����
       setBackground(Color.pink); // ��� ����

       // ���ο� �Ű���� ���� ũ��� ����
       // SIZE : �Է� ��, 50: �������� ���� ��, NUM: �ν� ���� �� (10��)
       network = new Neural(SIZE, 50, NUM);

       // ���� ��ư�� ���õ� �׸� ���� �̹����� �ҷ���
       car = getImage(getDocumentBase(), "data/"+choice1.getSelectedItem());
            P("\nInitialized and loaded : " + choice1.getSelectedItem()+ "\n");       

       // �ҷ��� �̹����� ���ؼ� 100x50 pixels ũ��� �����ϸ�
       image = Utilities.makeBufferedImage(car.getScaledInstance(100,50,0));
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    // ������Ʈ�� ������ �̹����� ���۵��̹����� �ٽ� �׷���
    public void paint(Graphics g) {
       g.drawImage(car, 180, 230, 300, 150, this);
       g.drawImage(image,50,230, this);
    }

    // TextField�� ������ ����
    public void ClearOutput() {
         outputText.replaceText("",0, 32000);
    }
    // �Ű������� ���޹��� ��Ʈ���� ���
    // �Ű������� �Ӽ��� ���� 3������ ����
    public void P(String s) {
         outputText.appendText(s);
    }
    public void P(int i) {
         StringBuffer sb = new StringBuffer();
         sb.append(i);
         String s2 = new String(sb);
         outputText.appendText(s2);
    }
    public void P(float x) {
         StringBuffer sb = new StringBuffer();
         sb.append(x);
         String s2 = new String(sb);
         outputText.appendText(s2);
    }
    //***************************************************
    // �̺�Ʈ ó��
    // ��� ��ư�� ������ �� �߻��ϴ� �̺�Ʈ�� ó��
    // �̺�Ʈ�� �߻��� �� ��� ��ư�� ���̺� ���� ó��
    //***************************************************
    public boolean action(Event evt, Object obj) {
        System.out.println (evt.id);
        if (evt.target instanceof Button) {
           String label = (String)obj;
           if (label.equals(RunLabel)) {   // for testing
              System.out.println("Run button pressed\n");
              doRunButton();
              repaint();
              return true;
           }
           if (label.equals(ResetLabel)) {  // for training
              System.out.println("Reset button pressed\n");
              doResetButton();
              repaint();
              return true;
           }
           if (label.equals(ClearLabel)) {  // for clearing
              System.out.println("Clear button pressed\n");
              outputText.setText("");
              repaint();
              return true;
           }
           if (label.equals(LoadLabel)) {   // for loading image
              System.out.println("Load button pressed\n");
              loadImage();
              repaint();
              return true;
           }
        }
        return false;
    }

    //************************************************
    // �Էµ� �����Ϳ� ���� �Ű���� training
    // �ڵ��� ��ȣ���� pixel�� ���ø��Ͽ� RGB���� ������ ��
    // �̸� ����� ������ ��ȯ�Ͽ� training
    //************************************************
    public void train() {
      P("Starting to train network..wait..\n");
      int sum=0, j = 0, ic=0, oc=0;

      int rgbValue;
      int red;
      int green;
      int blue;
      double bgt;

      float ins[] = new float[104]; // �Է� ������ �迭
      float outs[] = new float[10]; // ��� ������ �迭

      String start = choice1.getSelectedItem() ; // ��밪�� �Է� ����
      s1 = text1.getText();
      s1.getChars(0,4, outNumber, 0);

      for (int i=29; i<85; i=i+17) {
      // �̹����� pixel�� ���� ���ø��� �����ϰ�
      // ���ø��� pixel�� ���ؼ� RGB���� ���� ������
      // ����� RGB���� �̿��Ͽ�BGT���� ����Ͽ� 0.7 �̻��� ���
      // ���� �������� �ν�
          for (int x=2; x<17; x=x+2) {
            for (int y=3; y<28; y=y+2) {
               rgbValue = image.getRGB(i+x,19+y);
               red=(rgbValue & 0x00ff0000)>>>16;
               green=(rgbValue & 0x0000ff00)>>>8;
               blue=rgbValue & 0x000000ff;
               bgt=0.333*red/255 + 0.333*green/255 + 0.333*blue/255;

               if (bgt < 0.7) {
                ins[ic++] = -0.4f;
              }  else  {
                ins[ic++] = +0.4f;
            }
          }   //y
        }  //x

        // �ν��ؾ��� ���ڿ� �ش�Ǵ� �迭�� �ε��� ����
        // +0.4f �� ����(��밪)
        for (int k=0; k<NUM; k++)  {
          if (Chars[k]==outNumber[j]) outs[oc++] = +0.4f;
          else      outs[oc++] = -0.4f;
             }
            j++;
      // error ���� 0.000001f���� ���� ������ training ������ �н��� ����
      for (int m=0; m<3000; m++) {
        float error = network.Train(ins, outs, 1); // training �Լ� ȣ��
        if ((m % 10) == 0) {
           P("Output error for iteration " +
             m + " =" + error + "\n");
        }
        if (error < 0.000001f)  break;  // done training
      }
       ic=0; oc=0;
      } //i

    }

    public void doRunButton() {
        PutChar(); // test ����
     }
    public void doResetButton() {
        train();  // training ��
        train();  // ��Ȯ���� ���� �ݺ� ����
        train();
        train();
        train();
        train();
    }
    // Load ��� ��ư Ŭ�� �� ���� ��ư���� ���õ� �̹����� �ε�
    public void loadImage() {
       text1.setText("");
       outputText.setText("");
       if (radioButton1.getState() == true) {
       car = getImage(getDocumentBase(), "data/"+choice1.getSelectedItem());
       image = Utilities.makeBufferedImage(car.getScaledInstance(100,50,0));
       } // endif
       if (radioButton2.getState() == true) {
       car = getImage(getDocumentBase(), "data/"+choice2.getSelectedItem());
       image = Utilities.makeBufferedImage(car.getScaledInstance(100,50,0));
       } // endif
     }
    //*****************************************************
    // �ڵ��� ��ȣ���� ���ڸ� �ν��Ͽ� ���
    //*****************************************************
    public void PutChar() {
      // Special case:Mode==1 for testing:
      int rgbValue;
      int red;
      int green;
      int blue;
      double bgt;
      String goal = choice2.getSelectedItem() ;

      int ic=0;
      for (int i=29; i<85; i=i+17) { // ��ȣ���� 4���� ���ڿ� ���ؼ� �ݺ�
      // �ν��� ��ȣ���� ���� ������ ���� ���ø��� RGB���� �����Ͽ�
      // BGT���� ����ϰ�, �� ���� ���� ������ �Է� �迭���� ����
          for (int x=2; x<17; x=x+2) {
            for (int y=3; y<28; y=y+2) {
               rgbValue = image.getRGB(i+x,19+y);
               red=(rgbValue & 0x00ff0000)>>>16;
               green=(rgbValue & 0x0000ff00)>>>8;
               blue=rgbValue & 0x000000ff;
               bgt=0.333*red/255 + 0.333*green/255 + 0.333*blue/255;

               if (bgt >= 0.7){
                network.Inputs[ic++] = +0.4f;
              }  else  {
                network.Inputs[ic++] = -0.4f;
            }
          }   //y

        }  //x

            // �Է� ������ ������ �������� ���ؼ� ��� �������� ���
            network.ForwardPass();
            // ��� ������ �� �� �ִ� ���� ã��
            int index=0;
            float maxVal=-99f;
            for (int j=0; j<NUM; j++) {
//            P("forward: " + Float.toString(network.Outputs[j]) + "\n");
                if (network.Outputs[j]>maxVal) {
                   maxVal = network.Outputs[j];
                   index = j;
                }
            }
            // �ִ밪�� ������ ��� ������ �ε����� ���� ����� ����ϸ�,
            // �ִ밪�� 0.0f ���� ���� ��� Maybe not correct ���ڿ��� �Բ� ���
              if (maxVal < 0.0){
                    P("Character recognized. But Maybe not correct: " + Chars[index]+"  : "+Float.toString(maxVal)+"\n" );
              }  else  {
                    P("Character recognized: " + Chars[index]+"  : "+Float.toString(maxVal)+"\n" );
            }
//            return;
        ic=0;
      } //i

    }
}

