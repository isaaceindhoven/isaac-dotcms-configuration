package nl.isaac.comp.configuration.visitor;

import java.util.Stack;

import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.tree.ConfigurationNode;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.tree.ConfigurationNodeVisitor;

public class ToStringSingleLineVisitor implements ConfigurationNodeVisitor {

	private Stack<String> parentStack = new Stack<String>();
	private StringBuffer buf = new StringBuffer();

	public ToStringSingleLineVisitor() {
	}

	public void visitBeforeChildren(ConfigurationNode node) {

		if (node.getName() != null) {

			parentStack.add(node.getName());

			if (node.getChildrenCount() == 0) {
				buf.append(", ");
			}

			buf.append(parentStack.peek());

			if (node.getChildrenCount() == 0) {
				buf.append(" = ").append(node.getValue());
			}
		}
	}

	public void visitAfterChildren(ConfigurationNode node) {
		
		if (!parentStack.isEmpty()) {
			parentStack.pop();
		}
	}

	public boolean terminate() {
		return false;
	}

	@Override
	public String toString() {
		return buf.toString();
	}
}
