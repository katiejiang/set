import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.text.DecimalFormat;

public class Game implements Runnable {
	private HashMap<Double, LinkedList<String>> scores = new HashMap<Double, LinkedList<String>>();
	private String INSTRUCTIONS_MESSAGE = "";

	public void run() {
		final JFrame frame = new JFrame("Set");
		frame.setLocation(300, 300);

		final JPanel status_panel = new JPanel();
		frame.add(status_panel, BorderLayout.SOUTH);
		final JLabel status = new JLabel("");
		status_panel.add(status);

		try {
			File f = new File("highscores.txt");
			if (f.createNewFile()) {
				updateHighScores();
			} else {
				readHighScores();
			}
		} catch (IOException e) {
			System.out.println("Could not read high scores: " + e);
		}

		try {
			BufferedReader br = new BufferedReader(new FileReader("instructions.html"));
			while (br.ready()) {
				INSTRUCTIONS_MESSAGE += br.readLine();
			}
		} catch (IOException e) {
		}
		
		final Table table = new Table(status, scores);
		frame.add(table, BorderLayout.CENTER);

		do {
			table.username = JOptionPane.showInputDialog(frame, "What's your username?");
		} while (table.username == null || table.username.equals(""));

		table.updateStatus();

		final JPanel control_panel = new JPanel();
		frame.add(control_panel, BorderLayout.NORTH);

		final JButton reset = new JButton("New Game");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				table.reset();
			}
		});
		control_panel.add(reset);
		
		final JButton instructions = new JButton("Instructions");
		instructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, INSTRUCTIONS_MESSAGE);
			}
		});
		control_panel.add(instructions);

		final JButton user = new JButton("Change Username");
		user.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean canCancel = table.username != null && !table.username.equals("");
				String oldName = table.username;
				table.username = JOptionPane.showInputDialog(frame, "What's your username?");
				if (canCancel && (table.username == null || table.username.equals(""))) {
					table.username = oldName;
				} else if (table.username == null || table.username.equals("")) {
					do {
						table.username = JOptionPane.showInputDialog(frame, "What's your username?");
					} while (table.username == null || table.username.equals(""));
				}
				table.updateStatus();
			}
		});
		control_panel.add(user);

		final JButton writeScores = new JButton("Show High Scores");
		writeScores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					updateHighScores();
				} catch (IOException ioe) {
					System.out.println(ioe);
				}
			}
		});
		control_panel.add(writeScores);
		
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public void readHighScores() throws IOException {
		Reader r = new FileReader("highscores.txt");
		try {
			BufferedReader br = new BufferedReader(r);
			if (br.ready()) {
				br.readLine();
				br.readLine();
				br.readLine();
				while (br.ready()) {
					String[] tokens = br.readLine().split("\t");
					String name = tokens[1];
					double time = Double.parseDouble(tokens[3]);
					if (scores.containsKey(time)) {
						scores.get(time).add(name);
					} else {
						LinkedList<String> l = new LinkedList<String>();
						l.add(name);
						scores.put(time, l);
					}
				}
			}
			br.close();
		} finally {
			r.close();

		}
	}

	public void updateHighScores() throws IOException {
		String content = "High Scores\n\n#\tName\t\tTime (s)";
		if (!scores.isEmpty()) {
			ArrayList<Double> sortedTimes = new ArrayList<Double>();
			for (Double k : scores.keySet()) {
				sortedTimes.add(k);
			}
			Collections.sort(sortedTimes);

			int c = 1;
			for (Double t : sortedTimes) {
				for (String n : scores.get(t)) {
					DecimalFormat df = new DecimalFormat("0.0");
					content += "\n" + c + "\t" + n + "\t\t" + df.format(t);
				}
				c++;
			}
		}

		BufferedWriter out = new BufferedWriter(new FileWriter("highscores.txt"));
		out.write(content);
		out.close();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}
}
