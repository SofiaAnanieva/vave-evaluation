package org.argouml.cognitive.ui;

import java.util.ArrayList;
import java.util.List;
import org.argouml.cognitive.Critic;


public class KnowledgeTypeNode {
	private static List<KnowledgeTypeNode>types = null;
	private String name;
	public KnowledgeTypeNode(String n) {
		name = n;
	}
	public static List<KnowledgeTypeNode>getTypeList() {
		if (types == null) {
			types = new ArrayList<KnowledgeTypeNode>();
			types.add(new KnowledgeTypeNode(Critic.KT_DESIGNERS));
			types.add(new KnowledgeTypeNode(Critic.KT_CORRECTNESS));
			types.add(new KnowledgeTypeNode(Critic.KT_COMPLETENESS));
			types.add(new KnowledgeTypeNode(Critic.KT_CONSISTENCY));
			types.add(new KnowledgeTypeNode(Critic.KT_SYNTAX));
			types.add(new KnowledgeTypeNode(Critic.KT_SEMANTICS));
			types.add(new KnowledgeTypeNode(Critic.KT_OPTIMIZATION));
			types.add(new KnowledgeTypeNode(Critic.KT_PRESENTATION));
			types.add(new KnowledgeTypeNode(Critic.KT_ORGANIZATIONAL));
			types.add(new KnowledgeTypeNode(Critic.KT_EXPERIENCIAL));
			types.add(new KnowledgeTypeNode(Critic.KT_TOOL));
		}
		return types;
	}
	public String getName() {
		return name;
	}
	public String toString() {
		return getName();
	}
}



