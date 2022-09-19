package org.argouml.uml.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import java.lang.Integer;


class LabelledLayout implements LayoutManager,java.io.Serializable {
	private static final long serialVersionUID = -5596655602155151443l;
	private int hgap;
	private int vgap;
	private boolean ignoreSplitters;
	public LabelledLayout() {
		ignoreSplitters = false;
		hgap = 0;
		vgap = 0;
	}
	public LabelledLayout(boolean ignoreSplitters) {
		this.ignoreSplitters = ignoreSplitters;
		this.hgap = 0;
		this.vgap = 0;
	}
	public LabelledLayout(int hgap,int vgap) {
		this.ignoreSplitters = false;
		this.hgap = hgap;
		this.vgap = vgap;
	}
	public void addLayoutComponent(String name,Component comp) {
	}
	public void removeLayoutComponent(Component comp) {
	}
	public Dimension preferredLayoutSize(Container parent) {
		synchronized (parent.getTreeLock()) {
			final Insets insets = parent.getInsets();
			int preferredWidth = 0;
			int preferredHeight = 0;
			int widestLabel = 0;
			final int componentCount = parent.getComponentCount();
			for (int i = 0;i < componentCount;++i) {
				Component childComp = parent.getComponent(i);
				if (childComp.isVisible()&&!(childComp instanceof Seperator)) {
					int childHeight = getPreferredHeight(childComp);
					if (childComp instanceof JLabel) {
						final JLabel jlabel = (JLabel) childComp;
						widestLabel = Math.max(widestLabel,getPreferredWidth(jlabel));
						childComp = jlabel.getLabelFor();
						final int childWidth = getPreferredWidth(childComp);
						preferredWidth = Math.max(preferredWidth,childWidth);
						childHeight = Math.min(childHeight,getPreferredHeight(jlabel));
					}
					preferredHeight += childHeight + this.vgap;
				}
			}
			preferredWidth += insets.left + widestLabel + insets.right;
			preferredHeight += insets.top + insets.bottom;
			return new Dimension(insets.left + widestLabel + preferredWidth + insets.right,preferredHeight);
		}
	}
	public Dimension minimumLayoutSize(Container parent) {
		synchronized (parent.getTreeLock()) {
			final Insets insets = parent.getInsets();
			int minimumHeight = insets.top + insets.bottom;
			final int componentCount = parent.getComponentCount();
			for (int i = 0;i < componentCount;++i) {
				Component childComp = parent.getComponent(i);
				if (childComp instanceof JLabel) {
					final JLabel jlabel = (JLabel) childComp;
					childComp = jlabel.getLabelFor();
					final int childHeight = Math.max(getMinimumHeight(childComp),getMinimumHeight(jlabel));
					minimumHeight += childHeight + this.vgap;
				}
			}
			return new Dimension(0,minimumHeight);
		}
	}
	public void layoutContainer(Container parent) {
		synchronized (parent.getTreeLock()) {
			int sectionX = parent.getInsets().left;
			final ArrayList<Component>components = new ArrayList<Component>();
			final int sectionCount = getSectionCount(parent);
			final int sectionWidth = getSectionWidth(parent,sectionCount);
			int sectionNo = 0;
			for (int i = 0;i < parent.getComponentCount();++i) {
				final Component childComp = parent.getComponent(i);
				if (childComp instanceof Seperator) {
					if (!this.ignoreSplitters) {
						layoutSection(parent,sectionX,sectionWidth,components,sectionNo++);
						sectionX += sectionWidth + this.hgap;
						components.clear();
					}
				}else {
					components.add(parent.getComponent(i));
				}
			}
			layoutSection(parent,sectionX,sectionWidth,components,sectionNo);
		}
	}
	private int getSectionCount(Container parent) {
		int sectionCount = 1;
		final int componentCount = parent.getComponentCount();
		if (!ignoreSplitters) {
			for (int i = 0;i < componentCount;++i) {
				if (parent.getComponent(i)instanceof Seperator) {
					++
					sectionCount;
				}
			}
		}
		return sectionCount;
	}
	private int getSectionWidth(Container parent,int sectionCount) {
		return(getUsableWidth(parent) - (sectionCount - 1) * this.hgap) / sectionCount;
	}
	private int getUsableWidth(Container parent) {
		final Insets insets = parent.getInsets();
		return parent.getWidth() - (insets.left + insets.right);
	}
	private void layoutSection(final Container parent,final int sectionX,final int sectionWidth,final ArrayList components,final int sectionNo) {
		final ArrayList<Integer>rowHeights = new ArrayList<Integer>();
		final int componentCount = components.size();
		if (componentCount == 0) {
			return;
		}
		int labelWidth = 0;
		int unknownHeightCount = 0;
		int totalHeight = 0;
		for (int i = 0;i < componentCount;++i) {
			final Component childComp = (Component) components.get(i);
			final int childHeight;
			if (childComp instanceof JLabel) {
				final JLabel jlabel = (JLabel) childComp;
				final Component labelledComp = jlabel.getLabelFor();
				labelWidth = Math.max(labelWidth,getPreferredWidth(jlabel));
				if (labelledComp != null) {
					++
					i;
					childHeight = getChildHeight(labelledComp);
					if (childHeight == 0) {
						++
						unknownHeightCount;
					}
				}else {
					childHeight = getPreferredHeight(jlabel);
				}
			}else {
				childHeight = getChildHeight(childComp);
				if (childHeight == 0) {
					++
					unknownHeightCount;
				}
			}
			totalHeight += childHeight + this.vgap;
			rowHeights.add(new Integer(childHeight));
		}
		totalHeight -= this.vgap;
		final Insets insets = parent.getInsets();
		final int parentHeight = parent.getHeight() - (insets.top + insets.bottom);
		int y = insets.top;
		int row = 0;
		for (int i = 0;i < componentCount;++i) {
			Component childComp = (Component) components.get(i);
			if (childComp.isVisible()) {
				int rowHeight;
				int componentWidth = sectionWidth;
				int componentX = sectionX;
				if (childComp instanceof JLabel&&((JLabel) childComp).getLabelFor() != null) {
					i++;
					final JLabel jlabel = (JLabel) childComp;
					childComp = jlabel.getLabelFor();
					jlabel.setBounds(sectionX,y,labelWidth,getPreferredHeight(jlabel));
					componentWidth = sectionWidth - (labelWidth);
					componentX = sectionX + labelWidth;
				}
				Integer tempint = rowHeights.get(row);
				rowHeight = tempint.intValue();
				if (rowHeight == 0) {
					try {
						rowHeight = calculateHeight(parentHeight,totalHeight,unknownHeightCount--,childComp);
					}catch (ArithmeticException e) {
						String lookAndFeel = UIManager.getLookAndFeel().getClass().getName();
						throw new IllegalStateException("Division by zero laying out " + childComp.getClass().getName() + " on " + parent.getClass().getName() + " in section " + sectionNo + " using " + lookAndFeel,e);
					}
					totalHeight += rowHeight;
				}
				if (childComp.getMaximumSize() != null&&getMaximumWidth(childComp) < componentWidth) {
					componentWidth = getMaximumWidth(childComp);
				}
				childComp.setBounds(componentX,y,componentWidth,rowHeight);
				y += rowHeight + this.vgap;
				++
				row;
			}
		}
	}
	private int getChildHeight(Component childComp) {
		if (isResizable(childComp)) {
			return 0;
		}else {
			return getMinimumHeight(childComp);
		}
	}
	private boolean isResizable(Component comp) {
		if (comp == null) {
			return false;
		}
		if (comp instanceof JComboBox) {
			return false;
		}
		if (comp.getPreferredSize() == null) {
			return false;
		}
		if (comp.getMinimumSize() == null) {
			return false;
		}
		return(getMinimumHeight(comp) < getPreferredHeight(comp));
	}
	private final int calculateHeight(final int parentHeight,final int totalHeight,final int unknownHeightsLeft,final Component childComp) {
		return Math.max((parentHeight - totalHeight) / unknownHeightsLeft,getMinimumHeight(childComp));
	}
	private int getPreferredHeight(final Component comp) {
		return(int) comp.getPreferredSize().getHeight();
	}
	private int getPreferredWidth(final Component comp) {
		return(int) comp.getPreferredSize().getWidth();
	}
	private int getMinimumHeight(final Component comp) {
		return(int) comp.getMinimumSize().getHeight();
	}
	private int getMaximumWidth(final Component comp) {
		return(int) comp.getMaximumSize().getWidth();
	}
	public static Seperator getSeparator() {
		return new Seperator();
	}
	public int getHgap() {
		return this.hgap;
	}
	public void setHgap(int hgap) {
		this.hgap = hgap;
	}
	public int getVgap() {
		return this.vgap;
	}
	public void setVgap(int vgap) {
		this.vgap = vgap;
	}
}

class Seperator extends JPanel {
	private static final long serialVersionUID = -4143634500959911688l;
	Seperator() {
			super.setVisible(false);
		}
}



