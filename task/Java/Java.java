

import javax.swing.JFrame;

public class Java {

	public static void main(String[] args) {
		JFrame frame;
		if(args.length < 1)
			frame = new JFrame("Gabi");
		else
			frame = new JFrame(args[0]+ " " + args[1]);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setSize(400, 400);

	}
}
