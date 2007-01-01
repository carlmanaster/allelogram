package org.carlmanaster.allelogram.gui;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.carlmanaster.allelogram.model.Classifier;

public class MenuBarBuilder {
	private final AllelogramApplet applet;
	private final MenuBar menubar;

	public MenuBarBuilder(AllelogramApplet applet) {
		this.applet = applet;
		menubar = applet.getMenuBar();
	}

	public void buildMenuBar() {
		menubar.add(buildFileMenu());
		menubar.add(buildBinMenu());
		menubar.add(buildNormalizeMenu());
		menubar.add(buildSortMenu());
		menubar.add(buildColorMenu());
	}

	/*
	 * open... (O)
	 * ------
	 * close (W)
	 * save (S)
	 * save as...
	 * revert
	 * -------
	 * save bins...
	 * -------
	 * pick file format...
	 * -------
	 * page setup...
	 * print (P)...
	 * 
	 */
	private Menu buildFileMenu() {
		MenuItem open = new MenuItem("Open");
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				applet.doOpen(null,null);
			}
		});
		open.setShortcut(new MenuShortcut(KeyEvent.VK_O, false));

		MenuItem saveAs = new MenuItem("Save As...");
		saveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				applet.doSaveAs();
			}
		});
		saveAs.setShortcut(new MenuShortcut(KeyEvent.VK_S, false));

        MenuItem print = new MenuItem("Print");
		print.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent event) {
		    applet.doPrint();
		  }
		});
		print.setShortcut(new MenuShortcut(KeyEvent.VK_P, false));

		MenuItem pickFileFormat = new MenuItem("Pick File Format");
		pickFileFormat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				applet.doPickFileFormat();
			}
		});
		Menu menu = makeMenu("File");
		menu.add(open);
		menu.add(saveAs);
		menu.addSeparator();
		menu.add(print);
		menu.addSeparator();
		menu.add(pickFileFormat);
		return menu;
	}
	
	private Menu makeMenu(String s) {
		Menu menu = new Menu(s);
		menu.setName(s);
		return menu;
	}

	/*
	 * guess (G)
	 * guess using size
	 * ---------
	 * add bin above
	 * add bin below
	 * delete bin
	 * ---------
	 * clear bins
	 * 
	 */
	private Menu buildBinMenu() {
		MenuItem guess = new MenuItem("Guess Bins");
		guess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				applet.doGuess();
			}
		});
		guess.setShortcut(new MenuShortcut(KeyEvent.VK_G, false));

		MenuItem clear = new MenuItem("Clear Bins");
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				applet.doClearBins();
			}
		});

		MenuItem bySize = new MenuItem("Name Bins by Size");
		bySize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				applet.nameBinsBySize();
			}
		});
		MenuItem byLetter = new MenuItem("Name Bins by Letter");
		byLetter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				applet.nameBinsByLetter();
			}
		});
		MenuItem byIndex = new MenuItem("Name Bins by Index");
		byIndex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				applet.nameBinsByIndex();
			}
		});

		Menu menu = makeMenu("Bin");
		menu.add(guess);
		menu.addSeparator();
		menu.add(clear);
		menu.addSeparator();
		menu.add(bySize);
		menu.add(byLetter);
		menu.add(byIndex);
		
		return menu;
	}
	
	/*
	 * Auto-Normalize
	 * --------------
	 * Clear Normalization
	 * 
	 */
	private Menu buildNormalizeMenu() {
		MenuItem normalize = new MenuItem("Auto-Normalize");
		normalize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				applet.doAutonormalize();
			}
		});

		MenuItem clear = new MenuItem("Clear Normalization");
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				applet.doClearNormalization();
			}
		});

		Menu menu = makeMenu("Normalize");
		menu.add(normalize);
		menu.addSeparator();
		menu.add(clear);
		return menu;
	}
	
	/*
	 * size only
	 * -------
	 */
	private Menu buildSortMenu() {
		MenuItem sizeOnly = new MenuItem("by Size Only");
		sizeOnly.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				applet.doSort(null);
			}
		});

		Menu menu = makeMenu("Sort");
		menu.add(sizeOnly);
		menu.addSeparator();
		
		return menu;
	}
	
	public void extendSortMenu() {
		new SortMenuExtender().extend();
	}
	
	private Menu getMenu(String name) {
		for (int i = 0; i < menubar.getMenuCount(); ++i) {
			Menu menu = menubar.getMenu(i);
			if (menu.getName().equals(name))
				return menu;
		}
		return null;
	}

	/*
	 * don't color
	 * -------
	 */
	private Menu buildColorMenu() {
		MenuItem color = new MenuItem("Don't Color");
		color.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				applet.doColor(null);
			}
		});

		Menu menu = makeMenu("Color");
		menu.add(color);
		menu.addSeparator();
		return menu;
	}
	
	public void extendColorMenu() {
		new ColorMenuExender().extend();
	}
	
	private abstract class MenuExtender {
		private final Menu menu;
		protected final Map<String, Classifier> classifications;
		private final ArrayList<String> names;
		
		public MenuExtender(String menuName) {
			menu = getMenu(menuName);
			classifications = applet.getSettings().getClassifiers();
			names = new ArrayList<String>();
			names.addAll(classifications.keySet());
			Collections.sort(names);
		}

		public void extend() {
			removeOldItems();
			for (final String name : names) {
				MenuItem item = makeItem(name);
				item.addActionListener(makeListener(name));
				menu.add(item);
			}
			
		}

		private void removeOldItems() {
			// Leave the first item in place
			for (int i = menu.getItemCount() - 1; i > 1; --i)
				menu.remove(i);
		}

		protected abstract MenuItem makeItem(final String name);
		protected abstract ActionListener makeListener(final String name);
	}

	private class SortMenuExtender extends MenuExtender {
		SortMenuExtender() {
			super("Sort");
		}
		protected MenuItem makeItem(final String name) {
			return new MenuItem("by " + name);
		}
		protected ActionListener makeListener(final String name) {
			return new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					applet.doSort(classifications.get(name));
				}
			};
		}
	}
	
	private class ColorMenuExender extends MenuExtender {
		ColorMenuExender() {
			super("Color");
		}
		protected MenuItem makeItem(String name) {
			return new MenuItem("by " + name);
		}
		protected ActionListener makeListener(final String name) {
			return new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					applet.doColor(classifications.get(name));
				}
			};
		}
		
	}

}
