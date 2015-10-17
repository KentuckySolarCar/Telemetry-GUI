package com.telemetry.gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class TabbedPane extends Panel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2149927542836761326L;
	private final Panel button_panel;
	private final Panel display_panel;
	private ArrayList<Tab> tabs = new ArrayList<>();
	private ArrayList<Panel> panels = new ArrayList<>();
	private int tab_select = 0;
	
	public TabbedPane(){
		super();
		
		setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		
		button_panel = new Panel();
		display_panel = new Panel();
		
		add(button_panel, BorderLayout.NORTH);
		add(display_panel, BorderLayout.SOUTH);
	}
	
	private void updateComponents(){
		button_panel.removeAll();
		button_panel.setLayout( new GridLayout(1, tabs.size()) );
		
		for( Tab tab : tabs ){
			button_panel.add(tab);
		}
		
		display_panel.removeAll();
		display_panel.add( panels.get(tab_select) );
		
		repaint();
	}
	
	public void addTab( String name, Panel comp ){
		// Add panels
		panels.add( comp );
		
		// Add buttons
		final Tab tab_button = new Tab(name);
		
		tabs.add(tab_button);
		
		updateComponents();
	}
	
	public class Tab extends Component{
		
		public static final int HEIGHT = 25;
		
		private boolean selected = false;
		private final String text;
		private final int tab_index;
		
		public Tab( String text ){
			super();
			
			setPreferredSize(new Dimension(0,HEIGHT));

			tab_index = tabs.size();
			this.text = text;
			
			addMouseListener(new MouseListener(){

				@Override
				public void mouseClicked(MouseEvent arg0) {
					if( tab_index != tab_select ){
						tab_select = tab_index;
						updateComponents();
						setSelected(true);
						repaint();
					}
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {}

				@Override
				public void mouseExited(MouseEvent arg0) {}

				@Override
				public void mousePressed(MouseEvent arg0) {}

				@Override
				public void mouseReleased(MouseEvent arg0) {}
				
			});
		}
		
		public void setSelected( boolean sel ){
			selected = sel;
		}
		
		@Override
		public void paint(Graphics g){
			g.setColor(Color.white);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			g.setColor(Color.black);

			System.out.println(selected);
			
			if(selected){
				g.drawRect(0, 0, getWidth()-1, getHeight()+1);
			}else{
				g.drawRect(0, 0, getWidth()-1, getHeight()-1);
			}
			
			FontMetrics fm = g.getFontMetrics();
			g.drawString(text, (getWidth()-fm.stringWidth(text))/2, (getHeight()+fm.getAscent())/2);
		}
		
	}
	
}