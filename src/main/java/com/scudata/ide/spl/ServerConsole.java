package com.scudata.ide.spl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.UIManager;

import com.scudata.app.common.Section;
import com.scudata.app.config.ConfigUtil;
import com.scudata.app.config.RaqsoftConfig;
import com.scudata.common.Escape;
import com.scudata.common.Logger;
import com.scudata.common.SplServerConfig;
import com.scudata.common.StringUtils;
import com.scudata.parallel.UnitClient;
import com.scudata.parallel.UnitContext;
import com.scudata.server.IServer;
import com.scudata.server.http.SplxServerInIDE;
import com.scudata.server.http.HttpContext;
import com.scudata.server.odbc.DataTypes;
import com.scudata.server.odbc.OdbcContext;
import com.scudata.server.odbc.OdbcServer;
import com.scudata.server.unit.ShutdownUnitServer;
import com.scudata.server.unit.UnitServer;

/**
 * ��������������ֹͣ���ַ���ѡ��[a,x]ֻ����һ�����������ϣ���ѡ��Ķ���
 * ������ͼ�ο���̨ һ��ѡ��Ҳû��ʱ���������������̨����
 * UnitServerConsole //java ServerConsole -a 
 * -p[ip:port] �����ڵ������� ��ʡ��ip:portʱ���Զ�˳��Ѱ��һ��û��ռ�õ����ã�
 * -o����odbc 
 * -h ����http 
 * -x[ip:port] ָֹͣ����������ʡ��ip:port��ֹͣ�������з���
 * -a �������з���
 */
public class ServerConsole {
	private static ArrayList<Object> runningServers = new ArrayList<Object>();

	public static ArrayList<Object> getRunningServers(){
		return runningServers;
	}
	/**
	 * �ڵ���ļ�Ĭ��ΪconfigĿ¼�£�������·����Ȼ����start.home�µľ���·��
	 * 
	 * @param configFile �����ļ���
	 * @throws Exception
	 * @return InputStream ��Ӧ��������
	 */
	public static InputStream getConfigIS(String configFile) throws Exception {
		return UnitContext.getUnitInputStream(configFile);
	}

	/**
	 * �����б��еķ������Ƿ����������еķ�����
	 * @param servers �������б�
	 * @return ֻҪ�������еķ������ͷ���true�����򷵻�false
	 */
	public static boolean isRunning(List<IServer> servers) {
		for (IServer server : servers) {
			if (server != null && server.isRunning())
				return true;
		}
		return false;
	}
	
	private static void initLang(boolean init){
		try{
			if( init ) {
				loadRaqsoftConfig();
			}
		}catch(Exception x){}
	}

	/**
	 * װ����Ǭ�����ļ�
	 * @return �����ļ�����
	 * @throws Exception
	 */
	private static RaqsoftConfig rc = null;
	public static RaqsoftConfig loadRaqsoftConfig() throws Exception {
		if(rc==null) {
			InputStream inputStream = getConfigIS("raqsoftConfig.xml");
			rc = ConfigUtil.load(inputStream,true);
			inputStream.close();
		}
		return rc;
	}

	private static boolean isWindows() {
		String osName = System.getProperty("os.name");
		System.out.println("os.name:"+osName);
		return osName.startsWith("Windows");
	}
	
