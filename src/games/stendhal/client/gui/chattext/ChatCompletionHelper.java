/**
 * 
 */
package games.stendhal.client.gui.chattext;

import games.stendhal.common.filter.CollectionFilter;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.apache.log4j.Logger;
/**
 * Matches the entered text of chat with the online player list to enable tab completion of nicknames
 * 
 * @author madmetzger
 */
public final class ChatCompletionHelper extends KeyAdapter {
	
	private static final Logger logger = Logger.getLogger(ChatCompletionHelper.class);
	
	private final ChatTextController chatController;
	
	private final Set<String> playersonline;
	
	private int  lastkeypressed;
	
	private Collection< ? extends String> resultset = Collections.emptyList();
	
	private int currentIndex;
	
	private String output;

	/**
	 * Create a new ChatCompletionHelper
	 * @param chatTextController
	 * @param list
	 */
	public ChatCompletionHelper(final ChatTextController chatTextController,
			final Set<String> list) {
		this.chatController = chatTextController;
		this.playersonline = list;
	}

	@Override
	public void keyPressed(final KeyEvent e) {

		final int keypressed = e.getKeyCode();

		if (keypressed == KeyEvent.VK_TAB) {
			if (lastkeypressed != KeyEvent.VK_TAB) {
				currentIndex = 0;
				logger.debug("Contents of PlayerList on tab: "+ playersonline);
				buildNames();

			} else {
				currentIndex++;
				if (currentIndex == resultset.size()) {
					currentIndex = 0;
				}
			}
			if (!resultset.isEmpty()) {

				chatController.setChatLine(output
						+ resultset.toArray()[currentIndex]);
			}
		}
		lastkeypressed = e.getKeyCode();
	}

	private void buildNames() {
		final String[] strwords = chatController.getText()
				.split("\\s+");

		final String prefix = strwords[strwords.length - 1];

		final CollectionFilter<String> filter = new StringPrefixFilter(
				prefix);
		output = "";
		for (int j = 0; j < strwords.length - 1; j++) {
			output = output + strwords[j] + " ";
		}

		resultset = filter.filterCopy(playersonline);
	}
	
}
