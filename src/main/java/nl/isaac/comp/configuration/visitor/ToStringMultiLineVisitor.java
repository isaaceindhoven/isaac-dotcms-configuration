package nl.isaac.comp.configuration.visitor;

import java.util.Stack;

import org.apache.commons.configuration.tree.ConfigurationNode;
import org.apache.commons.configuration.tree.ConfigurationNodeVisitor;

public class ToStringMultiLineVisitor implements ConfigurationNodeVisitor {

	private Stack<String> parentStack = new Stack<String>();
	private StringBuffer buf = new StringBuffer();

	public ToStringMultiLineVisitor() {
	}

	public void visitBeforeChildren(ConfigurationNode node) {

		if (node.getName() != null) {

			parentStack.add(node.getName());

			if (node.getChildrenCount() == 0) {
				if (buf.length() > 0) {
					buf.append("\n");
				}

				for (int i = 0; i < parentStack.size() - 1; i++) {
					buf.append(parentStack.get(i)).append(".");
				}

				buf.append(parentStack.peek());

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
