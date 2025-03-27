package com.scudata.ide.vdb.config;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class ConfigOptionsWriter {
	private TransformerHandler handler = null;
	// private String fileName;
	// private AttributesImpl atts;
	// Ԫ�ز�Σ����ڿ���XML����
	private int level = 0;
	// ÿ����θ�������4���ո񣬼�һ��tab
	private final String tab = "    ";
	// ϵͳ���з���WindowsΪ��"\n"��Linux/UnixΪ��"/n"
	private final String separator = System.getProperties().getProperty("os.name").toUpperCase()
			.indexOf("WINDOWS") != -1 ? "\n" : "/n";

	public ConfigOptionsWriter() {
		try {
			SAXTransformerFactory fac = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
			handler = fac.newTransformerHandler();
			Transformer transformer = handler.getTransformer();
			// ����������õı��뷽ʽ
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			// �Ƿ��Զ���Ӷ���Ŀհ�
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			// �Ƿ����xml����
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			// outStream = new FileOutputStream(fileName);

			// level = 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Ԫ�������Ƕ���ӽڵ㣬���Ԫ�صĿ�ʼ�ͽ����ֿ�д
	// �磺<a><b>bcd</b></a>
	private void startElement(String objectElement, AttributesImpl attrs) throws SAXException {
		if (attrs == null) {
			attrs = new AttributesImpl();
		}
		// level++;
		appendTab();
		if (objectElement != null) {
			// ע�⣬���atts.addAttribute���������ԣ��������磺<a key="key"
			// value="value">abc</a>��ʽ
			// ���û���������ԣ�������磺<a>abc</a>��ʽ
			handler.startElement("", "", objectElement, attrs);
		}
	}

	// ����Ԫ�ؽ�����ǣ��磺</a>
	private void endElement(String objectElement) throws SAXException {
		// level--;
		appendTab();
		if (objectElement != null) {
			handler.endElement("", "", objectElement);
		}
	}

	// �Է�յĿ�Ԫ�أ���<a key="key" value="value"/>�����û��У�д��һ��ʱXML�Զ����Է��
	private void endEmptyElement(String objectElement) throws SAXException {
		// level--;
		handler.endElement("", "", objectElement);
	}

	// ���ӽڵ��Ԫ�س�Ϊ���ԣ���<a>abc</a>
	private void writeAttribute(String key, String value) throws SAXException {
		if (value == null)
			value = "";
		appendTab();
		handler.startElement("", "", key, new AttributesImpl());
		handler.characters(value.toCharArray(), 0, value.length());
		handler.endElement("", "", key);
		// level--;
	}

	// Tab������SAXĬ�ϲ��Զ������������Ҫ�ֶ�����Ԫ�ز�ν�����������
	private void appendTab() throws SAXException {
		StringBuffer sb = new StringBuffer(separator);
		for (int i = 0; i < level; i++) {
			sb.append(tab);
		}
		String indent = sb.toString();
		handler.characters(indent.toCharArray(), 0, indent.length());
	}

	/**
	 * ������Դ�ͻ���д���ļ�
	 * 
	 * @param dslm
	 * @throws SAXException
	 * @throws IOException
	 */
	public void write(String filePath) throws Exception {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			File f = new File(filePath);
			if(!f.exists()){
				File p = f.getParentFile();
				p.mkdirs();
			}
			fos = new FileOutputStream(filePath);
			bos = new BufferedOutputStream(fos);
			write(bos);
			bos.flush();
		} finally {
			if (fos != null)
				fos.close();
			if (bos != null)
				bos.close();
		}
	}

	public void write(OutputStream out) throws Exception {
		Result resultxml = new StreamResult(out);
		handler.setResult(resultxml);
		level = 0;
		handler.startDocument();
		// ���ø��ڵ�Ͱ汾
		handler.startElement("", "", ConfigFile.ROOT, getAttributesImpl(new String[] { ConfigFile.VERSION, "1" }));
		// ѡ��
		level = 1;
		startElement(ConfigFile.OPTIONS, null);
		Map<String, Object> options = ConfigOptions.options;
		Iterator<String> it = options.keySet().iterator();
		String key;
		while (it.hasNext()) {
			key = it.next();
			level = 2;
			startElement(ConfigFile.OPTION, getAttributesImpl(
					new String[] { ConfigFile.NAME, key, ConfigFile.VALUE, object2String(options.get(key)) }));
			endEmptyElement(ConfigFile.OPTION);
		}
		level = 1;
		endElement(ConfigFile.OPTIONS);
		
		// ����λ�ô�С
		startElement(ConfigFile.DIMENSIONS, null);
		Map<String, String> dimensions = ConfigOptions.dimensions;
		it = dimensions.keySet().iterator();
		while (it.hasNext()) {
			key = it.next();
			level = 2;
			startElement(ConfigFile.DIMENSION,
					getAttributesImpl(new String[] { ConfigFile.NAME, key, ConfigFile.VALUE, dimensions.get(key) }));
			endEmptyElement(ConfigFile.DIMENSION);
		}
		level = 1;
		endElement(ConfigFile.DIMENSIONS);
		
		
		// ������Ϣ
		startElement(ConfigFile.CONNECTIONS, null);
		Map<String, String> connections = ConfigOptions.connections;
		it = connections.keySet().iterator();
		while (it.hasNext()) {
			key = it.next();
			level = 2;
			startElement(ConfigFile.CONNECTION,
					getAttributesImpl(new String[] { ConfigFile.NAME, key, ConfigFile.VALUE, connections.get(key) }));
			endEmptyElement(ConfigFile.CONNECTION);
		}
		level = 1;
		endElement(ConfigFile.CONNECTIONS);

		level = 0;
		handler.endElement("", "", ConfigFile.ROOT);
		// �ĵ�����,ͬ��������
		handler.endDocument();
	}

	private String object2String(Object val) {
		if (val == null)
			return "";
		return val.toString();
	}

	private AttributesImpl getAttributesImpl(String[] attrs) {
		AttributesImpl attrImpl = new AttributesImpl();
		int size = attrs.length;
		for (int i = 0; i < size; i += 2) {
			if (attrs[i + 1] != null) // �սڵ㲻д��
				attrImpl.addAttribute("", "", attrs[i], String.class.getName(), String.valueOf(attrs[i + 1]));
		}
		return attrImpl;
	}

}