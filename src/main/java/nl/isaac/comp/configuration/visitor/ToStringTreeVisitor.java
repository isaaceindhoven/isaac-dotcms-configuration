package nl.isaac.comp.configuration.visitor;

import org.apache.commons.configuration.tree.ConfigurationNode;
import org.apache.commons.configuration.tree.ConfigurationNodeVisitor;

public class ToStringTreeVisitor implements ConfigurationNodeVisitor {

	private StringBuffer buf = new StringBuffer();
	private int level = -1;
	private boolean addNewLine = false;
	
	public ToStringTreeVisitor() {
	}
	
	public void visitBeforeChildren(ConfigurationNode node) {
		
		if (node.getName() != null) {

			level++;

			if (addNewLine) {
				buf.append("\n");
				for (int i = 0; i < level; i++) {
					buf.append("\t");
				}
				addNewLine = false;
			}
			
			if (level > 0) {
				buf.append(".");
			}
			
			buf.append(node.getName());

			if (node.getChildrenCount() == 0) {
				buf.append(" = ").append(node.getValue());
				addNewLine = true;
			}
			
			if (node.getChildrenCount() > 1) {
				addNewLine = true;
			}
		}
	}
	
	public void visitAfterChildren(ConfigurationNode node) {
		level--;
	}
	
	public boolean terminate() {
		return false;
	}
	
	@Override
	public String toString() {
		return buf.toString();
	}
}
