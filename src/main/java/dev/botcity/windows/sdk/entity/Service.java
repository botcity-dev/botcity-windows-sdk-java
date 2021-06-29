package dev.botcity.windows.sdk.entity;

/**
 * <code>Service</code> is a class that contains the info about the service.
 * 
 * @see dev.botcity.windows.sdk.execute.WindowsSDK
 * @see dev.botcity.windows.sdk.entity.Application
 * 
 * @author <a href="https://github.com/gvom">Gabriel Meneses</a>
 */
public class Service {

	private String Id;
	private String ServiceName;

	/**
	 *
	 * Gets the identifier
	 *
	 * @return the identifier
	 */
	public String getId() {

		return Id;
	}

	/**
	 *
	 * Sets the identifier
	 *
	 * @param id the id
	 */
	public void setId(String id) {

		Id = id;
	}

	/**
	 *
	 * Gets the service name
	 *
	 * @return the service name
	 */
	public String getServiceName() {

		return ServiceName;
	}

	/**
	 *
	 * Sets the service name
	 *
	 * @param serviceName the service name
	 */
	public void setServiceName(String serviceName) {

		ServiceName = serviceName;
	}
}
