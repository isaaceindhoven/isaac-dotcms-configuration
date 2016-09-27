package nl.isaac.comp.configuration.types;

/**
 * This enum specifies the environment the application is running in. It should be used in all applications 
 * @author jan-willem
 *
 */
public enum EnvironmentType {

	/**
	 * Local development workstation (e.g. Eclipse)
	 */
	LOCAL(true),
	
	/**
	 * Development environment (non development machine)
	 */
	DEV(true),
	
	/**
	 * UAT environment
	 */
	UAT(true),
	
	/**
	 * Production environment
	 */
	PROD(false);
	
	private final boolean isTestEnvironment;
	
	private EnvironmentType(boolean isTestEnvironment) {
		this.isTestEnvironment = isTestEnvironment;
	}
	
	/**
	 * Returns true if this is not a production environment
	 * @return
	 */
	public boolean isTestEnvironment() {
		return isTestEnvironment;
	}
}
