package com.scudata.ide.spl.etl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.scudata.chart.Para;
import com.scudata.common.Escape;
import com.scudata.common.MessageManager;
import com.scudata.common.RQException;
import com.scudata.common.Sentence;
import com.scudata.common.StringUtils;
import com.scudata.dm.Sequence;
import com.scudata.ide.common.GC;

/**
 * ���������Ķ���Ԫ�ػ���
 * 
 * cellName.funcName(args...)
 * @author Joancy
 *
 */
public abstract class ObjectElement implements IFuncObject{
	public MessageManager mm = FuncMessage.get();
	public static String SNULL = "_NULL_";
	
	String elementName;
	String cellName = null;//�������֣� ��Ӧ�ں�����ʵ������

	public abstract byte getReturnType();//������������
	public abstract String getFuncName();//������д����
	public abstract String getFuncBody();//��������ʽ
	public abstract String optionString();//����ѡ��
	public void checkEmpty(){};//���Ա������ֵ���
	
	public abstract ParamInfoList getParamInfoList();//�����б�
	public abstract boolean setFuncBody(String funcBody);//������
	
	/**
	 * ���ú�����ѡ��
	 * @param options ѡ��
	 */
	public void setOptions(String options){
		if(options==null){
			return;
		}
		String group = FuncMessage.get().getMessage("options");
		ArrayList<ParamInfo> ofs = getParamInfoList().getParams(group);
		if(ofs==null){
			return;
		}
		for(ParamInfo pi:ofs){
			String fieldName = pi.getName();
			try {
				Field  fieldOption = getClass().getField(fieldName);
//				0��1ѡ���������Ϊ��Ӧ��Ӣ��
				if(fieldName.equals("zero")){
					fieldName = "0";
				}else if(fieldName.equals("one")){
					fieldName = "1";
				}
				Object value = options.indexOf(fieldName)>-1;
				fieldOption.set(this, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * ����������ת��Ϊ���ʽ���ʹ�
	 * @return ���ʽ��
	 */
	public String toExpressionString() {
		StringBuffer sb = new StringBuffer();
		if(getReturnType()==EtlConsts.TYPE_EMPTY){
			sb.append(">");
		}else{
			sb.append("=");
		}
		if(StringUtils.isValidString(cellName)){
			sb.append(cellName);
			sb.append(".");
		}
		sb.append(getFuncName());
		sb.append(getOptions());
		sb.append("(");
		String body = getFuncBody();
		if(StringUtils.isValidString(body)){
			sb.append(body);
		}
		sb.append(")");
		return sb.toString();
	}
	
	/**
	 * ����Ԫ������
	 * 
	 * Ԫ�����ֲ�ͬ�ں�����funcName�� funcName���Բ�Ψһ�� ����Ԫ�����ֱ���Ψһ
	 * ���ڽ��ͬ���������⣻ ���磬 db.query(sql, arg)  db.query(A,sql,arg);
	 * �˴�dbΪcellName�� queryΪfuncName������Ҫ���壻��elementName�����Ϊ  SQLQuery�� SequenceQuery���������ֲ�ͬ�ĺ�����
	 * @param eleName Ԫ������
	 */
	public void setElementName(String eleName){
		this.elementName = eleName;
	}
	/**
	 * ��ȡԪ������
	 * @return ����
	 */
	public String getElementName(){
		return elementName;
	}
	
	/**
	 * ��ȡ������������Ϣ
	 * ����Ϣ�洢����Դ�ļ�����ʾ�ڱ༭����
	 * @return ������Ϣ
	 */
	public String getFuncDesc(){
		return mm.getMessage(elementName+".desc");
	}
	
	/**
	 * ��ȡ�ú����İ�������
	 * @return ���ӵ�ַ
	 */
	public String getHelpUrl(){
		String url = mm.getMessage(elementName+".url");
		if(url.startsWith(elementName)){
			String prefix = "http://doc.raqsoft.com.cn/esproc/func/";
			if(GC.LANGUAGE==GC.ENGLISH){
				prefix = "http://doc.raqsoft.com/esproc/func/";
			}
			return prefix+elementName.toLowerCase()+".html";
		}
		return url;
	}
	
	/**
	 * ���õ�Ԫ������
	 * @param cellName ����
	 */
	public void setCellName(String cellName){
		this.cellName = cellName;
	}
	/**
	 * ��ȡ��Ԫ������
	 * @return
	 */
	public String getCellName(){
		return cellName;
	}
	
	/**
	 * ���ò�����Ϣ�б�
	 * @param paramInfos ������Ϣ�б�
	 */
	public void setParamInfoList(ArrayList<ParamInfo> paramInfos){
		Sequence params = new Sequence();
		for(ParamInfo pi:paramInfos){
			params.add(pi);
		}
		setParams(getClass(),this,params);
	}

	private void setParams(Class elementClass, ObjectElement elementObject,
			Sequence funcParams) {
		int size = funcParams.length();
		for (int i = 1; i <= size; i++) {
			FuncParam fp = (FuncParam) funcParams.get(i);
			Para p = new Para(fp.getValue());
			Field f = null;
			try {
				f = elementClass.getField(fp.getName());
				String className = f.getType().getName().toLowerCase();
				if (className.endsWith("boolean")) {
					f.set(elementObject, new Boolean(p.booleanValue()));
				} else if (className.endsWith("byte")) {
					f.set(elementObject, new Byte((byte) p.intValue()));
				} else if (className.endsWith("int")
						|| className.endsWith("integer")) {
					f.set(elementObject, new Integer(p.intValue()));
				} else if (className.endsWith("float")) {
					f.set(elementObject, new Float(p.floatValue()));
				} else if (className.endsWith("double")) {
					f.set(elementObject, new java.lang.Double(p.doubleValue()));
				} else if (className.endsWith(".color")) {// ���ϵ�Ͳ����chartcolor����color��
					f.set(elementObject, p.colorValue(0));
				} else if (className.endsWith("string")) {
					f.set(elementObject, p.stringValue());
				} else if (className.endsWith("sequence")) {
					f.set(elementObject, p.sequenceValue());
				} else if (className.endsWith("date")) {
					f.set(elementObject, p.dateValue());
				} else if (className.endsWith("chartcolor")) {
					f.set(elementObject, p.chartColorValue());
				} else {
					f.set(elementObject, p.getValue());
				}
			} catch (java.lang.NoSuchFieldException nField) {
			} catch (RQException rqe) {
				throw rqe;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * ������ֵ�б�args���ݷָ���seperatorƴ�ɱ��ʽ��
	 * @param args ����ֵ�б�
	 * @param seperator �ָ���
	 * @return ���ʽ��
	 */
	public static String getStringListExp(ArrayList<String> args,String seperator){
		if(args==null || args.isEmpty()){
			return "";
		}
		StringBuffer options = new StringBuffer();
		for(int i=0;i<args.size();i++){
			if(i>0){
				options.append(seperator);
			}
			options.append(args.get(i));
		}
		return options.toString();
	}
	
	/**
	 * getStringListExp�����������
	 * @param args ���ʽ��
	 * @param seperator �ָ���
	 * @return ����ֵ�б�
	 */
	public static ArrayList<String> getStringList(String args,String seperator){
		if(!isValidString(args)){
			return null;
		}
		ArrayList<String> sl = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(args,seperator);
		while(st.hasMoreTokens()){
			sl.add(st.nextToken());
		}
		return sl;
	}
	
	/**
	 * ��ȡ���ʽƴ����ѡ���ʾ
	 * @return ѡ���ʾ
	 */
	public String getOptions(){
		String options = optionString();
		if(StringUtils.isValidString(options)){
			return "@"+options.toString();
		}else{
			return "";
		}
	}
	
	/**
	 * ������ֵparamValue���ղ�����ʾ����ƴΪSPL���ʽ��
	 * @param paramValue ����ֵ
	 * @return SPL���ʽ��
	 */
	public static String getParamExp(String paramValue){
		if(paramValue==null){
			return "";
		}
		if(paramValue.startsWith("=")){
			return paramValue.substring(1);
		}
		return Escape.addEscAndQuote(paramValue);
	}
	/**
	 * getParamExp�������
	 * @param paramValue SPL���ʽ��
	 * @return ����ֵ���ڴ��ʾ 
	 */
	public static String getParam(String paramValue){
		if(!isValidString(paramValue)){
			return "";
		}
		if(paramValue.startsWith("\"")){
			return Escape.removeEscAndQuote(paramValue);
		}
		return "="+paramValue;
	}
	
	/**
	 * ������ֵparamValue���ձ��ʽ��ʾ����ƴΪSPL���ʽ��
	 * @param paramValue ����ֵ
	 * @return SPL���ʽ��
	 */
	public static String getExpressionExp(String paramValue){
		if(!isValidString(paramValue)){
			return "";
		}
		if(paramValue.startsWith("=")){
			return paramValue.substring(1);
		}
		return paramValue;
	}
	
	/**
	 * getExpressionExp�������
	 * @param paramValue SPL���ʽ��
	 * @return ����ֵ���ڴ��ʾ 
	 */
	public static String getExpression(String paramValue){
		if(!isValidString(paramValue)){
			return "";
		}
		String exp = paramValue;
		int idx = 0;
		int len = exp.length();
		int tmp = Sentence.scanIdentifier( exp, idx );
		if( tmp < len - 1 ) {//��ʶ���ȳ��ȶ�ʱ�����Ǳ��ʽ
			return "="+paramValue;
		}
		return paramValue;
	}
	
	/**
	 * ������ֵparamValue������ֵ��ʾ����ƴΪSPL���ʽ��
	 * @param paramValue ����ֵ
	 * @return SPL���ʽ��
	 */
	public static String getNumberExp(String paramValue){
		if(!isValidString(paramValue)){
			return "";
		}
		if(paramValue.startsWith("=")){
			return paramValue.substring(1);
		}
		return paramValue;
	}
	
	/**
	 * getNumberExp�������
	 * @param paramValue SPL���ʽ��
	 * @return �ڴ����ֵ
	 */
	public static String getNumber(String paramValue){
		if(!isValidString(paramValue)){
			return "";
		}
		try{
			Double d = Double.valueOf(paramValue);
			//�������ֵ��ֱ�ӷ��ص�ǰ���ʽ
			return paramValue;
		}catch(Exception x){
			//���򵱱��ʽ
			return "="+paramValue;
		}
	}

	/**
	 * ������ֵfields���ձ��ʽ��ʾ����ƴΪSPL���ʽ��
	 * @param fields �ֶζ����б�
	 * @return SPL���ʽ��
	 */
	public static String getFieldDefineExp(ArrayList<FieldDefine> fields){
		if(fields==null || fields.isEmpty()){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		if(fields!=null){
			for(FieldDefine fd:fields){
				if(sb.length()>0){
					sb.append(",");
				}
				sb.append(fd.getOne());
				if(StringUtils.isValidString(fd.getTwo())){
					sb.append(":");
					sb.append(fd.getTwo());
				}
				if(StringUtils.isValidString(fd.getThree())){
					sb.append(":");
					sb.append(fd.getThree());
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * getFieldDefineExp�������
	 * �����ֶζ��壬�Ӵ������ֶζ����б�
	 * @param fields SPL���ʽ��
	 * @return �ֶζ����б�
	 */
	public static ArrayList<FieldDefine> getFieldDefine(String fields){
		if(!isValidString(fields)){
			return null;
		}
		ArrayList<FieldDefine> fds = new ArrayList<FieldDefine>();
		StringTokenizer st = new StringTokenizer(fields,",");
		while(st.hasMoreTokens()){
			String section = st.nextToken();
			StringTokenizer token = new StringTokenizer(section,":");
			FieldDefine fd = new FieldDefine();
			fd.setOne(token.nextToken());
			if(token.hasMoreTokens()){
				fd.setTwo(token.nextToken());	
			}
			if(token.hasMoreTokens()){
				fd.setThree(token.nextToken());	
			}
			fds.add(fd);
		}
		return fds;
	}
	
	/**
	 * ������ֵfields���ձ��ʽ��ʾ����2ƴΪSPL���ʽ��
	 * ��FieldDefine�������ɹ���2��û���ֶ���ʱ��ȱʡʹ�ñ��ʽ����  A:A
	 * @param fields �ֶζ����б�
	 * @return SPL���ʽ��
	 */
	public static String getFieldDefineExp2(ArrayList<FieldDefine> fields){
		if(fields==null || fields.isEmpty()){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		if(fields!=null){
			for(FieldDefine fd:fields){
				if(sb.length()>0){
					sb.append(",");
				}
				sb.append(fd.getOne());
				sb.append(":");
				if(StringUtils.isValidString(fd.getTwo())){
					sb.append(fd.getTwo());
				}else{
					sb.append(fd.getOne());
				}
			}
		}
		return sb.toString();
	}

	/**
	 * getFieldDefineExp2�������
	 * �����ֶζ��壬�Ӵ������ֶζ����б�
	 * @param fields SPL���ʽ��
	 * @return �ֶζ����б�
	 */
	public static ArrayList<FieldDefine> getFieldDefine2(String fields){
		if(!StringUtils.isValidString(fields)){
			return null;
		}
		ArrayList<FieldDefine> fds = new ArrayList<FieldDefine>();
		StringTokenizer st = new StringTokenizer(fields,",");
		while( st.hasMoreTokens() ){
			String tmp = st.nextToken();
			StringTokenizer tmpST = new StringTokenizer(tmp,":");
			String one = tmpST.nextToken();
			String two = tmpST.nextToken();
			FieldDefine fd = new FieldDefine();
			fd.setOne(one);
			if(!one.equals(two)){
				fd.setTwo(two);
			}
			fds.add(fd);
		}
		return fds;
	}
	
	/**
	 * �жϲ���ֵ�Ƿ�Ϊ���ʽд��
	 * @param paramValue ����ֵ
	 * @return ���ʽд��ʱ����true�����򷵻�false
	 */
	public static boolean isExpression(String paramValue){
		if(paramValue==null){
			return false;
		}
		return paramValue.startsWith("=");
	}
	
	/**
	 * ��������ʡ���м�ʱ������������ķָ��������硰A;;B��, ��ʱ��2�ڣ���Tokenizer��������
	 * Ϊ�˱�������������˴�����Щ�ָ����м���� NULL ����
	 * @param param ����ֵ
	 * @return У����Ĳ���ֵ
	 */
	private static String verifyParam(String param){
		if(param.startsWith(";")){
			param = SNULL+param;
		}
		param = Sentence.replace(param, ";;", ";"+SNULL+";", 0);
		param = Sentence.replace(param, ";;", ";"+SNULL+";", 0);
		param = Sentence.replace(param, ";,", ";"+SNULL+",", 0);
		param = Sentence.replace(param, ";:", ";"+SNULL+":", 0);
		param = Sentence.replace(param, ",:", ","+SNULL+":", 0);
		return param;
	}
	
	/**
	 * �����ִ�ʱ��ʹ����NULL�ַ�����
	 * @param str
	 * @return
	 */
	public static boolean isValidString(String str){
		if(!StringUtils.isValidString(str)){
			return false;
		}
		return !str.equals(SNULL);
	}
	
	/**
	 * ���ʽ������Ϊ��������
	 * @param exp ���ʽ
	 * @param oes ���б༭�õĵ�Ԫ��������ӳ���
	 * @return ����Ԫ��
	 */
	public static ObjectElement parseString(String exp, HashMap<String, ObjectElement> oes){
		if(!isValidString(exp)){
			return null;
		}
		exp = exp.trim();
		if(!(exp.startsWith("=") || exp.startsWith(">"))){
			return null;
		}
//		=file@s("d:/1.txt")  =A1.create@o(arg1)
		int index = exp.indexOf("(");
		if(index<0){
			return null;
		}
		int iTmp = Sentence.scanParenthesis(exp, index);
		if (iTmp < 0) {
			return null;
		} else if(iTmp<(exp.length()-1)){//��֧�ֶ༶������д A1.import().sort()
			return null;
		}

		String celName = null;//������������ A1
		String funName = null;//������ create
		String options = null;//ѡ�� o
		String funBody = null;//������ arg1

		String tmp = exp.substring(1,index);
		int dot1 = tmp.indexOf(".");
		if(dot1>0){
			celName = tmp.substring(0,dot1);
			tmp = tmp.substring(dot1+1);
		}
		int indexOption = tmp.indexOf("@");
		if(indexOption>0){
			funName = tmp.substring(0,indexOption);
			tmp = tmp.substring(indexOption+1);
		}else{
			funName = tmp;
			tmp = null;
		}
		options = tmp;
		funBody = exp.substring(index+1,exp.length()-1);
		ArrayList<ElementInfo> funObjs = ElementLib.getElementInfos(funName);
		if(funObjs.isEmpty()){
			return null;
		}
		ElementInfo ei=null;
		if( funObjs.size()==1 ){
			ei = funObjs.get(0);
		}else{
			ObjectElement parent = oes.get(celName);
			byte parentType;
			if(parent==null){
				parentType = EtlConsts.TYPE_EMPTY;
			}else{
				parentType = parent.getReturnType();
			}
			int c = 0;
			ArrayList<ElementInfo> tmpFunObjs = new ArrayList<ElementInfo>();
			for(ElementInfo tmpEI:funObjs){
				ObjectElement tmpOE = tmpEI.newInstance();
				if(tmpOE.getParentType()==parentType){
					ei = tmpEI;
					c++;
					tmpFunObjs.add(ei);
					if(!StringUtils.isValidString(funBody)){
						break;
					}
				}
			}
			if(c>1){
				for(ElementInfo tmpEI:tmpFunObjs){
					ObjectElement tmpOE = tmpEI.newInstance();
					String tmps = verifyParam(funBody);
					if(tmpOE.setFuncBody(tmps)){
						ei = tmpEI;
						break;
					}
				}
			}
		}
		if(ei==null){
			return null;
		}
		ObjectElement oe = ei.newInstance();
		oe.setCellName(celName);
		oe.setOptions(options);
		if(StringUtils.isValidString(funBody)){
			funBody = verifyParam(funBody);
			oe.setFuncBody(funBody);
		}
		return oe;
	}
	
}