	private static boolean isNimbusVisible() {
		try {
			Class c = Class.forName("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			return c != null;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
 * ����������۸��ݲ���ϵͳ��ѡȡ���ʵ�ȱʡ���
 */
	public static void setDefaultLNF(){
		String lnf;
		
		if( isWindows() ){
			lnf =  "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		}else if (isNimbusVisible()){
			lnf = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
		}else{
			lnf = UIManager.getSystemLookAndFeelClassName();
		}
		try{
			UIManager.setLookAndFeel(lnf);
		}catch(Exception x){
			x.printStackTrace();
		}
	}
	
	static UnitServerConsole webStartServer = null;
	/**
	 * ʹ�ø÷���������web���������ɽڵ��
	 * @return ������������true
	 * @throws Exception
	 */
	public static boolean startUnitServer() throws Exception{
		if(webStartServer==null){
			setDefaultLNF();
			webStartServer = new UnitServerConsole(null, 0);
		}
		
		webStartServer.setVisible(true);
		return webStartServer.webStartUnitServer();
	}
	
	/**
	 * �����õ��жϷ������Ƿ��Ѿ�����
	 * @return �����з���true�����򷵻�false
	 */
	public static boolean isUnitServerRunning(){
		if(webStartServer!=null){
			return webStartServer.isWebUnitServerRunning();
		}
		return false;
	}
	
	/**
	 * �����ã���ͣ������
	 * @throws Exception
	 */
	public static void stopUnitServer() throws Exception{
		if(webStartServer!=null){
			webStartServer.webStopUnitServer();
		}
	}

	static void exit(int second){
		sleep(second);
		System.exit(0);
	}
	
	static void sleep(int second){
		try {
			Thread.sleep(second*1000);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * ����������̨��ں���
	 * @param args ִ�в���
	 */
	public static void main(String[] args) {
		String usage = "�������ѡ����������ֹͣ���ַ��񣬸�ʽΪ ServerConsole.sh -[options] -[options]...\r\n"
				+ "��ָ����ĳ��ѡ������������Ӧ����ʱ������������ͼ�λ����µĸ������\r\n"
				+ "Ҳ���Բ����κ�ѡ���ʾ�����������̨����[ͼ�δ��ڿ���̨]��\r\n"
				+ "��������ѡ����� -a , -x ����ͬʱ���֣�����ѡ�������ϡ�\r\n\r\n"
				+ "-p[ip:port]	�����ֻ� ����ʡ��ip:portʱ���Զ�˳��Ѱ��һ��û��ռ�õķֻ����á�\r\n"
				+ "-c port cfg	ʹ������cfg������ֹͣ�ֻ� ����ʡ��cfgʱ����ֹͣ�ֻ���\r\n"
				+ "-o	���� ODBC ����\r\n"
				+ "-h	���� HTTP ����\r\n"
				+ "-x[ip:port]	ָֹͣ���ֻ�����ʡ��ip:portʱ��ֹͣ�������������з���\r\n"
				+ "-a	�������з���\r\n"
				+ "-?	���ߴ���ѡ��ʱ����ӡ��ǰ������Ϣ��\r\n\r\n"
				+ " ʾ����ServerConsole.sh -a  ����ȫ������,�൱�� ServerConsole.sh -p -o -h\r\n\r\n"
				+ " ʾ����ServerConsole.sh -p 127.0.0.1:8281  ����ָ��ip�ֻ�\r\n\r\n"
				+ " ʾ����ServerConsole.sh -o  ������odbc����\r\n\r\n"
		;
		
		String usageEn = "The class starts or stops services according to the option used, format is ServerConsole.sh -[options] -[options]....\r\n"
				+ "When an option is specified to start the corresponding service, it starts the service in a non-GUI environment.\r\n"
				+ "When no option is specified, start the service console [graphics console].\r\n"
				+ "In the following options, any options can work together except for -a and -x.\r\n\r\n"
				+ "-p[ip:port]	Start a node; wehn ip:port are absent, automatically find an idle node.\r\n"
				+ "-c port cfg	Use configuration cfg to start or stop a node; when cfg is absent, just stop the node.\r\n"
				+ "-o	Start ODBC service.\r\n"
				+ "-h	Start HTTP service.\r\n"
				+ "-x[ip:port]	Stop assigning nodes; when ip:port are absent, stop all working services on the local machine. \r\n"
				+ "-a	Start all services.\r\n"
				+ "-?	Or print the current help information when the error option is present. \r\n\r\n"
				+ " Example��ServerConsole.sh -a  Start all services, which is equivalent to ServerConsole.sh -p -o -h\r\n\r\n"
				+ " Example��ServerConsole.sh -p 127.0.0.1:8281  Start a node with specified ip.\r\n\r\n"
				+ " Example��ServerConsole.sh -o  Start odbc service only.\r\n\r\n";
		String lang = Locale.getDefault().toString();
		if(lang.equalsIgnoreCase("en")){
			usage = usageEn;			
		}
		doMain( args, usage );
	}
	
	public static void doMain(String[] args,String usage) {
		boolean init = true;
		
		String arg;
		if (args.length == 1) { //
			arg = args[0].trim();
			if (arg.trim().indexOf(" ") > 0) {
				if (arg.charAt(1) != ':') {// ����·�����ļ������� [�̷�]:��ͷ
					// �����������Ϊһ���ļ���ʱ����Ҫ������ת�������ļ��������ո�ʱ���ʹ��� xq 2017��5��23��
					Section st = new Section(arg, ' ');
					args = st.toStringArray();
				}
			}
		}
		
		boolean printHelp = false;
		boolean isP = false, isO = false, isH = false, isX = false, isC = false;
		// -shost:port �ڲ�����
		boolean isS = false;
		String host = null,cfgPath=null;
		int port = 0;

		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				arg = args[i];
				if (arg.equalsIgnoreCase("com.scudata.ide.spl.ServerConsole")) { // ��bat�򿪵��ļ�������������ǲ���
					continue;
				}
				if (arg.equalsIgnoreCase("-a")) {
					isP = true;
					isO = true;
					isH = true;
					break;
				}
				if (arg.toLowerCase().startsWith("-p")) {
					int index = arg.indexOf(':');
					String address=null;
					if(index>0){
						address = arg.substring(2).trim();
					}
					else if(index<0 && i+1<args.length){
						address = args[i+1];
						index = address.indexOf(':');
						if(index>0){
							i++;
						}
					}
					if(index>0 && address!=null){
						String tmp = address;
						UnitClient uc = new UnitClient( tmp );
						host = uc.getHost();
						port = uc.getPort();
					}
					isP = true;
					continue;
				}
				if (arg.equalsIgnoreCase("-o")) {
					isO = true;
					continue;
				}
				if (arg.equalsIgnoreCase("-h")) {
					isH = true;
					continue;
				}
				if (arg.toLowerCase().startsWith("-x")) {
					int index = arg.indexOf(':');
					String address=null;
					if(index>0){
						address = arg.substring(2).trim();
					}
					else if(index<0 && i+1<args.length){
						address = args[i+1];
						index = address.indexOf(':');
						if(index>0){
							i++;
						}
					}
					if(index>0 && address!=null){
						String tmp = address;
						UnitClient uc = new UnitClient( tmp );
						host = uc.getHost();
						port = uc.getPort();
					}
					isX = true;
					break;
				}
				if (arg.toLowerCase().startsWith("-s")) {
					isS = true;
					int index = arg.indexOf(':');
					host = arg.substring(2, index).trim();
					port = Integer.parseInt(arg.substring(index + 1).trim());
					break;
				}
				if (arg.toLowerCase().startsWith("-c")) {
					host = "127.0.0.1";//Ŀǰȱʡ
					if(i+1<args.length){
						String buf = args[i+1];
						port = Integer.parseInt(buf);
						i++;
					}
					if(i+1<args.length){
						cfgPath = Escape.removeEscAndQuote( args[i+1] );
						Logger.debug("Config file:"+cfgPath);
						i++;
					}
					if(cfgPath==null) {
						isX = true;
					}else {
						isC = true;
						InputStream is;
						try {
							is = new FileInputStream( cfgPath );
							SplServerConfig ssc = SplServerConfig.getCfg(is);
							init = !StringUtils.isValidString( ssc.splConfig );//���ָ����config������ֹ�ڵ����config����
							is.close();
						} catch (Exception e) {
							
						}

					}
					break;
				}
				//���������κ�һ��ѡ��ʱ
				printHelp = true;
			}
		}
		
		if ( printHelp ) {
			System.err.println(usage);
			System.err.println("Press enter to exit.");
			try{
				System.in.read();
			}catch(Exception x){
			}
			exit(0);
		}
		
		initLang( init );

		if (isS) {
			try {
				RaqsoftConfig rc = loadRaqsoftConfig();
				UnitServer server = UnitServer.getInstance(host, port);
				runningServers.add(server);
				server.setRaqsoftConfig(rc);
				server.run();
			} catch (Exception x) {
				x.printStackTrace();
			}
			System.exit(0);
		}

		if (isC) {
			try {
				UnitServer server = UnitServer.getInstance(host, port, cfgPath);
				runningServers.add(server);
				server.run();
			} catch (Exception x) {
				x.printStackTrace();
				System.exit(-1);
			}
			System.exit(0);
		}
		/***************************** �ر����з��� ******************************/
		if (isX) {
			// UnitServer
			try {
				if(host!=null){
					ShutdownUnitServer.close(host,port);
					System.exit(0);
				}else{
					ShutdownUnitServer.autoClose();
				}
			} catch (Exception x) {
				// x.printStackTrace();
			}
			// OdbcServer
			try {
				OdbcContext ctx = new OdbcContext();
				host = ctx.getHost();
				port = ctx.getPort();
				Socket s = new Socket();
				s.connect(new InetSocketAddress(host, port), 1000);
				OutputStream os = s.getOutputStream();
				DataTypes.writeInt(os, -1);
				os.close();
			} catch (Exception e) {
			}
			// HttpServer
			try {
				HttpContext ctx = new HttpContext(true);
				host = ctx.getHost();
				port = ctx.getPort();
				String durl = ctx.getDefaultUrl();
				URL url = new URL(durl + "/shutdown");
				URLConnection uc = url.openConnection();
				InputStream is = uc.getInputStream();
				is.close();
			} catch (Exception e) {
			}
			System.exit(0);
		}

		RaqsoftConfig rc = null;
		try {
			rc = loadRaqsoftConfig();
		} catch (Exception x) {
			x.printStackTrace();
			exit(3);
		}

		/***************************** ����ͼ�ο���̨ ******************************/
		if (!isP && !isO && !isH) {
			setDefaultLNF();
			UnitServerConsole usc = new UnitServerConsole(null, 0);
			runningServers.add(usc);
			usc.setVisible(true);
			return;
		}

		/***************************** ����ָ������ ******************************/
		Thread tp = null, to = null;
		final ArrayList<InputStreamFlusher> flushers = new ArrayList<InputStreamFlusher>();

		// �����ֻ�
		if (isP) {
			try {
				UnitServer server = UnitServer.getInstance(host, port);
				runningServers.add(server);
				server.setRaqsoftConfig(rc);
				tp = new Thread(server);
				tp.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// ����OdbcServer
		if (isO) {
			try {
				OdbcServer server = OdbcServer.getInstance();
				runningServers.add(server);
				server.setRaqsoftConfig(rc);
				to = new Thread(server);
				to.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// ����HttpServer
		SplxServerInIDE thServer = null;
		if (isH) {
			try {
				thServer = SplxServerInIDE.getInstance();
				runningServers.add(thServer);
				thServer.setRaqsoftConfig(rc);
				thServer.start();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

		try {
			if (tp != null) {
				tp.join();
			}
			if (to != null) {
				to.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (thServer != null) {
			while (thServer.isRunning()) {
				sleep(3);
			}
		}

		for (InputStreamFlusher flusher : flushers) {
			flusher.shutDown();
		}
		exit(3);
	}
}

class InputStreamFlusher extends Thread {
	InputStream is;
	int port = 0;
	boolean stop = false;

	public InputStreamFlusher(InputStream is, int port) {
		this.is = is;
		this.port = port;
	}

	public void shutDown() {
		stop = true;
	}

	public void run() {
		BufferedReader br1 = new BufferedReader(new InputStreamReader(is));
		try {
			String line1 = null;
			while ((line1 = br1.readLine()) != null && !stop) {
				if (line1 != null) {
					System.out.println("[" + port + "] " + line1);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
			}
		}
	}
}
