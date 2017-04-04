// {$R Hello.JFM}

import java.awt.*;
import java.applet.Applet;

// Class Hello
public class Hello extends Applet
{
    final int MenuBarHeight = 0;

    // Component Declaration
    public TextField TextField1;
    public Button Display;
    public Button Clear;
    // End of Component Declaration

    // init()
    public void init()
    {
        // Frame Initialization
        setForeground(Color.black);
        setBackground(Color.lightGray);
        setFont(new Font("Dialog",Font.BOLD,12));
        setLayout(null);
        // End of Frame Initialization

        // Component Initialization
        TextField1 = new TextField(" ");
        TextField1.setForeground(Color.black);
        TextField1.setBackground(Color.white);
        TextField1.setFont(new Font("Dialog",Font.BOLD,12));
        Display = new Button("Display");
        Display.setFont(new Font("Dialog",Font.BOLD,12));
        Clear = new Button("Clear");
        Clear.setFont(new Font("Dialog",Font.BOLD,12));
        // End of Component Initialization

        // Add()s
        add(Clear);
        add(Display);
        add(TextField1);
        // End of Add()s

        InitialPositionSet();
    } // End of init()

    // start()
    public void start()
    {
    } // End of start()

    // stop()
    public void stop()
    {
    } // End of stop()

    // destroy()
    public void destroy()
    {
    } // End of destroy()

    public void paint(Graphics g)
    {
        // paint()
        // End of paint()
    }

    public void InitialPositionSet()
    {
        // InitialPositionSet()
        resize(392,273);
        TextField1.reshape(152,48+MenuBarHeight,121,24);
        Display.reshape(105,159+MenuBarHeight,75,25);
        Clear.reshape(241,160+MenuBarHeight,75,25);
        // End of InitialPositionSet()
    }

    public boolean handleEvent(Event evt)
    {
        // handleEvent()
        if (evt.id == Event.WINDOW_DESTROY && evt.target == this) Hello_WindowDestroy(evt.target);
        else if (evt.id == Event.ACTION_EVENT && evt.target == Display) Display_Action(evt.target);
        else if (evt.id == Event.ACTION_EVENT && evt.target == Clear) Clear_Action(evt.target);
        // End of handleEvent()

        return super.handleEvent(evt);
    }

    // Event Handling Routines
    public void Hello_WindowDestroy(Object target)
    {
        System.exit(0);
    }

    public void Display_Action(Object target)
    {
            TextField1.setText("æ»≥Á«œººø‰?");
    }

    public void Clear_Action(Object target)
    {
           TextField1.setText("");
    
    }

    // End of Event Handling Routines

} // End of Class Hello
