package org.argouml.profile.internal.ocl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.argouml.profile.internal.ocl.uml14.Bag;
import org.argouml.profile.internal.ocl.uml14.HashBag;
import org.argouml.profile.internal.ocl.uml14.OclEnumLiteral;
import tudresden.ocl.parser.analysis.DepthFirstAdapter;
import tudresden.ocl.parser.node.AActualParameterList;
import tudresden.ocl.parser.node.AAdditiveExpressionTail;
import tudresden.ocl.parser.node.AAndLogicalOperator;
import tudresden.ocl.parser.node.ABooleanLiteral;
import tudresden.ocl.parser.node.ADeclaratorTail;
import tudresden.ocl.parser.node.ADivMultiplyOperator;
import tudresden.ocl.parser.node.AEmptyFeatureCallParameters;
import tudresden.ocl.parser.node.AEnumLiteral;
import tudresden.ocl.parser.node.AEqualRelationalOperator;
import tudresden.ocl.parser.node.AExpressionListOrRange;
import tudresden.ocl.parser.node.AFeatureCall;
import tudresden.ocl.parser.node.AFeatureCallParameters;
import tudresden.ocl.parser.node.AFeaturePrimaryExpression;
import tudresden.ocl.parser.node.AGtRelationalOperator;
import tudresden.ocl.parser.node.AGteqRelationalOperator;
import tudresden.ocl.parser.node.AIfExpression;
import tudresden.ocl.parser.node.AImpliesLogicalOperator;
import tudresden.ocl.parser.node.AIntegerLiteral;
import tudresden.ocl.parser.node.AIterateDeclarator;
import tudresden.ocl.parser.node.ALetExpression;
import tudresden.ocl.parser.node.AListExpressionListOrRangeTail;
import tudresden.ocl.parser.node.ALiteralCollection;
import tudresden.ocl.parser.node.ALogicalExpressionTail;
import tudresden.ocl.parser.node.ALtRelationalOperator;
import tudresden.ocl.parser.node.ALteqRelationalOperator;
import tudresden.ocl.parser.node.AMinusAddOperator;
import tudresden.ocl.parser.node.AMinusUnaryOperator;
import tudresden.ocl.parser.node.AMultMultiplyOperator;
import tudresden.ocl.parser.node.AMultiplicativeExpressionTail;
import tudresden.ocl.parser.node.ANEqualRelationalOperator;
import tudresden.ocl.parser.node.ANotUnaryOperator;
import tudresden.ocl.parser.node.AOrLogicalOperator;
import tudresden.ocl.parser.node.APlusAddOperator;
import tudresden.ocl.parser.node.APostfixExpressionTail;
import tudresden.ocl.parser.node.ARealLiteral;
import tudresden.ocl.parser.node.ARelationalExpressionTail;
import tudresden.ocl.parser.node.AStandardDeclarator;
import tudresden.ocl.parser.node.AStringLiteral;
import tudresden.ocl.parser.node.AUnaryUnaryExpression;
import tudresden.ocl.parser.node.AXorLogicalOperator;
import tudresden.ocl.parser.node.PActualParameterListTail;
import tudresden.ocl.parser.node.PDeclaratorTail;
import tudresden.ocl.parser.node.PExpression;
import tudresden.ocl.parser.node.PExpressionListTail;


