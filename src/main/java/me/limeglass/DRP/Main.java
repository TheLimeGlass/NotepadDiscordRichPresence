package me.limeglass.DRP;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import javax.swing.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

	private static boolean ready = false;

	public static void main(String[] args) {
		JFrame frame = new JFrame("LimeNotepad++");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		JLabel text = new JLabel("In Discord, set your active game to: \"LimeNotepad++\"");
		frame.getContentPane().add(text, SwingConstants.CENTER);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Shutting down DiscordHook.");
			DiscordRPC.discordShutdown();
		}));

		DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {
			Main.ready = true;
			System.out.println("Welcome " + user.username + "#" + user.discriminator + ".");
			DiscordRPC.discordUpdatePresence(getPresence());
		}).build();
		DiscordRPC.discordInitialize("468010822610714634", handlers, true);

		while (true) {
			DiscordRPC.discordRunCallbacks();

			if (!ready) continue;
			
			if (getProcessNames().contains("notepad++.exe")) {
				DiscordRPC.discordUpdatePresence(getPresence());
			}
		}
	}
	
	private static DiscordRichPresence getPresence() {
		return new DiscordRichPresence.Builder("Running Test")
			.setBigImage("big", null)
			.build();
	}
	
	private static Set<String> getProcessNames() {
		Set<String> processes = new HashSet<String>();
		try {
			Process process = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			processes = input.lines().collect(Collectors.toSet());
			input.close();
		} catch (Exception err) {
			System.out.println("Error while grabbing the processes of the system.");
			err.printStackTrace();
		}
		return processes;
	}
}
