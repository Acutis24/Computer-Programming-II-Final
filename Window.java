import javax.swing.JFrame;//the actual window
import javax.swing.JLabel;//more features
import javax.swing.ImageIcon;//to add images
import javax.swing.JButton; //to add buttons
import java.awt.event.ActionListener;//to add action to the buttons
import java.awt.event.ActionEvent;//to add action to the buttons

public class Window {
    public static void main(String[] args) {
        JFrame frame = new JFrame(); //creates a window
        frame.setTitle("Window"); //sets the title of the window
        frame.setSize(1750, 1000); //size of the window
        frame.setLayout(null); //sets the layout to null so we can set the position of the components
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //closes the window when you click the x button

        addButtons(frame); //calls the addButtons method

        frame.setVisible(true); //makes the window visible — must be last!
    }

    public static void addButtons(JFrame frame) {
        JButton playButton = new JButton("Play"); //creates the play button
        playButton.setBounds(775, 565, 150, 40); //sets the position and size of the button
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Game Started!"); //placeholder
            }
        });
        frame.add(playButton); //adds the button to the window
    }
}