public class EvaluateExpression extends DepthFirstAdapter {
	private Map<String,Object>vt = null;
	private Object val = null;
	private Object fwd = null;
	private ModelInterpreter interp = null;
	public EvaluateExpression(Object modelElement,ModelInterpreter mi) {
		reset(modelElement,mi);
	}
	public EvaluateExpression(Map<String,Object>variableTable,ModelInterpreter modelInterpreter) {
		reset(variableTable,modelInterpreter);
	}
	public void reset(Object element,ModelInterpreter mi) {
		vt = new HashMap<String,Object>();
		vt.put("self",element);
		reset(vt,mi);
	}
	public void reset(Map<String,Object>newVT,ModelInterpreter mi) {
		this.interp = mi;
		this.val = null;
		this.fwd = null;
		this.vt = newVT;
	}
	public Object getValue() {
		return val;
	}
	public void caseAIfExpression(AIfExpression node) {
		boolean test = false;
		boolean ret = false;
		inAIfExpression(node);
		if (node.getTIf() != null) {
			node.getTIf().apply(this);
		}
		if (node.getIfBranch() != null) {
			node.getIfBranch().apply(this);
			test = asBoolean(val,node.getIfBranch());
			val = null;
		}
		if (node.getTThen() != null) {
			node.getTThen().apply(this);
		}
		if (node.getThenBranch() != null) {
			node.getThenBranch().apply(this);
			if (test) {
				ret = asBoolean(val,node.getThenBranch());
				val = null;
			}
		}
		if (node.getTElse() != null) {
			node.getTElse().apply(this);
		}
		if (node.getElseBranch() != null) {
			node.getElseBranch().apply(this);
			if (!test) {
				ret = asBoolean(val,node.getThenBranch());
				val = null;
			}
		}
		if (node.getEndif() != null) {
			node.getEndif().apply(this);
		}
		val = ret;
		outAIfExpression(node);
	}
	public void caseALogicalExpressionTail(ALogicalExpressionTail node) {
		Object left = val;
		val = null;
		inALogicalExpressionTail(node);
		if (node.getLogicalOperator() != null) {
			node.getLogicalOperator().apply(this);
		}
		if (node.getRelationalExpression() != null) {
			node.getRelationalExpression().apply(this);
		}
		Object op = node.getLogicalOperator();
		Object right = val;
		val = null;
		if (op != null) {
			if (op instanceof AAndLogicalOperator) {
				if (left != null&&left instanceof Boolean&&((Boolean) left).booleanValue() == false) {
					val = false;
				}else if (right != null&&right instanceof Boolean&&((Boolean) right).booleanValue() == false) {
					val = false;
				}else {
					val = asBoolean(left,node)&&asBoolean(right,node);
				}
			}else if (op instanceof AImpliesLogicalOperator) {
				val = !asBoolean(left,node)||asBoolean(right,node);
			}else if (op instanceof AOrLogicalOperator) {
				if (left != null&&left instanceof Boolean&&((Boolean) left).booleanValue() == true) {
					val = true;
				}else if (right != null&&right instanceof Boolean&&((Boolean) right).booleanValue() == true) {
					val = true;
				}else {
					val = asBoolean(left,node)||asBoolean(right,node);
				}
			}else if (op instanceof AXorLogicalOperator) {
				val = !asBoolean(left,node)^asBoolean(right,node);
			}else {
				error(node);
			}
		}else {
			error(node);
		}
		outALogicalExpressionTail(node);
	}
	public void caseARelationalExpressionTail(ARelationalExpressionTail node) {
		Object left = val;
		val = null;
		inARelationalExpressionTail(node);
		if (node.getRelationalOperator() != null) {
			node.getRelationalOperator().apply(this);
		}
		if (node.getAdditiveExpression() != null) {
			node.getAdditiveExpression().apply(this);
		}
		Object op = node.getRelationalOperator();
		Object right = val;
		val = null;
		if (left != null&&op != null&&right != null) {
			if (op instanceof AEqualRelationalOperator) {
				val = left.equals(right);
			}else if (op instanceof AGteqRelationalOperator) {
				val = asInteger(left,node) >= asInteger(right,node);
			}else if (op instanceof AGtRelationalOperator) {
				val = asInteger(left,node) > asInteger(right,node);
			}else if (op instanceof ALteqRelationalOperator) {
				val = asInteger(left,node) <= asInteger(right,node);
			}else if (op instanceof ALtRelationalOperator) {
				val = asInteger(left,node) < asInteger(right,node);
			}else if (op instanceof ANEqualRelationalOperator) {
				val = !left.equals(right);
			}else {
				error(node);
			}
		}else {
			if (op instanceof AEqualRelationalOperator) {
				val = (left == right);
			}else if (op instanceof ANEqualRelationalOperator) {
				val = (left != right);
			}else {
				error(node);
				val = null;
			}
		}
		outARelationalExpressionTail(node);
	}
	@Override public void caseAAdditiveExpressionTail(AAdditiveExpressionTail node) {
		Object left = val;
		val = null;
		inAAdditiveExpressionTail(node);
		if (node.getAddOperator() != null) {
			node.getAddOperator().apply(this);
		}
		if (node.getMultiplicativeExpression() != null) {
			node.getMultiplicativeExpression().apply(this);
		}
		Object op = node.getAddOperator();
		Object right = val;
		val = null;
		if (left != null&&op != null&&right != null) {
			if (op instanceof AMinusAddOperator) {
				val = asInteger(left,node) - asInteger(right,node);
			}else if (op instanceof APlusAddOperator) {
				val = asInteger(left,node) + asInteger(right,node);
			}else {
				error(node);
			}
		}else {
			error(node);
		}
		outAAdditiveExpressionTail(node);
	}
	public void caseAMultiplicativeExpressionTail(AMultiplicativeExpressionTail node) {
		Object left = val;
		val = null;
		inAMultiplicativeExpressionTail(node);
		if (node.getMultiplyOperator() != null) {
			node.getMultiplyOperator().apply(this);
		}
		if (node.getUnaryExpression() != null) {
			node.getUnaryExpression().apply(this);
		}
		Object op = node.getMultiplyOperator();
		Object right = val;
		val = null;
		if (left != null&&op != null&&right != null) {
			if (op instanceof ADivMultiplyOperator) {
				val = asInteger(left,node) / asInteger(right,node);
			}else if (op instanceof AMultMultiplyOperator) {
				val = asInteger(left,node) * asInteger(right,node);
			}else {
				error(node);
			}
		}else {
			error(node);
		}
		outAMultiplicativeExpressionTail(node);
	}
	public void caseAUnaryUnaryExpression(AUnaryUnaryExpression node) {
		inAUnaryUnaryExpression(node);
		if (node.getUnaryOperator() != null) {
			node.getUnaryOperator().apply(this);
		}
		if (node.getPostfixExpression() != null) {
			val = null;
			node.getPostfixExpression().apply(this);
		}
		Object op = node.getUnaryOperator();
		if (op instanceof AMinusUnaryOperator) {
			val = -asInteger(val,node);
		}else if (op instanceof ANotUnaryOperator) {
			val = !asBoolean(val,node);
		}
		outAUnaryUnaryExpression(node);
	}
	public void caseAPostfixExpressionTail(APostfixExpressionTail node) {
		inAPostfixExpressionTail(node);
		if (node.getPostfixExpressionTailBegin() != null) {
			node.getPostfixExpressionTailBegin().apply(this);
		}
		if (node.getFeatureCall() != null) {
			fwd = node.getPostfixExpressionTailBegin();
			node.getFeatureCall().apply(this);
		}
		outAPostfixExpressionTail(node);
	}
	@Override public void caseAFeaturePrimaryExpression(AFeaturePrimaryExpression node) {
		Object subject = val;
		Object feature = null;
		List parameters = null;
		inAFeaturePrimaryExpression(node);
		if (node.getPathName() != null) {
			node.getPathName().apply(this);
			feature = node.getPathName().toString().trim();
		}
		if (node.getTimeExpression() != null) {
			node.getTimeExpression().apply(this);
		}
		if (node.getQualifiers() != null) {
			node.getQualifiers().apply(this);
		}
		if (node.getFeatureCallParameters() != null) {
			val = null;
			node.getFeatureCallParameters().apply(this);
			parameters = (List) val;
		}
		if (subject == null) {
			val = vt.get(feature);
			if (val == null) {
				val = this.interp.getBuiltInSymbol(feature.toString().trim());
			}
		}else {
			val = runFeatureCall(subject,feature,fwd,parameters);
		}
		outAFeaturePrimaryExpression(node);
	}
	@Override public void outAEmptyFeatureCallParameters(AEmptyFeatureCallParameters node) {
		val = new ArrayList();
		defaultOut(node);
	}
	@SuppressWarnings("unchecked")@Override public void caseAFeatureCallParameters(AFeatureCallParameters node) {
		inAFeatureCallParameters(node);
		if (node.getLPar() != null) {
			node.getLPar().apply(this);
		}
		boolean hasDeclarator = false;
		if (node.getDeclarator() != null) {
			node.getDeclarator().apply(this);
			hasDeclarator = true;
		}
		if (node.getActualParameterList() != null) {
			List<String>vars = null;
			if (hasDeclarator) {
				List ret = new ArrayList();
				vars = (List) val;
				final PExpression exp = ((AActualParameterList) node.getActualParameterList()).getExpression();
				ret.add(vars);
				ret.add(exp);
				ret.add(new LambdaEvaluator() {
					public Object evaluate(Map<String,Object>vti,Object expi) {
						Object state = EvaluateExpression.this.saveState();
						EvaluateExpression.this.vt = vti;
						EvaluateExpression.this.val = null;
						EvaluateExpression.this.fwd = null;
						((PExpression) expi).apply(EvaluateExpression.this);
						Object reti = EvaluateExpression.this.val;
						EvaluateExpression.this.loadState(state);
						return reti;
					}
				});
				val = ret;
			}else {
				node.getActualParameterList().apply(this);
			}
		}
		if (node.getRPar() != null) {
			node.getRPar().apply(this);
		}
		outAFeatureCallParameters(node);
	}
	@SuppressWarnings("unchecked")private void loadState(Object state) {
		Object[]stateArr = (Object[]) state;
		this.vt = (Map<String,Object>) stateArr[0];
		this.val = stateArr[1];
		this.fwd = stateArr[2];
	}
	private Object saveState() {
		return new Object[] {vt,val,fwd};
	}
	@Override public void caseAStandardDeclarator(AStandardDeclarator node) {
		inAStandardDeclarator(node);
		List<String>vars = new ArrayList<String>();
		if (node.getName() != null) {
			node.getName().apply(this);
			vars.add(node.getName().toString().trim());
		}
		 {
			Object temp[] = node.getDeclaratorTail().toArray();
			for (int i = 0;i < temp.;i++) {
				((PDeclaratorTail) temp[i]).apply(this);
				vars.add(((ADeclaratorTail) temp[i]).getName().toString().trim());
			}
			val = vars;
		}
		if (node.getDeclaratorTypeDeclaration() != null) {
			node.getDeclaratorTypeDeclaration().apply(this);
		}
		if (node.getBar() != null) {
			node.getBar().apply(this);
		}
		outAStandardDeclarator(node);
	}
	@Override public void outAIterateDeclarator(AIterateDeclarator node) {
		val = new ArrayList<String>();
		defaultOut(node);
	}
	@Override public void caseALetExpression(ALetExpression node) {
		Object name = null;
		Object value = null;
		inALetExpression(node);
		if (node.getTLet() != null) {
			node.getTLet().apply(this);
		}
		if (node.getName() != null) {
			node.getName().apply(this);
			name = node.getName().toString().trim();
		}
		if (node.getLetExpressionTypeDeclaration() != null) {
			node.getLetExpressionTypeDeclaration().apply(this);
		}
		if (node.getEqual() != null) {
			node.getEqual().apply(this);
		}
		if (node.getExpression() != null) {
			node.getExpression().apply(this);
			value = val;
		}
		if (node.getTIn() != null) {
			node.getTIn().apply(this);
		}
		vt.put(("" + name).trim(),value);
		val = null;
		outALetExpression(node);
	}
	public void outAStringLiteral(AStringLiteral node) {
		String text = node.getStringLit().getText();
		val = text.substring(1,text.length() - 1);
		defaultOut(node);
	}
	public void outARealLiteral(ARealLiteral node) {
		val = (int) Double.parseDouble(node.getReal().getText());
		defaultOut(node);
	}
	public void outAIntegerLiteral(AIntegerLiteral node) {
		val = Integer.parseInt(node.getInt().getText());
		defaultOut(node);
	}
	public void outABooleanLiteral(ABooleanLiteral node) {
		val = Boolean.parseBoolean(node.getBool().getText());
		defaultOut(node);
	}
	public void outAEnumLiteral(AEnumLiteral node) {
		val = new OclEnumLiteral(node.getName().toString().trim());
		defaultOut(node);
	}
	@SuppressWarnings("unchecked")public void caseALiteralCollection(ALiteralCollection node) {
		Collection<Object>col = null;
		inALiteralCollection(node);
		if (node.getCollectionKind() != null) {
			node.getCollectionKind().apply(this);
			String kind = node.getCollectionKind().toString().trim();
			if (kind.equalsIgnoreCase("Set")) {
				col = new HashSet<Object>();
			}else if (kind.equalsIgnoreCase("Sequence")) {
				col = new ArrayList<Object>();
			}else if (kind.equalsIgnoreCase("Bag")) {
				col = new HashBag<Object>();
			}
		}
		if (node.getLBrace() != null) {
			node.getLBrace().apply(this);
		}
		if (node.getExpressionListOrRange() != null) {
			val = null;
			node.getExpressionListOrRange().apply(this);
			col.addAll((Collection<Object>) val);
		}
		if (node.getRBrace() != null) {
			node.getRBrace().apply(this);
		}
		val = col;
		outALiteralCollection(node);
	}
	@Override public void caseAExpressionListOrRange(AExpressionListOrRange node) {
		List ret = new ArrayList();
		inAExpressionListOrRange(node);
		if (node.getExpression() != null) {
			val = null;
			node.getExpression().apply(this);
			ret.add(val);
		}
		if (node.getExpressionListOrRangeTail() != null) {
			val = null;
			node.getExpressionListOrRangeTail().apply(this);
			ret.addAll((Collection) val);
		}
		val = ret;
		outAExpressionListOrRange(node);
	}
	@Override public void caseAListExpressionListOrRangeTail(AListExpressionListOrRangeTail node) {
		inAListExpressionListOrRangeTail(node);
		 {
			List ret = new ArrayList();
			Object temp[] = node.getExpressionListTail().toArray();
			for (int i = 0;i < temp.;i++) {
				val = null;
				((PExpressionListTail) temp[i]).apply(this);
				ret.add(val);
			}
			val = ret;
		}
		outAListExpressionListOrRangeTail(node);
	}
	@Override public void caseAFeatureCall(AFeatureCall node) {
		Object subject = val;
		Object feature = null;
		Object type = fwd;
		List parameters = null;
		inAFeatureCall(node);
		if (node.getPathName() != null) {
			node.getPathName().apply(this);
			feature = node.getPathName().toString().trim();
		}
		if (node.getTimeExpression() != null) {
			node.getTimeExpression().apply(this);
		}
		if (node.getQualifiers() != null) {
			node.getQualifiers().apply(this);
		}
		if (node.getFeatureCallParameters() != null) {
			val = null;
			node.getFeatureCallParameters().apply(this);
			parameters = (List) val;
		}else {
			parameters = new ArrayList();
		}
		val = runFeatureCall(subject,feature,type,parameters);
		outAFeatureCall(node);
	}
	@Override public void caseAActualParameterList(AActualParameterList node) {
		List list = new ArrayList();
		inAActualParameterList(node);
		if (node.getExpression() != null) {
			val = null;
			node.getExpression().apply(this);
			list.add(val);
		}
		 {
			Object temp[] = node.getActualParameterListTail().toArray();
			for (int i = 0;i < temp.;i++) {
				val = null;
				((PActualParameterListTail) temp[i]).apply(this);
				list.add(val);
			}
		}
		val = list;
		outAActualParameterList(node);
	}
	private boolean asBoolean(Object value,Object node) {
		if (value instanceof Boolean) {
			return(Boolean) value;
		}else {
			errorNotType(node,"Boolean",false);
			return false;
		}
	}
	private int asInteger(Object value,Object node) {
		if (value instanceof Integer) {
			return(Integer) value;
		}else {
			errorNotType(node,"integer",0);
			return 0;
		}
	}
	private Object runFeatureCall(Object subject,Object feature,Object type,List parameters) {
		if (parameters == null) {
			parameters = new ArrayList<Object>();
		}
		if ((subject instanceof Collection)&&type.toString().trim().equals(".")) {
			Collection col = (Collection) subject;
			Bag res = new HashBag();
			for (Object obj:col) {
				res.add(interp.invokeFeature(vt,obj,feature.toString().trim(),".",parameters.toArray()));
			}
			return res;
		}else {
			return interp.invokeFeature(vt,subject,feature.toString().trim(),type.toString().trim(),parameters.toArray());
		}
	}
	private void errorNotType(Object node,String type,Object dft) {
		val = dft;
		throw new RuntimeException();
	}
	private void error(Object node) {
		val = null;
		throw new RuntimeException();
	}
}



