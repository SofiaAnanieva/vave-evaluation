package org.argouml.model.mdr;

import java.io.IOException;
import java.io.Writer;
import org.netbeans.lib.jmi.xmi.WriterBase;
import org.netbeans.lib.jmi.xmi.XMIHeaderProvider;


class XmiHeaderProviderImpl implements XMIHeaderProvider {
	private static final String UML_VERSION = "1.4";
	private String version;
	public XmiHeaderProviderImpl(String ver) {
		version = ver;
	}
	public void writeHeader(Writer ps) {
		String header = "    <XMI.documentation>\n" + "      <XMI.exporter>ArgoUML" + " (using " + WriterBase.EXPORTER_NAME + " version " + WriterBase.EXPORTER_VERSION + ")</XMI.exporter>\n" + "      <XMI.exporterVersion>" + version + " revised on " + "$Date: 2010-09-25 19:23:13 -0300 (sáb, 25 set 2010) $ " + "</XMI.exporterVersion>\n" + "    </XMI.documentation>\n" + "    <XMI.metamodel xmi.name=\"UML\" xmi.version=\"" + UML_VERSION + "\"/>";
		try {
			ps.write(header);
		}catch (IOException e) {
		}
	}
}



