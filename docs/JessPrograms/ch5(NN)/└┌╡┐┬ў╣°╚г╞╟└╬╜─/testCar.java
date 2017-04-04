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

    BufferedImage image; // 이미지를 스케일링하기 위해 버퍼드 이미지 생성
    Image car;

    int width = 640;
    int height = 480;
    // GUI 인터페이스 정의
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

    // 2차원의 신경망 크기를 정의, 입력 배열의 크기
    final static int SIZE=104;

    // 인식해야할 문자의 수를 정의
    final static int NUM = 10;
    char Chars[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    // 번호판 영역 중 인식할 문자에 대한 위치 정의
    int X_Pos[] = {29, 46, 63, 80};
    int Y_Pos = 19;
    int Inputs[] = new int[SIZE];

    int num_cap=0;
    int active = 0;  // if 1, then train network

    // Neural network:
    Neural network;

    //*********************************************
    // 애플릿 초기화
    //*********************************************
    public void init() {
       width = 500; // 애플릿의 초기 크기 정의
       height = 400;

       String param = getParameter("width");
       outNumber = new char[4];
       // 익스플로러로 애플릿을 실행할 때 애플릿 크기 정의를 읽어옴
//       if (param != null) width = Integer.parseInt(param);
       param = getParameter("height");
//       if (param != null) height = Integer.parseInt(param);
       resize(width, height);
      // GUI 인터페이스 초기화
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
       // 선택 버튼에 다음 항목들을 추가시킴
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

       choice1.select("car_train_01.jpg") ; // 초기 선택값 설정
       choice2.select("car_test_01.jpg") ;

       panel.add(new Button(RunLabel));  // GUI 인터페이스 추가
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

       panel.setBackground(Color.orange); // panel 색상
       setBackground(Color.pink); // 배경 색상

       // 새로운 신경망을 다음 크기로 생성
       // SIZE : 입력 수, 50: 은닉층의 뉴런 수, NUM: 인식 문자 수 (10개)
       network = new Neural(SIZE, 50, NUM);

       // 선택 버튼에 선택된 항목에 대해 이미지를 불러옴
       car = getImage(getDocumentBase(), "data/"+choice1.getSelectedItem());
            P("\nInitialized and loaded : " + choice1.getSelectedItem()+ "\n");       

       // 불러온 이미지에 대해서 100x50 pixels 크기로 스케일링
       image = Utilities.makeBufferedImage(car.getScaledInstance(100,50,0));
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    // 업데이트시 원래의 이미지와 버퍼드이미지를 다시 그려줌
    public void paint(Graphics g) {
       g.drawImage(car, 180, 230, 300, 150, this);
       g.drawImage(image,50,230, this);
    }

    // TextField의 내용을 지움
    public void ClearOutput() {
         outputText.replaceText("",0, 32000);
    }
    // 매개변수로 전달받은 스트링을 출력
    // 매개변수의 속성에 따라서 3가지로 정의
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
    // 이벤트 처리
    // 명령 버튼이 눌렸을 때 발생하는 이벤트를 처리
    // 이벤트가 발생할 시 명령 버튼의 레이블에 따라 처리
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
    // 입력된 데이터에 따라서 신경망을 training
    // 자동차 번호판의 pixel을 샘플링하여 RGB값을 추출한 후
    // 이를 흑백의 값으로 변환하여 training
    //************************************************
    public void train() {
      P("Starting to train network..wait..\n");
      int sum=0, j = 0, ic=0, oc=0;

      int rgbValue;
      int red;
      int green;
      int blue;
      double bgt;

      float ins[] = new float[104]; // 입력 뉴런의 배열
      float outs[] = new float[10]; // 출력 뉴런의 배열

      String start = choice1.getSelectedItem() ; // 기대값을 입력 받음
      s1 = text1.getText();
      s1.getChars(0,4, outNumber, 0);

      for (int i=29; i<85; i=i+17) {
      // 이미지의 pixel에 대해 샘플링을 수행하고
      // 샘플링된 pixel에 대해서 RGB값을 각각 추출함
      // 추출된 RGB값을 이용하여BGT값을 계산하여 0.7 이상일 경우
      // 문자 영역으로 인식
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

        // 인식해야할 문자에 해당되는 배열의 인덱스 값을
        // +0.4f 로 설정(기대값)
        for (int k=0; k<NUM; k++)  {
          if (Chars[k]==outNumber[j]) outs[oc++] = +0.4f;
          else      outs[oc++] = -0.4f;
             }
            j++;
      // error 값이 0.000001f보다 작을 때까지 training 포워드 패스를 수행
      for (int m=0; m<3000; m++) {
        float error = network.Train(ins, outs, 1); // training 함수 호출
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
        PutChar(); // test 수행
     }
    public void doResetButton() {
        train();  // training 행
        train();  // 정확성을 위해 반복 수행
        train();
        train();
        train();
        train();
    }
    // Load 명령 버튼 클릭 시 선택 버튼에서 선택된 이미지를 로드
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
    // 자동차 번호판의 문자를 인식하여 출력
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
      for (int i=29; i<85; i=i+17) { // 번호판의 4개의 숫자에 대해서 반복
      // 인식할 번호판의 문자 영역에 대해 샘플링된 RGB값을 추출하여
      // BGT값을 계산하고, 이 값에 따라서 뉴런의 입력 배열값을 설정
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

            // 입력 뉴런의 값으로 은닉층을 통해서 출력 뉴런값을 계산
            network.ForwardPass();
            // 출력 뉴런의 값 중 최대 값을 찾음
            int index=0;
            float maxVal=-99f;
            for (int j=0; j<NUM; j++) {
//            P("forward: " + Float.toString(network.Outputs[j]) + "\n");
                if (network.Outputs[j]>maxVal) {
                   maxVal = network.Outputs[j];
                   index = j;
                }
            }
            // 최대값을 가지는 출력 뉴런의 인덱스에 따라서 결과를 출력하며,
            // 최대값이 0.0f 보다 작을 경우 Maybe not correct 문자열과 함께 출력
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

