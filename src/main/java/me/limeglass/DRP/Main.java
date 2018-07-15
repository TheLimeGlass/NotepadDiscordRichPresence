package me.limeglass.DRP;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import javax.swing.*;

import me.limeglass.DRP.utils.EnumAllWindowNames;

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
			System.out.println("Notepad++ Discord Precsenced hooked into account " + user.username + "#" + user.userId);
			DiscordRPC.discordUpdatePresence(getPresence());
		}).build();
		DiscordRPC.discordInitialize("468010822610714634", handlers, true);
		
		while (true) {
			DiscordRPC.discordRunCallbacks();
			if (ready) DiscordRPC.discordUpdatePresence(getPresence());
		}
	}
	
	private static DiscordRichPresence getPresence() {
		String name = "Starting up...";
		for (String title : EnumAllWindowNames.getAllWindowNames()) {
			if (title.endsWith(" - Notepad++")) {
				int index = title.lastIndexOf("\\");
				name = title.substring(index + 1, title.lastIndexOf(" - Notepad++"));
				name = "Editing: " + name;
			}
		}
		return new DiscordRichPresence.Builder(name)
			//.setDetails("Editing: " + name)
			.setBigImage("big", null)
			.build();
	}
}
