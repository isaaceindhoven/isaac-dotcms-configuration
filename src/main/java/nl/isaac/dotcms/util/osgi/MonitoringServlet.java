package nl.isaac.dotcms.util.osgi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.view.context.ChainedContext;

import com.dotmarketing.util.VelocityUtil;

public class MonitoringServlet extends HttpServlet {
	private static final String testMacro = "#macro(test $boolean)#if($boolean) OK\n#else NOK\n#end#end\n";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String velocity = getMonitoringVelocity();
		ChainedContext velocityContext = getStrictVelocityContext(request, response);

		try {
			String result = VelocityUtil.eval(velocity, velocityContext);
			if(result.contains("$") || result.contains("NOK")) {
				response.setStatus(500);
			}
			response.getWriter().write(result);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private ChainedContext getStrictVelocityContext(HttpServletRequest request,	HttpServletResponse response) {
		ChainedContext velocityContext = VelocityUtil.getWebContext(request, response);
		velocityContext.getVelocityEngine().setProperty(VelocityEngine.RUNTIME_REFERENCES_STRICT, true);
		velocityContext.getVelocityEngine().setProperty(VelocityEngine.RUNTIME_REFERENCES_STRICT_ESCAPE, true);
		return velocityContext;
	}

	private String getMonitoringVelocity() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		URL url = classLoader.getResource("");

		// for example: /D:/java/dotcms_3.7.1/dotserver/tomcat-8.0.18/lib/
		String tomcatLibPath = url.getPath();
		String configurationPluginPath = "";
		String[] folders = tomcatLibPath.split("/");

		// -3 because we have to skip the folders dotserver, tomcat-X and lib
		for (int i = 0; i < folders.length - 3; i++) {
			configurationPluginPath += folders[i] + "/";
		}

		String monitoringVtlPath = configurationPluginPath + "plugins/isaac-dotcms-configuration/ext/monitoring.vtl";

		File file = new File(monitoringVtlPath);

		if (file.exists() && file.isFile()) {

			String velocity = "";

			try (InputStream is = new FileInputStream(file)) {
				velocity = IOUtils.toString(is, "UTF-8");
			} catch (IOException e) {
				return null;
			}

			return testMacro + velocity;
		}

		return "/ext/monitoring.vtl does not exist";
	}

}
