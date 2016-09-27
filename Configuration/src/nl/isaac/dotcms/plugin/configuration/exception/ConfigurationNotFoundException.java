package nl.isaac.dotcms.plugin.configuration.exception;
/**
* dotCMS Configuration plugin by ISAAC - The Full Service Internet Agency is licensed 
* under a Creative Commons Attribution 3.0 Unported License
* - http://creativecommons.org/licenses/by/3.0/
* - http://www.geekyplugins.com/
* 
* @copyright Copyright (c) 2011 ISAAC Software Solutions B.V. (http://www.isaac.nl)
*/

public class ConfigurationNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 2L;

	public ConfigurationNotFoundException(String message) {
		super(message);
	}

}
